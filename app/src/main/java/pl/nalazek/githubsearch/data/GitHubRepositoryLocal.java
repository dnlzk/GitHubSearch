package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;

import java.util.List;

import pl.nalazek.githubsearch.data.QueryObjects.SearchQueryHistory;
import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.ResultCreator;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 * @author Daniel Nalazek
 */
public class GitHubRepositoryLocal implements GitHubRepository {

    private static SearchQueryHistory searchQueryHistory;

    private GitHubRepositoryAPIInterface gitHubAPI = null;
    private String keyword;
    private SearchResult resultForRequest;
    private GitHubRepositorySearchOptions searchOptions;
    private SearchResultsCallback searchResultsCallback;
    private DetailedResultsCallback detailedResultsCallback;



    public GitHubRepositoryLocal(GitHubRepositoryAPIInterface gitHubRepositoryAPIInterface) {
        gitHubAPI = gitHubRepositoryAPIInterface;
        searchQueryHistory = new SearchQueryHistory();
    }



    @Override
    public void requestSearch(@NonNull String keyword,
                              @NonNull GitHubRepositorySearchOptions searchOptions,
                              @NonNull SearchResultsCallback searchResultsCallback) {

        this.keyword = keyword;
        this.searchResultsCallback = searchResultsCallback;
        this.searchOptions = searchOptions;

        if (searchOptions.isForcedSearchInHistory()) {
            if (findInHistory()) return;
        }

        doSearchInAPI();
    }



    @Override
    public void requestDetailedData(@NonNull SearchResult searchResult,
                                    @NonNull DetailedResultsCallback detailedResultsCallback) {

        this.resultForRequest = searchResult;
        this.detailedResultsCallback = detailedResultsCallback;

        doRequestInAPI();
    }



    @Override
    public void stopSearch() {
        gitHubAPI.stopLastTask();
    }



    @Override
    public void close() {
        try {
            gitHubAPI.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
            shouldCallbackOnError(e.toString());
        }
    }




    private boolean findInHistory() {

        if(searchQueryHistory.isKeywordInHistory(keyword)) {
            List<? extends Result> results = getSearchResultsFromHistory(keyword);
            checkResultsAndCallback(results);
            return true;
        }
        else return false;
    }



    private void checkResultsAndCallback(List<? extends Result> results) {
        if(results.isEmpty()) searchResultsCallback.onError("Returned empty score");
        else searchResultsCallback.onSearchResultsReady(results);
    }



    private void doSearchInAPI() {

        gitHubAPI.startSearch(keyword, searchOptions,
                new GitHubRepositoryAPIInterface.SearchAPICallback() {
                    @Override
                    public void onResultsReady(ResponsePackage responsePackage) {
                        searchQueryHistory.put(keyword, responsePackage);
                        searchResultsCallback.onSearchResultsReady(makeResults(responsePackage));
                    }

                    @Override
                    public void onError(String message) {
                        if(shouldCallbackOnError(message))
                            searchResultsCallback.onError(message);
                    }
                });
    }



    private List<? extends Result> makeResults(ResponsePackage responsePackage) {
        return new ResultCreator().makeResults(responsePackage);
    }



    private void doRequestInAPI() {

        gitHubAPI.getDetailedData(resultForRequest,
                new GitHubRepositoryAPIInterface.DetailedDataAPICallback() {
                    @Override
                    public void onResultsReady(ResponsePackage responsePackage) {
                        detailedResultsCallback.onDetailedDataResultReady(makeResults(responsePackage));
                    }

                    @Override
                    public void onError(String message) {
                        if(shouldCallbackOnError(message))
                            detailedResultsCallback.onError(message);
                    }
        });
    }



    private List<? extends Result> getSearchResultsFromHistory(String keyword) {
        ResponsePackage responsePackage = searchQueryHistory.get(keyword);
        return makeResults(responsePackage);
    }



    private boolean shouldCallbackOnError(String error) {

        switch(error) {
            case QueryTask.STATE_INTERRUPTED:       return false;
            case QueryTask.STATE_CONNECTION_ERROR:  return true;
            case QueryTask.STATE_SUCCESS:           return false;

            case GitHubRepositoryAPI.STATE_MALFORMED_URL:           return true;
            case GitHubRepositoryAPI.STATE_NULL_QUERY:              return true;
            case GitHubRepositoryAPI.STATE_BUFFER_COMBINE_ERROR:    return true;

            default: return true;
        }
    }



}
