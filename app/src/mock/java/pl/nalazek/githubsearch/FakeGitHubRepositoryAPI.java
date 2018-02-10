package pl.nalazek.githubsearch;

import android.support.annotation.NonNull;

import pl.nalazek.githubsearch.data.GitHubRepositoryAPIInterface;
import pl.nalazek.githubsearch.data.GitHubRepositorySearchOptions;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 * @author Daniel Nalazek
 */

public class FakeGitHubRepositoryAPI implements GitHubRepositoryAPIInterface {

    public final static String GENERATE_ERROR_PHRASE = "genError";
    public final static String GENERATE_LONG_TASK = "genLongTask";

    private final static int LATENCY = 50;

    private Thread queryTaskMock;
    private SearchAPICallback searchAPICallback;



    @Override
    public synchronized void startSearch(@NonNull final String phrase,
                            @NonNull final GitHubRepositorySearchOptions searchOptions,
                            @NonNull final SearchAPICallback searchAPICallback) {

        this.searchAPICallback = searchAPICallback;

        queryTaskMock = new Thread(new Runnable() {
            @Override
            public void run() {

                generateLatency(LATENCY);
                if(phrase.equals(GENERATE_ERROR_PHRASE)) searchAPICallback.onError("Generated error");
                else if(phrase.equals(GENERATE_LONG_TASK)) generateLatency(10000);
                else searchAPICallback.onResultsReady(new ResponsePackage(SearchQuery.TYPE));
            }
        });
        queryTaskMock.start();
    }



    @Override
    public synchronized void getDetailedData(@NonNull final SearchResult searchResult,
                                @NonNull final DetailedDataAPICallback detailedDataCallback) {

        queryTaskMock = new Thread(new Runnable() {
            @Override
            public void run() {

                generateLatency(LATENCY);
                if(searchResult.getTitle().equals(GENERATE_ERROR_PHRASE))
                    detailedDataCallback.onError("Generated error");
                else
                    detailedDataCallback.onResultsReady(new ResponsePackage(UserDetailedQuery.TYPE));
            }
        });
        queryTaskMock.start();
    }



    @Override
    public void finish() throws InterruptedException {
    }



    @Override
    public void stopLastTask() {

        if(queryTaskMock.isAlive()) {
            queryTaskMock.interrupt();
            searchAPICallback.onError("Interrupted task");
        }
    }



    private void generateLatency(int milis) {
        try {
            synchronized (this) {
                wait(milis);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
