package pl.nalazek.githubsearch.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class GitHubRepositorySearchOptionsTest {

    private GitHubRepositorySearchOptions.GitHubRepositorySearchOptionsBuilder builder;

    @Rule
    public ExpectedException exception;


    @Before
    public void before() {
        builder = new GitHubRepositorySearchOptions.GitHubRepositorySearchOptionsBuilder();
        exception = ExpectedException.none();
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenIsForcedTrue() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("Force search fault", options.isForcedSearchInHistory(), is(true));
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenOrderIsAscending() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("Ordering fault", options.getOrder(), is(SearchQuery.Order.ASCENDING));
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenSortIsBest() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("Sorting fault", options.getSorting(), is(SearchQuery.Sort.BEST));
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenResultsPerPageIsFifty() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("Results per page fault", options.getResultsPerPage(), is((short)50));
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenScopeReposIsTrue() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("Repos scope fault", options.isScopeRepos(), is(true));
    }



    @Test
    public void givenForceSearchInHistoryWhenBuildThenScopeUsersIsTrue() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(true);
        assertThat("User scope fault", options.isScopeUsers(), is(true));
    }



    @Test
    public void givenForceSearchFalseInHistoryWhenBuildThenIsForcedFalse() throws Exception {
        GitHubRepositorySearchOptions options = builder.forceSearchInHistory(false);
        assertThat("Force search fault", options.isForcedSearchInHistory(), is(false));
    }



    @Test
    public void givenAlsoOrderingWhenBuildThenOrderingValue() throws Exception {
        for(SearchQuery.Order order : SearchQuery.Order.values()) {
            GitHubRepositorySearchOptions options = builder.setOrdering(order)
                    .forceSearchInHistory(false);
            assertThat("Ordering fault", options.getOrder(), is(order));
        }
    }


    @Test
    public void givenAlsoSortingWhenBuildThenSortingValue() throws Exception {
        for(SearchQuery.Sort sort : SearchQuery.Sort.values()) {
            GitHubRepositorySearchOptions options = builder.setSorting(sort)
                    .forceSearchInHistory(false);
            assertThat("Sorting fault", options.getSorting(), is(sort));
        }
    }



    @Test
    public void givenAlsoPerPageWhenBuildThenPerPage() throws Exception {
        short perPage = 26;
        GitHubRepositorySearchOptions options = builder.setResultsPerPage(perPage)
                .forceSearchInHistory(false);
        assertThat("Per page fault", options.getResultsPerPage(), is(perPage));
    }



    @Test
    public void givenAlsoInvalidPerPageWhenBuildThenException() throws Exception {
        short[] perPageArray = {-2, 0, 101, 200};
        for(short perPage : perPageArray) {
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("Acceptable value is between 1-100");
            builder.setResultsPerPage(perPage).forceSearchInHistory(false);
        }
    }


    @Test
    public void givenAlsoScopeReposWhenBuildThenScopeReposValues() throws Exception {
        boolean[][] states = {
                {true, true},
                {true, false},
                {false, true},
                {false, false},
        };

        for(boolean[] state : states) {
            GitHubRepositorySearchOptions options = builder.setScope(state[0], state[1])
                    .forceSearchInHistory(false);
            assertThat("Scope users fault", options.isScopeUsers(), is(state[0]));
            assertThat("Scope repos fault", options.isScopeRepos(), is(state[1]));
        }
    }
}