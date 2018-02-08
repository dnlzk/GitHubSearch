package pl.nalazek.githubsearch.data;

import org.junit.Before;
import org.junit.Test;

import java.io.InvalidObjectException;

import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Daniel Nalazek
 */
public class ResponsePackageEmptyTest {

    private ResponsePackage responsePackage;
    private String type = SearchQuery.TYPE;



    @Before
    public void before() throws Exception {
        responsePackage = new ResponsePackage(SearchQuery.TYPE);
    }



    @Test
    public void whenGetQueryTypeThenType() throws Exception {
        assertThat("Query type fault", responsePackage.getQueryType(), is(type));
    }



    @Test
    public void whenGetLastResponseExchangeTypeThenNull() throws Exception {
        assertTrue("Last response exchange type fault",
                responsePackage.getLastResponseExchangeType() == null);
    }



    @Test
    public void whenGetResponsesThenIsEmpty() throws Exception {
        assertTrue("Responses array size fault", responsePackage.getResponses().isEmpty());
    }



    @Test
    public void whenGetErrorMapThenIsEmpty() throws Exception {
        assertTrue("Error Map size fault", responsePackage.getErrorMessagesMap().isEmpty());
    }



    @Test
    public void whenGetLastResponseWithTypeThenNull() throws Exception {
        for(ExchangeType type : ExchangeType.values())
            assertTrue("Last response with type fault",
                    responsePackage.getLastResponseWithExchangeType(type) == null);
    }



    @Test
    public void whenGetAllResponsesWithTypeThenNull() throws Exception {
        for(ExchangeType type : ExchangeType.values())
            assertTrue("All responses with type fault",
                    responsePackage.getAllResponsesWith(type) == null);
    }



    @Test
    public void whenHasNoErrorMessagesThenTrue() throws Exception {
            assertThat("Has no errors fault", responsePackage.hasNoErrorMessages(), is(true));
    }



    @Test
    public void whenHasNoResponsesThenTrue() throws Exception {
        assertThat("Has no responses fault", responsePackage.hasNoResponses(), is(true));
    }



    @Test
    public void whenIsEmptyThenTrue() throws Exception {
        assertThat("Is empty fault", responsePackage.isEmpty(), is(true));
    }



    @Test
    public void whenCombineResoponsePackagesThenEmpty() throws Exception {

        ResponsePackage responsePackage2 = new ResponsePackage(type);
        responsePackage.combineResponsePackages(responsePackage2);
        assertTrue("Combine fault", responsePackage.isEmpty());
    }



    @Test(expected = InvalidObjectException.class)
    public void givenOtherQueryTypePackageWhenCombineResoponsePackagesThenException() throws Exception {
        ResponsePackage responsePackage2 = new ResponsePackage(UserDetailedQuery.TYPE);
        responsePackage.combineResponsePackages(responsePackage2);
    }



    @Test(expected = NullPointerException.class)
    public void whenNewPackageWithNullParameterThenException() throws Exception {
        responsePackage = new ResponsePackage(null);
    }



    @Test(expected = NullPointerException.class)
    public void whenAddResponseWithNullsThenException() throws Exception {
        responsePackage.addResponse(null, null, null);
    }



    @Test(expected = NullPointerException.class)
    public void whenAddResponseWithNullThenException() throws Exception {
        responsePackage.addResponse(null);
    }
}