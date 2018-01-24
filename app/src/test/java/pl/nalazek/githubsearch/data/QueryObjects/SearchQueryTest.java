package pl.nalazek.githubsearch.data.QueryObjects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import pl.nalazek.githubsearch.data.ExchangeType;

@RunWith(value = Parameterized.class)
public class SearchQueryTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {"User", SearchQuery.SearchScope.REPOSITORIES},
                {"/", SearchQuery.SearchScope.REPOSITORIES},
                {"6", SearchQuery.SearchScope.REPOSITORIES},
                {".", SearchQuery.SearchScope.REPOSITORIES},
                {"User", SearchQuery.SearchScope.USERS},
                {"/", SearchQuery.SearchScope.USERS},
                {"6", SearchQuery.SearchScope.USERS},
                {".", SearchQuery.SearchScope.USERS},
        });
    }

    @Parameterized.Parameter(value = 0)
    public static String keyword;

    @Parameterized.Parameter(value = 1)
    public static SearchQuery.SearchScope scope;

    public SearchQuery searchQuery;
    public ExchangeType type;

    @Before
    public void beforeConstructor() throws WhitespaceKeywordException {
        searchQuery = new SearchQuery(keyword, scope);
        setExchangeType(scope);
    }

    @Test
    public void whenGetQueryTypeMethodThenTypeString() {
        assertEquals("Type fault",SearchQuery.TYPE,searchQuery.getQueryType());
    }

    @Test
    public void whenGetQueryKeywordThenKeywordVariable() {
        assertEquals("Keyword fault",keyword,searchQuery.getKeyword());
    }

    @Test
    public void whenGetExchangeTypeThenTypeVariable() {
        assertEquals("Exchange Type fault",type,searchQuery.getExchangeType());
    }

    @Test
    public void whenGetURLThenURL() {
        URL url = createDefaultURLForCompare();
        assertEquals("URL fault",url,searchQuery.getURL());
    }

    @Test
    public void whenSetSortingThenURL() {

        for (SearchQuery.Sort sorting : SearchQuery.Sort.values()) {
            searchQuery.setSorting(sorting);
            URL url = createWitoutDefaultPaginationURLForCompare();
            String forks = getSortingString(sorting);
            String shouldBeURLString = url.toString() + forks + "&per_page=30";
            assertEquals("URL with forks fault", shouldBeURLString, searchQuery.getURL().toString());
        }
    }

    @Test
    public void whenSetOrderThenURL() {

        for (SearchQuery.Order order : SearchQuery.Order.values()) {
            searchQuery.setOrdering(order);
            URL url = createWitoutDefaultPaginationURLForCompare();
            String ordering = getOrderString(order);
            String shouldBeURLString = url.toString() + ordering + "&per_page=30";
            assertEquals("URL with forks fault", shouldBeURLString, searchQuery.getURL().toString());
        }
    }

    @Test
    public void whenSetPaginationInRangeThenURL() {
        int[] inRangeValues = {1,20,64,22};
        for(int perPage : inRangeValues) {
            assertEquals("URL with pagination fault",
                    createPaginationStringAndSetPerPage(perPage),
                    searchQuery.getURL().toString());
        }
    }

    @Test
    public void whenSetPaginationNotInRangeThenDefaultURL() {
        int[] notInRangeValues = {-1,0,101,20000,-200};
        for(int perPage : notInRangeValues) {
            searchQuery.setPerPage(perPage);
            assertEquals("URL with pagination fault",
                    createDefaultURLForCompare(),
                    searchQuery.getURL());
        }
    }

    @Test
    public void whenSetPaginationAndOrderAndSortingThenURL() {

        for (SearchQuery.Order order : SearchQuery.Order.values())
            for (SearchQuery.Sort sorting : SearchQuery.Sort.values()) {

                searchQuery.setSorting(sorting);
                searchQuery.setOrdering(order);
                searchQuery.setPerPage(47);

                String shouldBeURLString = createWitoutDefaultPaginationURLForCompare().toString() +
                        getOrderString(order) +
                        getSortingString(sorting) +
                        "&per_page=47";

                assertEquals("URL with pagination, order and sorting fault",
                        shouldBeURLString,
                        searchQuery.getURL().toString());
            }
    }

    private URL createDefaultURLForCompare() {

        String scopePath = getScopePathAndSetExchangeType(scope);
        try {
            return new URL("https://api.github.com" + scopePath + "?q=" + keyword + "&per_page=30");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private URL createWitoutDefaultPaginationURLForCompare() {

        String scopePath = getScopePathAndSetExchangeType(scope);
        try {
            return new URL("https://api.github.com" + scopePath + "?q=" + keyword);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setExchangeType(SearchQuery.SearchScope scope) {

        switch(scope) {
            case REPOSITORIES:
                type = ExchangeType.REPOS_SEARCH;
                break;
            case USERS:
                type = ExchangeType.USER_SEARCH;
                break;
            default: type = null; break;
        }
    }

    private String getScopePathAndSetExchangeType(SearchQuery.SearchScope scope) {

        String scopePath;
        switch(scope) {
            case REPOSITORIES:
                scopePath = "/search/repositories";
                break;
            case USERS:
                scopePath = "/search/users";
                break;
            default: scopePath = ""; break;
        }
        return scopePath;
    }

    private String getSortingString(SearchQuery.Sort sorting) {

        switch(sorting) {
            case STARS:
                return "&sort=stars";
            case FORKS:
                return "&sort=forks";
            case UPDATED:
                return "&sort=updated";
            case BEST:
            default:
                return "";
        }
    }

    private String getOrderString(SearchQuery.Order ordering) {

        switch (ordering) {
            case ASCENDING:
                return "&order=asc";
            case DESCENDING:
            default:
                return "";
        }
    }

    private String createPaginationStringAndSetPerPage(int perPage) {

        searchQuery.setPerPage(perPage);
        URL url = createWitoutDefaultPaginationURLForCompare();
        return url.toString() + "&per_page=" + String.valueOf(perPage);
    }
}