package pl.nalazek.githubsearch.data;

import com.google.common.base.Charsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.JsonObjects.JsonUserSearchResult;
import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.util.TextToString;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import static pl.nalazek.githubsearch.data.DataPaths.*;
import static pl.nalazek.githubsearch.data.ExchangeType.*;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponsePartitionedUserSearchSingletonTest {


    @Mock
    Headers headers;
    @Mock
    ResponseBody body;

    private ResponsePartitioned response;


    @Before
    public void before() throws Exception {
        when(headers.get("Link")).thenReturn(null);
        when(body.string()).thenReturn(TextToString.read(JSON_FILE_PATH + JSON_FILE_SEARCHUSER, Charsets.UTF_16));
        response = new ResponsePartitioned(headers, body, QueryTask.STATE_SUCCESS, USER_SEARCH);
    }


    @Test
    public void whenGetMessageThenSuccess() throws Exception {
        assertThat("Message fault", response.getMessage(), is(QueryTask.STATE_SUCCESS));
    }



    @Test
    public void whenGetAvatarThenNull() throws Exception {
        assertTrue("Avatar fault", response.getAvatar() == null );
    }



    @Test
    public void whenGetJsonListThenEmpty() throws Exception {
        assertTrue("List fault", response.getJsonObjectsList().isEmpty());
    }



    @Test
    public void whenIsFirstPageThenTrue() throws Exception {
        assertThat("First page fault", response.isFirstPage(), is(true));
    }



    @Test
    public void whenIsLastPageThenTrue() throws Exception {
        assertThat("Last page fault", response.isLastPage(), is(true));
    }



    @Test
    public void whenGetExchangeTypeThenTrue() throws Exception {
        assertThat("Exchange type fault", response.getExchangeType(), is(USER_SEARCH));
    }



    @Test
    public void whenGetLastPageNumberThenOne() throws Exception {
        assertThat("Last page number fault", response.getLastPageNumber(), is(1));
    }



    @Test
    public void whenGetNextPageURLThenNull() throws Exception {
        assertTrue("Next page url fault", response.getNextPageURL() == null);
    }


    @Test
    public void whenGetInstanceThenUserJSONSearchResult() throws Exception {
        assertTrue("Class type fault", response.getJsonObject() instanceof JsonUserSearchResult);
    }
}