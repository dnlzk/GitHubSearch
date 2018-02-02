package pl.nalazek.githubsearch.data;

import org.junit.Before;
import org.junit.Test;

import okhttp3.OkHttpClient;

import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class GitHubRepositoriesTest {

    private GitHubRepository repository;

    @Before
    public void before() throws Exception {
        repository = GitHubRepositories.getInstance(new GitHubRepositoryAPI(new OkHttpClient()));
    }


    @Test
    public void givenRepositoryWhenGetInstanceThenSameReference() throws Exception {
        GitHubRepository repository2 = GitHubRepositories.getInstance(new GitHubRepositoryAPI(new OkHttpClient()));
        assertTrue("Repo reference equality error", repository.equals(repository2));
    }
}