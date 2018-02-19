package pl.nalazek.githubsearch.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import pl.nalazek.githubsearch.FakeGitHubRepositoryAPI;
import pl.nalazek.githubsearch.Injection;
import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.timeout;

/**
 * @author Daniel Nalazek
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubRepositoryTest {


    private GitHubRepository gitHub;
    private GitHubRepositorySearchOptions.GitHubRepositorySearchOptionsBuilder optionsBuilder;
    private static final long TIMEOUT = 5000;
    private static final long TESTTIMEOUT = 1000;

    @Spy
    GitHubRepository.SearchResultsCallback searchResultsCallback;
    @Spy
    GitHubRepository.DetailedResultsCallback detailedResultsCallback;

    @Mock
    SearchResult searchResult;

    @Captor
    ArgumentCaptor<List<? extends Result>> resultsCaptor;
    @Captor
    ArgumentCaptor<String> errorCaptor;



    @Before
    public void before() {
        gitHub = Injection.provideGitHubRepository();
        optionsBuilder = new GitHubRepositorySearchOptions.GitHubRepositorySearchOptionsBuilder();
    }



    @Test
    public void whenRequestSearchTwiceSameThenCallbackWithErrorEmptyScore() throws Exception {

        requestSearch("Test", optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onSearchResultsReady(resultsCaptor.capture());

        requestSearch("Test", optionsBuilder.forceSearchInHistory(true), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onError(errorCaptor.capture());

        assertTrue("Empty score error", errorCaptor.getValue().contains("Returned empty score"));
    }



    @Test
    public void whenRequestSearchTwiceOtherThenCallback() throws Exception {

        requestSearch("Test", optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onSearchResultsReady(resultsCaptor.capture());

        requestSearch("Test1", optionsBuilder.forceSearchInHistory(true), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onSearchResultsReady(resultsCaptor.capture());
    }



    @Test
    public void whenRequestSearchThenCallback() throws Exception {

        requestSearch("Test", optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onSearchResultsReady(resultsCaptor.capture());
    }



    @Test
    public void givenErrorTriggerWhenRequestSearchThenErrorCallback() throws Exception {

        requestSearch(FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE,
                optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onError(errorCaptor.capture());
    }



    @Test
    public void givenLongTaskTriggerWhenRequestSearchAndStopThenStopCall() throws Exception {

        requestSearch(FakeGitHubRepositoryAPI.GENERATE_LONG_TASK,
                optionsBuilder.forceSearchInHistory(false), searchResultsCallback);

        gitHub.stopSearch();

        Mockito.verify(searchResultsCallback, timeout(TIMEOUT)).onError(errorCaptor.capture());
        assertTrue("Interrupted error value error", errorCaptor.getValue().equals("Interrupted task"));
    }



    @Test
    public void givenSearchResultWithEmptyTitleWhenRequestDetailedDataThenCallback() throws Exception {

        searchResult = new UserSearchResult("","","http://a.com","http://a.com","http://a.com",15);
        gitHub.requestDetailedData(searchResult, detailedResultsCallback);

        Mockito.verify(detailedResultsCallback, timeout(TIMEOUT)).onDetailedDataResultReady(resultsCaptor.capture());
    }



    @Test
    public void givenSearchResultWithErrorTriggerWhenRequestDetailedThenErrorCallback() throws Exception {

        searchResult = new UserSearchResult(
                FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE,
                "",
                "http://a.com",
                "http://a.com",
                "http://a.com",
                15);

        gitHub.requestDetailedData(searchResult, detailedResultsCallback);

        Mockito.verify(detailedResultsCallback, timeout(TIMEOUT)).onError(errorCaptor.capture());
    }




    private void requestSearch(String test, GitHubRepositorySearchOptions searchOptions,
                               GitHubRepository.SearchResultsCallback searchResultsCallback)
                                throws InterruptedException {

        gitHub.requestSearch(test, searchOptions, searchResultsCallback);
    }


}