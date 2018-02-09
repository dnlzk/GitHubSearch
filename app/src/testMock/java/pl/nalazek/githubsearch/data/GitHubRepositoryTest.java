package pl.nalazek.githubsearch.data;

import org.junit.Assert;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubRepositoryTest {


    private GitHubRepository gitHub;
    private GitHubRepositorySearchOptions.GitHubRepositorySearchOptionsBuilder optionsBuilder;
    private static final long TIMEOUT = 200;

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
        Mockito.verify(searchResultsCallback).onSearchResultsReady(resultsCaptor.capture());

        requestSearch("Test", optionsBuilder.forceSearchInHistory(true), searchResultsCallback);
        Mockito.verify(searchResultsCallback).onError(errorCaptor.capture());

        assertTrue("Empty score error", errorCaptor.getValue().contains("Returned empty score"));
    }



    @Test
    public void whenRequestSearchTwiceOtherThenCallback() throws Exception {

        requestSearch("Test", optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback).onSearchResultsReady(resultsCaptor.capture());

        requestSearch("Test1", optionsBuilder.forceSearchInHistory(true), searchResultsCallback);
        Mockito.verify(searchResultsCallback, times(2)).onSearchResultsReady(resultsCaptor.capture());
    }




    @Test
    public void whenRequestSearchThenCallback() throws Exception {

        requestSearch("Test", optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback).onSearchResultsReady(resultsCaptor.capture());
    }



    @Test
    public void givenErrorTriggerWhenRequestSearchThenErrorCallback() throws Exception {

        requestSearch(FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE, optionsBuilder.forceSearchInHistory(false), searchResultsCallback);
        Mockito.verify(searchResultsCallback).onError(errorCaptor.capture());
    }



    @Test
    public void givenLongTaskTriggerWhenRequestSearchAndStopThenStopCall() throws Exception {

        requestSearch(FakeGitHubRepositoryAPI.GENERATE_LONG_TASK, optionsBuilder.forceSearchInHistory(false), searchResultsCallback);

        gitHub.stopSearch();
        waitForThreadStabilized();

        Mockito.verify(searchResultsCallback).onError(errorCaptor.capture());
        assertTrue("Interrupted error value error", errorCaptor.getValue().equals("Interrupted task"));
    }



    @Test
    public void givenSearchResultWithEmptyTitleWhenRequestDetailedDataThenCallback() throws Exception {

        when(searchResult.getTitle()).thenReturn("");

        gitHub.requestDetailedData(searchResult, detailedResultsCallback);
        waitForThreadStabilized();

        Mockito.verify(detailedResultsCallback).onDetailedDataResultReady(resultsCaptor.capture());
    }



    @Test
    public void givenSearchResultWithErrorTriggerWhenRequestDetailedThenErrorCallback() throws Exception {

        when(searchResult.getTitle()).thenReturn(FakeGitHubRepositoryAPI.GENERATE_ERROR_PHRASE);

        gitHub.requestDetailedData(searchResult, detailedResultsCallback);
        waitForThreadStabilized();

        Mockito.verify(detailedResultsCallback).onError(errorCaptor.capture());
    }




    private void requestSearch(String test, GitHubRepositorySearchOptions searchOptions, GitHubRepository.SearchResultsCallback searchResultsCallback) throws InterruptedException {
        gitHub.requestSearch(test, searchOptions, searchResultsCallback);
        waitForThreadStabilized();
    }



    private void waitForThreadStabilized() throws InterruptedException {
        synchronized (this) {
            wait(TIMEOUT);
        }
    }


}