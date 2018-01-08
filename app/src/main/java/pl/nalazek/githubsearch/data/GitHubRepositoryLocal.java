package pl.nalazek.githubsearch.data;

import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

/**
 * @author Daniel Nalazek
 */

public class GitHubRepositoryLocal implements GitHubRepository {

    public GitHubRepositoryLocal(GitHubRepositoryAPIInterface gitHubRepositoryAPIInterface) {
    }

    @Override
    public void requestSearch(String phrase, GitHubRepositorySearchOptions searchOptions) {

    }

    @Override
    public void requestUserDetailedData(UserSearchResult userSearchResult) {

    }
}
