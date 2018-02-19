package pl.nalazek.githubsearch.search;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import pl.nalazek.githubsearch.FakeGitHubRepositoryAPI;
import pl.nalazek.githubsearch.Injection;
import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.GitHubRepositorySearchOptions;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.ResultObjects.Result;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;

import static pl.nalazek.githubsearch.search.SearchPresenter.DEFAULT_PER_PAGE_VALUE;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchPresenterTest {


    private SearchPresenter searchPresenter;
    private static final String SEARCH_PHRASE = "test";

    @Spy
    SearchContract.Showable showable;
    @Spy
    GitHubRepository repo =  Mockito.spy(Injection.provideGitHubRepository());

    @Captor
    ArgumentCaptor<String> errorCaptor;
    @Captor
    ArgumentCaptor<List<? extends Result>> resultsCaptor;


    @Before
    public void before() {
        searchPresenter = new SearchPresenter(repo,showable);
    }



    @Test
    public void whenGetSearchOptionsThenDefaults() throws Exception {

        GitHubRepositorySearchOptions options = searchPresenter.getSearchOptions();

        assertThat("Order fault", options.getOrder(), is(SearchQuery.Order.ASCENDING));
        assertThat("Sort fault", options.getSorting(), is(SearchQuery.Sort.BEST));
        assertThat("Per page fault", options.getResultsPerPage(), is(DEFAULT_PER_PAGE_VALUE));
        assertThat("Scope users fault", options.isScopeUsers(), is(true));
        assertThat("Scope repos fault", options.isScopeRepos(), is(true));
        assertThat("History force fault", options.isForcedSearchInHistory(), is(false));
    }



    @Test
    public void whenSetWrongPerPageValueThenShowableOnErrorAndDefault() throws Exception {

        short[] wrongValues = {-5,0,102};

        for(short value : wrongValues) {
            searchPresenter.setResultsNumber(value);
            assertThat("Default per page fault", searchPresenter.getSearchOptions().getResultsPerPage(),
                    is(DEFAULT_PER_PAGE_VALUE));
        }

        Mockito.verify(showable, times(3)).showError(errorCaptor.capture());
    }



    @Test
    public void whenSetGoodPerPageValueThenNotShowableOnErrorAndValue() throws Exception {

        short[] goodValues = {1,5,99};
        for(short value : goodValues) {
            searchPresenter.setResultsNumber(value);
            assertThat("Per page fault", searchPresenter.getSearchOptions().getResultsPerPage(),
                    is(value));
        }
        Mockito.verify(showable, times(0)).showError(errorCaptor.capture());
    }



    @Test
    public void whenSetSearchScopeUsersFalseThenValue() throws Exception {

        SearchQuery.SearchScope[][] scopes = getAllSearchScopesCombinations();

        for(SearchQuery.SearchScope[] scope : scopes) {
            searchPresenter.setSearchScope(scope);

            if(scope.length == 0) {
                assertAllFalseScope();
            }
            else {
                    for (SearchQuery.SearchScope s : scope) {

                        if (s.equals(SearchQuery.SearchScope.USERS))
                            assertUsersScopeTrueOnly(scope);
                        if (s.equals(SearchQuery.SearchScope.REPOSITORIES))
                            assertReposScopeTrueOnly(scope);
                    }
            }
        }
    }



    @Test
    public void givenSameInstanceRepoWhenFinishThenCloseCallOnRepo() throws Exception {
        searchPresenter.finish();
        Mockito.verify(repo).close();
    }



    @Test
    public void givenSameInstanceRepoWhenStopSearchThenStopSearchCallOnRepoAndHideBusy() throws Exception {
        searchPresenter.stopSearch();
        Mockito.verify(repo).stopSearch();
        Mockito.verify(showable).hideBusy();
    }



    @Test
    public void whenRequestedSearchWithPhraseThenShowBusyAndResultsCallback() throws Exception {
        searchPresenter.requestSearch(SEARCH_PHRASE, true);
        Mockito.verify(showable).showBusy();
        Mockito.verify(showable, timeout(100)).showResults(resultsCaptor.capture());
    }



    @Test
    public void whenRequestedSearchWithErrorPhraseThenShowBusyAndErrorCallback() throws Exception {
        searchPresenter.requestSearch(FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE, true);
        Mockito.verify(showable).showBusy();
        Mockito.verify(showable, timeout(100)).showError(errorCaptor.capture());
    }




    @NonNull
    private SearchQuery.SearchScope[][] getAllSearchScopesCombinations() {

        return new SearchQuery.SearchScope[][]{
                {SearchQuery.SearchScope.USERS},
                {SearchQuery.SearchScope.USERS, SearchQuery.SearchScope.REPOSITORIES},
                {SearchQuery.SearchScope.REPOSITORIES},
                {}
        };
    }



    private void assertReposScopeTrueOnly(SearchQuery.SearchScope[] scope) {

        assertThat("Repo scope true fault", searchPresenter.getSearchOptions()
                .isScopeRepos(), is(true));
        if(scope.length == 1) assertThat("User scope singleton fault", searchPresenter.getSearchOptions()
                .isScopeUsers(), is(false));
    }



    private void assertUsersScopeTrueOnly(SearchQuery.SearchScope[] scope) {

        assertThat("User scope true fault", searchPresenter.getSearchOptions()
                .isScopeUsers(), is(true));
        if(scope.length == 1) assertThat("Repo scope singleton fault", searchPresenter.getSearchOptions()
                .isScopeRepos(), is(false));
    }



    private void assertAllFalseScope() {

        assertThat("User scope false fault", searchPresenter.getSearchOptions().isScopeUsers(),
                is(false));
        assertThat("Repo scope false fault", searchPresenter.getSearchOptions().isScopeRepos(),
                is(false));
    }
}