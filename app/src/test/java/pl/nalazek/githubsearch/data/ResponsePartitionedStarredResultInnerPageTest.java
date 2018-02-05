package pl.nalazek.githubsearch.data;

import com.google.common.base.Charsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.JsonObjects.JsonObject;
import pl.nalazek.githubsearch.data.JsonObjects.JsonUserStarred;
import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.util.TextToString;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static pl.nalazek.githubsearch.data.DataPaths.*;
import static pl.nalazek.githubsearch.data.ExchangeType.USER_DETAILED_STARS;

/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponsePartitionedStarredResultInnerPageTest {


    @Mock
    Headers headers;
    @Mock
    ResponseBody body;

    private ResponsePartitioned response;
    private static final String NEXT_PAGE = "https://api.github.com/user/5085821/starred?page=5";

    @Before
    public void before() throws Exception {
        when(headers.get("Link")).thenReturn(TextToString.read(JSON_FILE_PATH + JSON_FILE_STARRED_PAGE_ONEBUTLAST_LINKHEADER, Charsets.UTF_16));
        when(body.string()).thenReturn(TextToString.read(JSON_FILE_PATH + JSON_FILE_STARRED_PAGE_FIRST, Charsets.UTF_16));
        response = new ResponsePartitioned(headers, body, QueryTask.STATE_SUCCESS, USER_DETAILED_STARS);
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
    public void whenGetJsonListThenNotEmpty() throws Exception {
        assertThat("List fault", response.getJsonObjectsList().isEmpty(), is(false));
    }



    @Test
    public void whenIsFirstPageThenFalse() throws Exception {
        assertThat("First page fault", response.isFirstPage(), is(false));
    }



    @Test
    public void whenIsLastPageThenTrue() throws Exception {
        assertThat("Last page fault", response.isLastPage(), is(false));
    }



    @Test
    public void whenGetExchangeTypeThenTrue() throws Exception {
        assertThat("Exchange type fault", response.getExchangeType(), is(USER_DETAILED_STARS));
    }



    @Test
    public void whenGetLastPageNumberThenMinusOne() throws Exception {
        assertThat("Last page number fault", response.getLastPageNumber(), is(5));
    }



    @Test
    public void whenGetNextPageURLThenNull() throws Exception {
        assertThat("Next page url fault", response.getNextPageURL(), is(NEXT_PAGE));
    }


    @Test
    public void whenGetInstanceOfElementsThenUserJSONUserStarred() throws Exception {
        for(JsonObject jsonResult : response.getJsonObjectsList())
            assertTrue("Class type fault", jsonResult instanceof JsonUserStarred);
    }



    @Test
    public void whenGetJsonObjectThenNull() throws Exception {
        assertTrue("Json object null fault", response.getJsonObject() == null);
    }

}