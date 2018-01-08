package pl.nalazek.githubsearch.data;

import java.util.List;
import pl.nalazek.githubsearch.data.ResultObjects.*;

/**
 * This interface is the main entry point for accessing GitHub repository
 * @author Daniel Nalazek
 */

public interface GitHubRepository {

    void requestSearch(String phrase, GitHubRepositorySearchOptions searchOptions);

    void requestUserDetailedData(UserSearchResult userSearchResult);

    interface SearchResultsCallback {

        void onSearchResultsReady(List<Result> results);
    }

    interface UserDetailedCallback {

        void onUserDetailedDataResultReady(UserDetailedResult result);
    }

}
