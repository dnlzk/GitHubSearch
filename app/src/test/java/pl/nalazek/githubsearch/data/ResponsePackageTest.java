package pl.nalazek.githubsearch.data;

import com.google.common.base.Charsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InvalidObjectException;
import java.util.Collections;

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.QueryObjects.Query;
import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery;
import pl.nalazek.githubsearch.data.ResultObjects.InvalidJsonObjectException;
import pl.nalazek.githubsearch.util.TextToString;

import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import static pl.nalazek.githubsearch.data.DataPaths.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponsePackageTest {

    private ResponsePackage responsePackage;
    private String type = UserDetailedQuery.TYPE;
    private static final String error = "error";
    private ExchangeType eType = ExchangeType.USER_DETAILED_STARS;

    @Mock
    private Response okResponse;
    @Mock
    private ResponseBody body;
    @Mock
    private Headers headers;
    @Mock
    private Query query;



    @Before
    public void before() throws Exception {
        setupOkResponse();
        responsePackage = new ResponsePackage(UserDetailedQuery.TYPE);
        responsePackage.addResponse(okResponse, QueryTask.STATE_SUCCESS, eType);
    }



    @Test
    public void whenGetQueryTypeThenType() throws Exception {
        assertThat("Query type fault", responsePackage.getQueryType(), is(type));
    }



    @Test
    public void whenGetLastResponseExchangeTypeThen() throws Exception {
        assertTrue("Last response exchange type fault",
                responsePackage.getLastResponseExchangeType() == eType);
    }



    @Test
    public void whenGetResponsesThenIsNotEmpty() throws Exception {
        assertFalse("Responses array size fault", responsePackage.getResponses().isEmpty());
    }



    @Test
    public void whenGetErrorMapThenIsEmpty() throws Exception {
        assertTrue("Error Map size fault", responsePackage.getErrorMessagesMap().isEmpty());
    }



    @Test
    public void whenGetLastResponseWithTypeThenNotNull() throws Exception {
        assertTrue("Last response with type fault",
                responsePackage.getLastResponseWithExchangeType(eType) != null);
    }



    @Test
    public void whenGetAllResponsesWithTypeThenSizeOne() throws Exception {
            assertTrue("All responses with type fault",
                    responsePackage.getAllResponsesWith(eType).size() == 1);
    }



    @Test
    public void whenHasNoErrorMessagesThenTrue() throws Exception {
            assertThat("Has no errors fault", responsePackage.hasNoErrorMessages(), is(true));
    }



    @Test
    public void whenHasNoResponsesThenFalse() throws Exception {
        assertThat("Has no responses fault", responsePackage.hasNoResponses(), is(false));
    }



    @Test
    public void whenIsEmptyThenFalse() throws Exception {
        assertThat("Is empty fault", responsePackage.isEmpty(), is(false));
    }



    @Test
    public void whenCombineResoponsePackagesThenNotEmpty() throws Exception {

        ResponsePackage responsePackage2 = new ResponsePackage(type);
        responsePackage.combineResponsePackages(responsePackage2);
        assertFalse("Combine fault", responsePackage.isEmpty());
    }



    @Test
    public void givenAddMessageWhenGetErrorMapThenIsNotEmpty() throws Exception {
        responsePackage.addErrorMessageAndQuery(error, query);
        assertFalse("Message map fault", responsePackage.getErrorMessagesMap().isEmpty());
    }



    @Test
    public void givenAddMessageWhenGetErrorMapKeyThenValue() throws Exception {
        responsePackage.addErrorMessageAndQuery(error, query);
        assertThat("Message map key/value fault", responsePackage.getErrorMessagesMap().get(error), is(query));
    }


    @Test
    public void givenAddMessageWhenHasNoErrorsThenFalse() throws Exception {
        responsePackage.addErrorMessageAndQuery(error, query);
        assertThat("Message map has no errors fault", responsePackage.hasNoErrorMessages(), is(false));
    }


    @Test
    public void givenResponsePartitionedWhenAddResponseThenSizeIsTwo() throws Exception {
        ResponsePartitioned response = getResponsePartitioned();
        responsePackage.addResponse(response);
        assertThat("Package size fault", responsePackage.getResponses().size(), is(2));
    }


    @Test
    public void givenResponsePartitionedListWhenAddResponsesThenSizeIsTwo() throws Exception {
        ResponsePartitioned response = getResponsePartitioned();
        responsePackage.addResponses(Collections.singletonList(response));
        assertThat("Package size fault", responsePackage.getResponses().size(), is(2));
    }



    @Test(expected = InvalidJsonObjectException.class)
    public void whenAddWrongResponseThenException() throws Exception {
        setupNotParsableOkResponse();
        responsePackage.addResponse(okResponse, QueryTask.STATE_SUCCESS, eType);
    }


    @Test(expected = InvalidObjectException.class)
    public void givenOtherQueryTypePackageWhenCombineResoponsePackagesThenException() throws Exception {
        ResponsePackage responsePackage2 = new ResponsePackage(SearchQuery.TYPE);
        responsePackage.combineResponsePackages(responsePackage2);
    }


    private void setupOkResponse() throws Exception {

        when(okResponse.body())
                .thenReturn(body);
        when(okResponse.headers())
                .thenReturn(headers);
        when(body.string())
                .thenReturn(TextToString.read(JSON_FILE_PATH + JSON_FILE_STARRED, Charsets.UTF_16));
        when(headers.get("Link"))
                .thenReturn(null);
    }



    private void setupNotParsableOkResponse() throws Exception {
        when(okResponse.body())
                .thenReturn(body);
        when(okResponse.headers())
                .thenReturn(headers);
        when(body.string())
                .thenReturn(TextToString.read(JSON_FILE_PATH + JSON_FILE_STARRED));
    }


    private ResponsePartitioned getResponsePartitioned() throws InvalidJsonObjectException {
        return new ResponsePartitioned(headers, body, QueryTask.STATE_SUCCESS,
                eType);
    }

}