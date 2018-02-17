package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;

import java.util.List;
import pl.nalazek.githubsearch.data.ResultObjects.*;

/**
 * This interface is the main entry point for accessing GitHub repository
 * @author Daniel Nalazek
 */

public interface GitHubRepository {

    void requestSearch(@NonNull String keyword, @NonNull GitHubRepositorySearchOptions searchOptions, @NonNull SearchResultsCallback searchResultsCallback);

    void requestDetailedData(@NonNull SearchResult searchResult, @NonNull DetailedResultsCallback detailedResultsCallback);

    void close();

    void stopSearch();



    interface SearchResultsCallback {
        void onSearchResultsReady(List<? extends Result> results);
        void onError(String message);
    }



    interface DetailedResultsCallback {
        void onDetailedDataResultReady(List<? extends Result> result);
        void onError(String message);
    }
}
