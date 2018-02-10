package pl.nalazek.githubsearch.data.ResultObjects;

import com.google.common.base.Charsets;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.*;
import pl.nalazek.githubsearch.data.QueryObjects.*;
import pl.nalazek.githubsearch.util.TextToString;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultCreatorTest {

    private final static String JSON_FILE_SEARCH = DataPaths.JSON_FILE_PATH + DataPaths.JSON_FILE_SEARCHUSER;
    private final static String JSON_USER_DETAILED = DataPaths.JSON_FILE_PATH + DataPaths.JSON_FILE_DETAILEDUSER;
    private final static String JSON_FILE_STARRED_PAGE_LAST_LINKHEADER = DataPaths.JSON_FILE_PATH + DataPaths.JSON_FILE_STARRED_PAGE_LAST_LINKHEADER;
    private final static String JSON_FILE_STARRED_PAGE_LAST = DataPaths.JSON_FILE_PATH + DataPaths.JSON_FILE_STARRED_PAGE_LAST;

    @Mock
    ResponsePartitioned responsePartitioned;
    @Mock
    ResponsePackage responsePackage;
    @Mock
    ResponseBody body;
    @Mock
    ResponseBody bodyUserDetailed;
    @Mock
    Headers headers;
    @Mock
    ResponseBody bodyStarsLastPage;
    @Mock
    Headers headersStarsLastPage;

    @Rule
    public ExpectedException exception = ExpectedException.none();


    private ResultCreator creator = new ResultCreator();


    @Before
    public void before() throws Exception {
        setupMocks();
    }



    @Test
    public void givenResponsePartionedExchangeTypePageWhenMakeResultsThenArrayLenghtZero() throws Exception {

        when(responsePartitioned.getExchangeType()).thenReturn(ExchangeType.USER_PAGE);

        Result[] results = creator.makeResults(responsePartitioned);
        assertThat("Result size zero fault", results.length, is(0));
    }


    @Test
    public void givenResponsePartionedExchangeUserSearchWhenMakeResultsThenArrayLenghtEight() throws Exception {

        ResponsePartitioned response = new ResponsePartitioned(headers, body, QueryTask.STATE_SUCCESS, ExchangeType.USER_SEARCH);

        Result[] results = creator.makeResults(response);
        assertThat("Result size fault", results.length, is(8));
    }



    @Test
    public void givenResponsePartionedExchangeUserDetailedWhenMakeResultsThenArrayLenghtOne() throws Exception {

        ResponsePartitioned response = new ResponsePartitioned(headers, bodyUserDetailed, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED);

        Result[] results = creator.makeResults(response);
        assertThat("Result size fault", results.length, is(1));
    }



    @Test
    public void givenResponsePartionedExchangeSearchWhenMakeResultsThenArrayLenghtZero() throws Exception {
        when(responsePartitioned.getExchangeType()).thenReturn(ExchangeType.USER_SEARCH);
        when(responsePartitioned.getJsonObject()).thenReturn(null);

        Result[] results = creator.makeResults(responsePartitioned);
        assertThat("Result size fault", results.length, is(0));
    }


    @Test
    public void givenResponsePackageTypeNewWhenMakeResultsThenListEmpty() throws Exception {

        when(responsePackage.getQueryType()).thenReturn("New type");

        List<? extends Result> results = creator.makeResults(responsePackage);
        assertTrue("Result empty list fault", results.isEmpty());
    }


    @Test
    public void givenEmptyResponsePackageSearchQueryTypeWhenMakeResultsThenListEmpty() throws Exception {

        responsePackage = new ResponsePackage(SearchQuery.TYPE);

        List<? extends Result> results = creator.makeResults(responsePackage);
        assertTrue("Result empty list fault", results.isEmpty());
    }


    @Test
    public void givenEmptyResponsePackageUserDetailedQueryTypeWhenMakeResultsThenListEmpty() throws Exception {

        responsePackage = new ResponsePackage(UserDetailedQuery.TYPE);

        List<? extends Result> results = creator.makeResults(responsePackage);
        assertTrue("Result empty list fault", results.isEmpty());
    }


    @Test
    public void givenInvalidResponsePackageWhenMakeResultsThenEmptyList() throws Exception {

        responsePackage = new ResponsePackage(UserDetailedQuery.TYPE);
        responsePackage.addResponses(Collections.singletonList(
                new ResponsePartitioned(headersStarsLastPage, bodyStarsLastPage, QueryTask.STATE_SUCCESS, ExchangeType.USER_DETAILED_STARS)
                )
        );

        List<? extends Result> results = creator.makeResults(responsePackage);
        assertTrue("Result empty list with exception fault", results.isEmpty());
    }



    private void setupMocks() throws Exception {

        when(body.string()).thenReturn(TextToString.read(JSON_FILE_SEARCH, Charsets.UTF_16));
        when(bodyUserDetailed.string()).thenReturn(TextToString.read(JSON_USER_DETAILED, Charsets.UTF_16));
        when(headers.get("Link")).thenReturn(null);
        when(headersStarsLastPage.get("Link")).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_LAST_LINKHEADER, Charsets.UTF_16));
        when(bodyStarsLastPage.string()).thenReturn(TextToString.read(JSON_FILE_STARRED_PAGE_LAST, Charsets.UTF_16));
    }
}