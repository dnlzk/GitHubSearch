/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.search;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.GitHubRepositorySearchOptions;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.ResultObjects.Result;

/**
 * This class is a controller responsible for piping requests and callbacks between the view and the model.
 */
public class SearchPresenter implements SearchContract.UserActionListener {


    public static final String CHECK_IN_HISTORY = "CheckInHistory";
    public static final String STOP_SEARCH = "StopSearch";
    public static final String RESULTS_PER_PAGE_OUT_OF_RANGE_ERROR = "Results per page setting " +
            "out of range! Acceptable values are between 1 and 100.";
    public static final short DEFAULT_PER_PAGE_VALUE = 50;

    private short resultsPerPage = DEFAULT_PER_PAGE_VALUE;
    private Boolean scopeUsers = true;
    private Boolean scopeRepos = true;
    private SearchQuery.Sort sorting = SearchQuery.Sort.BEST;
    private SearchQuery.Order ordering = SearchQuery.Order.ASCENDING;
    private Boolean sortById = true;
    private boolean searchInHistory = false;
    private GitHubRepositorySearchOptions options;

    private SearchContract.Showable showable;
    private GitHubRepository gitHubRepository;



    public SearchPresenter(GitHubRepository gitHubRepository, SearchContract.Showable showable) {
        this.gitHubRepository = gitHubRepository;
        this.showable = showable;
        setSearchOptions();
    }



    /**
     * Use to set the scope of searching
     * @param searchScope pass the SearchScope enums to define the scope or none to disable searching
     */
    public void setSearchScope(SearchQuery.SearchScope... searchScope) {
        scopeUsers = false;
        scopeRepos = false;
        for(SearchQuery.SearchScope scope : searchScope) {
            switch(scope){
                case USERS: scopeUsers = true; break;
                case REPOSITORIES: scopeRepos = true; break;
            }
        }
        setSearchOptions();
    }



    /**
     * Use to set the number of results viewed. Note that if you set the scope both to USERS
     * and REPOSITORIES the number of results viewed will be doubled.
     * Example: passing number = 30
     * if the scope is set only to USERS there will be 30 results on page,
     * if the scope is set to both USERS and REPOSITORIES there will be 60 results on page
     * @param number the number of results, MIN=1, MAX=100
     * @see SearchPresenter#setSearchScope(SearchQuery.SearchScope...)
     */
    public void setResultsNumber(short number){
        resultsPerPage = number;
        setSearchOptions();
    }



    public GitHubRepositorySearchOptions getSearchOptions() {
        return options;
    }



    @Override
    public void finish() {
        gitHubRepository.close();
    }



    @Override
    public void requestSearch(String keyword, boolean searchInHistory) {

        this.searchInHistory = searchInHistory;
        showable.showBusy();
        setSearchOptions();
        createSearchRequest(keyword, options);
    }



    @Override
    public void stopSearch() {
        gitHubRepository.stopSearch();
        showable.hideBusy();
    }




    private void createSearchRequest(String keyword, GitHubRepositorySearchOptions options) {

        gitHubRepository.requestSearch(keyword, options,
                new GitHubRepository.SearchResultsCallback() {

                    @Override
                    public void onSearchResultsReady(List<? extends Result> results) {
                        if(sortById) showable.showResults(getSortedById(results));
                        else showable.showResults(results);
                    }



                    @Override
                    public void onError(String message) {
                        showable.showError(message);
                    }



                    private List<? extends Result> getSortedById(List<? extends Result> results) {
                        Collections.sort(results, new Comparator<Result>() {
                            public int compare(Result r1, Result r2) {
                                return Integer.compare(r1.getId() , r2.getId());
                            }
                        });
                        return results;
                    }
                });
    }



    private void setSearchOptions() {
        options = tryToGetGitHubRepositorySearchOptions();
    }



    private GitHubRepositorySearchOptions tryToGetGitHubRepositorySearchOptions() {
        try {
                return   GitHubRepositorySearchOptions.build()
                        .setOrdering(ordering)
                        .setSorting(sorting)
                        .setResultsPerPage(resultsPerPage)
                        .setScope(scopeUsers, scopeRepos)
                        .forceSearchInHistory(searchInHistory);
        }
        catch (IllegalArgumentException e) {

            showable.showError(RESULTS_PER_PAGE_OUT_OF_RANGE_ERROR);
            resultsPerPage = DEFAULT_PER_PAGE_VALUE;
            return tryToGetGitHubRepositorySearchOptions();
        }
    }
}

