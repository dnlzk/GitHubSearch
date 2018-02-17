package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;

import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 * @author Daniel Nalazek
 */

public interface GitHubRepositoryAPIInterface {

    void startSearch(@NonNull String keyword,
                     @NonNull GitHubRepositorySearchOptions searchOptions,
                     @NonNull SearchAPICallback res);



    void getDetailedData(@NonNull SearchResult searchResult,
                         @NonNull DetailedDataAPICallback detailedDataCallback);



    /*
     * Finishes all connections with external data storages. Should be always called to free resources.
     */
    void finish() throws InterruptedException;



    /**
     * Stops last task. Usually used to avoid no more needed result callbacks.
     */
    void stopLastTask();



    interface ResultsAPICallback {
        void onResultsReady(ResponsePackage responsePackage);
        void onError(String message);
    }


    interface SearchAPICallback extends ResultsAPICallback {}


    interface DetailedDataAPICallback extends ResultsAPICallback {}


    interface QueryTaskCallback {
        void onResponseReady(QueryTask queryTask, ResponsePackage responsePackage);
    }
}
