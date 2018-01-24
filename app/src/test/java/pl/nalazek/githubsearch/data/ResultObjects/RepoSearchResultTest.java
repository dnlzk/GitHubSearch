package pl.nalazek.githubsearch.data.ResultObjects;

import org.junit.Before;
import org.junit.Test;

import pl.nalazek.githubsearch.data.ExchangeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class RepoSearchResultTest {

    RepoSearchResult repoSearchResult;
    String name = "John";
    String description = "evaluate description";
    String exampleRepoUrl = "http://u.pl";

    @Before
    public void beforeConstructor() {
        repoSearchResult = new RepoSearchResult(name, description, exampleRepoUrl);
    }

    @Test
    public void whenGetTitleThenName() throws Exception {
        assertThat("Title fault", repoSearchResult.getTitle(), is(name));
    }

    @Test
    public void whenGetDescriptionThenDescription() throws Exception {
        assertThat("Description fault", repoSearchResult.getDescription(), is(description));
    }

    @Test
    public void whenGetExchangeThenUserSearch() throws Exception {
        assertThat("Exchange type fault", repoSearchResult.getExchangeType(), is(ExchangeType.REPOS_SEARCH));
    }

    @Test
    public void whenGetTypeThenUserSearchType() throws Exception {
        assertThat("Type fault", repoSearchResult.getResultType(), is(RepoSearchResult.TYPE));
    }

    @Test
    public void whenGetURLThenUrl() throws Exception {
        assertThat("Type fault", repoSearchResult.getRepoURL(), is(exampleRepoUrl));
    }

    @Test
    public void givenNewUserSearchResultSameValuesWhenEqualsThenEqual() throws Exception {
        RepoSearchResult newRepoSearchResult = new RepoSearchResult(name, description, exampleRepoUrl);
        assertTrue(repoSearchResult.equals(newRepoSearchResult));
    }

    @Test
    public void givenNewUserSearchResultSameValuesWhenHashCodeEqualityCheckThenEqual() throws Exception {
        RepoSearchResult newRepoSearchResult = new RepoSearchResult(name, description, exampleRepoUrl);
        assertTrue(repoSearchResult.hashCode() == newRepoSearchResult.hashCode());
    }

    @Test
    public void givenNewUserSearchResultDifferentValuesWhenEqualsThenNotEqual() throws Exception {
        RepoSearchResult newRepoSearchResult = new RepoSearchResult(name, "other", exampleRepoUrl);
        assertFalse(repoSearchResult.equals(newRepoSearchResult));
    }

    @Test
    public void givenNewUserSearchResultDifferentValuesWhenHashCodeEqualityCheckThenNptEqual() throws Exception {
        RepoSearchResult newRepoSearchResult = new RepoSearchResult(name, description, "other");
        assertFalse(repoSearchResult.hashCode() == newRepoSearchResult.hashCode());
    }

}
