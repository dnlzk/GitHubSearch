package pl.nalazek.githubsearch;

import pl.nalazek.githubsearch.data.GitHubRepositories;
import pl.nalazek.githubsearch.data.GitHubRepository;

/**
 * Enables injection of mock implementations for {@link GitHubRepository} at compile time.
 */
public class Injection {

    public static GitHubRepository provideGitHubRepository() {
        return GitHubRepositories.getInstance(new FakeGitHubRepositoryAPI());
    }
}