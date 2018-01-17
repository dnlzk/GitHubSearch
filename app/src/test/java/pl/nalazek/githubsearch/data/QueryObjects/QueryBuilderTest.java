package pl.nalazek.githubsearch.data.QueryObjects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URL;
import java.util.Arrays;

import pl.nalazek.githubsearch.data.ExchangeType;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(value = Parameterized.class)
public class QueryBuilderTest {

    @Parameterized.Parameters
    public static Iterable<Object> testData() {
        return Arrays.asList(new Object[]{
                "User", "Jun", "000,l", " cdvwa"
        });
    }

    @Parameterized.Parameter(value = 0)
    public static String keyword;

    QueryBuilder queryBuilder;
    URL[] examplaryURLs;

    @Before
    public void beforeConstructor() throws Exception {
        queryBuilder = new QueryBuilder();
        examplaryURLs = new URL[]{new URL("http://a.com"), new URL("http://a.com"), new URL("http://a.com")};
    }

    @Test
    public void whenBuildingQueriesWithKeywordOnlyThenArraySizeIsTwo() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().build(keyword);
        assertThat("Query[] size fault", queries.length, is(2));
    }

    @Test
    public void whenBuildingQueriesWithKeywordOnlyThenArrayContainsSearchQueryTypeObjects() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().build(keyword);
        for (Query query : queries) {
            assertThat("Query class fault", query, is(instanceOf(SearchQuery.class)));
        }
    }

    @Test
    public void whenBuildingQueriesWithKeywordOnlyThenQueriesURLsContainKeyword() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().build(keyword);
        for (Query query : queries) {
            assertThat("Query keyword url fault", query.getURL().toString(), containsString("q=" + keyword));
        }
    }

    @Test
    public void whenBuildingQueriesWithSortingThenQueriesURLsContainsAdditionallySortingString() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setSorting(SearchQuery.Sort.UPDATED).build(keyword);
        for (Query query : queries) {
            assertThat("Query keyword url fault", query.getURL().toString(), containsString("&sort=updated"));
        }
    }

    @Test
    public void whenBuildingQueriesWithOrderingThenQueriesURLsContainsAdditionallyOrderingString() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setOrdering(SearchQuery.Order.ASCENDING).build(keyword);
        for (Query query : queries) {
            assertThat("Query keyword url fault", query.getURL().toString(), containsString("&order=asc"));
        }
    }

    @Test
    public void whenBuildingQueriesWithPaginationThenQueriesURLsContainsAdditionallyPaginationString() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setResultsPerPage(45).build(keyword);
        for (Query query : queries) {
            assertThat("Query keyword url fault", query.getURL().toString(), containsString("&per_page=45"));
        }
    }

    @Test
    public void whenBuildingQueriesWithScopeUsersAndReposThenArraySizeIsTwo() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setScope(true, true).build(keyword);
        assertThat("Query[] size fault", queries.length, is(2));
    }

    @Test
    public void whenBuildingQueriesWithScopeUsersThenArraySizeIsOne() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setScope(true, false).build(keyword);
        assertThat("Query[] size fault", queries.length, is(1));
    }

    @Test
    public void whenBuildingQueriesWithScopeReposThenArraySizeIsOne() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setScope(false, true).build(keyword);
        assertThat("Query[] size fault", queries.length, is(1));
    }

    @Test
    public void whenBuildingQueriesWithScopeUsersAndRepos() throws Exception {
        Query[] queries = queryBuilder.buildSearchQuery().setResultsPerPage(45).build(keyword);
        assertThat("Query[] size fault", queries.length, is(2));
    }

    @Test
    public void whenBuildingQueriesWithThreeURLThenArraySizeIsThree() throws Exception {
        Query[] queries = queryBuilder.buildUserDetailedQuerry().build(examplaryURLs[0],
                examplaryURLs[1],
                examplaryURLs[2]);
        assertThat("Query[] size fault", queries.length, is(3));
    }

    @Test
    public void whenBuildingQueriesWithThreeURLThenArrayContainsUserDetailedQueryTypeObjects() throws Exception {
        Query[] queries = queryBuilder.buildUserDetailedQuerry().build(examplaryURLs[0],
                examplaryURLs[1],
                examplaryURLs[2]);
        for (Query query : queries) {
            assertThat("Query class fault", query, is(instanceOf(UserDetailedQuery.class)));
        }
    }

    @Test
    public void whenBuildingQueriesWithOneURLAndMultipleExchangeTypesThenQueryTypeIsUserDetailedQuery() throws Exception {
        for (ExchangeType value : ExchangeType.values()) {
            Query query = queryBuilder.buildUserDetailedQuerry().build(examplaryURLs[0], value);
            assertThat("Query class fault", query, is(instanceOf(UserDetailedQuery.class)));
        }
    }
}