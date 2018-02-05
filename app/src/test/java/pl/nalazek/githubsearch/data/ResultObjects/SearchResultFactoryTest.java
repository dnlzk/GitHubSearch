package pl.nalazek.githubsearch.data.ResultObjects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.ExchangeType;
import pl.nalazek.githubsearch.data.JsonObjects.JsonUserDetailed;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResponsePartitioned;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchResultFactoryTest {

    @Mock
    ResponseBody body;
    @Mock
    Headers headers;

    String title = "dzharii";
    String url = "https://github.com/dzharii";
    SearchResultFactory searchResultFactory;

    @Before
    public void before() throws Exception {
        searchResultFactory = SearchResultFactory.getInstance();

        when(body.string()).thenReturn("{\n" +
                "  \"total_count\": 1,\n" +
                "  \"incomplete_results\": false,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"login\": "+ title +",\n" +
                "      \"id\": 36020,\n" +
                "      \"avatar_url\": \"https://avatars3.githubusercontent.com/u/36020?v=4\",\n" +
                "      \"gravatar_id\": \"\",\n" +
                "      \"url\": \"https://api.github.com/users/dzharii\",\n" +
                "      \"html_url\": \""+ url +"\",\n" +
                "      \"followers_url\": \"https://api.github.com/users/dzharii/followers\",\n" +
                "      \"following_url\": \"https://api.github.com/users/dzharii/following{/other_user}\",\n" +
                "      \"gists_url\": \"https://api.github.com/users/dzharii/gists{/gist_id}\",\n" +
                "      \"starred_url\": \"https://api.github.com/users/dzharii/starred{/owner}{/repo}\",\n" +
                "      \"subscriptions_url\": \"https://api.github.com/users/dzharii/subscriptions\",\n" +
                "      \"organizations_url\": \"https://api.github.com/users/dzharii/orgs\",\n" +
                "      \"repos_url\": \"https://api.github.com/users/dzharii/repos\",\n" +
                "      \"events_url\": \"https://api.github.com/users/dzharii/events{/privacy}\",\n" +
                "      \"received_events_url\": \"https://api.github.com/users/dzharii/received_events\",\n" +
                "      \"type\": \"User\",\n" +
                "      \"site_admin\": false,\n" +
                "      \"score\": 52.629246\n" +
                "    }\n" +
                "  ]\n" +
                "}\n");
    }

    @Test
    public void whenGetInstanceThenSameReference() throws Exception {
        SearchResultFactory searchResultFactoryForCompare = SearchResultFactory.getInstance();
        assertThat("Static reference fault", searchResultFactory, is(searchResultFactoryForCompare));
    }

    @Test
    public void givenResponsePackageWithoutResponsesWhenMakeResultsThenEmptyList() throws Exception {
        ResponsePackage responsePackage = new ResponsePackage(SearchQuery.TYPE);
        List<SearchResult> list = searchResultFactory.makeResults(responsePackage);
        assertTrue("Results empty list fault", list.isEmpty());
    }

    @Test
    public void givenResponsePartitionedUserSearchWhenMakeResultsThenArrayLenghtOne() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, body, "Success", ExchangeType.USER_SEARCH);
        SearchResult[] searchResults = searchResultFactory.makeResults(responsePartitioned);
        assertThat("Results array lenght fault", searchResults.length, is(1));
    }

    @Test
    public void givenResponsePartitionedRepoSearchWhenMakeResultsThenArrayLenghtOne() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, body, "Success", ExchangeType.REPOS_SEARCH);
        SearchResult[] searchResults = searchResultFactory.makeResults(responsePartitioned);
        assertThat("Results array lenght fault", searchResults.length, is(1));
    }

    @Test
    public void givenResponsePartitionedWhenMakeResultsAndGetTitleThenTitle() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, body, "Success", ExchangeType.USER_SEARCH);
        SearchResult[] searchResults = searchResultFactory.makeResults(responsePartitioned);
        assertThat("Results title fault", searchResults[0].getTitle(), is(title));
    }

    @Test
    public void givenResponsePartitionedWhenMakeResultsAndGetDescriptionThenUrl() throws Exception {
        ResponsePartitioned responsePartitioned = new ResponsePartitioned(headers, body, "Success", ExchangeType.USER_SEARCH);
        SearchResult[] searchResults = searchResultFactory.makeResults(responsePartitioned);
        assertThat("Results description fault", searchResults[0].getDescription(), is(url));
    }

    @Test(expected = InvalidJsonObjectException.class)
    public void givenResponsePartitionedWhenMakeResultsThenInvalidJSONObjectException() throws Exception {
        ResponsePartitioned responsePartitioned = mock(ResponsePartitioned.class);
        when(responsePartitioned.getJsonObject()).thenReturn(new JsonUserDetailed());
        searchResultFactory.makeResults(responsePartitioned);
    }

    @Test
    public void givenResponsePackageWithMessagesWhenMakeResultsThenSizeIsOne() throws Exception {
        ResponsePackage responsePackage = createResponsePackageMock();
        List<SearchResult> list = searchResultFactory.makeResults(responsePackage);
        assertThat("List size fault", list.size(), is(1));
    }

    private ResponsePackage createResponsePackageMock() throws Exception {
        ResponsePackage responsePackage = mock(ResponsePackage.class);
        ArrayList<ResponsePartitioned> list = new ArrayList<> (
                Collections.singletonList(
                        new ResponsePartitioned(headers, body, "Success", ExchangeType.USER_SEARCH)
                ));
        when(responsePackage.getResponses()).thenReturn(list);

        return responsePackage;
    }
}