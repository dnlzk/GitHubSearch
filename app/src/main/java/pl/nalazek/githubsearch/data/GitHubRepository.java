package pl.nalazek.githubsearch.data;

import java.util.ArrayList;

import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.UserDetailedResult;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

/**
 * This interface is the main entry point for accessing GitHub repository
 * @author Daniel Nalazek
 */

public interface GitHubRepository {

    void requestSearch(String phrase, GitHubRepositorySearchOptions searchOptions);

    void requestUserDetailed(UserSearchResult userSearchResult);

    interface SearchResultsCallback {

        void onSearchResultsReady(ArrayList<Result> results);
    }

    interface UserDetailedCallback {

        void onUserDetailedResultReady(UserDetailedResult result);
    }

}
