package pl.nalazek.githubsearch.data.QueryObjects;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.GitHubRepositoryAPI;
import pl.nalazek.githubsearch.data.GitHubRepositoryAPIInterface;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Daniel Nalazek
 */
@RunWith(MockitoJUnitRunner.class)
public class QueryTaskTest{

    @Mock
    OkHttpClient client;
    @Mock
    Call call;
    @Mock
    Response response;
    @Mock
    ResponseBody body;
    @Mock
    Headers headers;

    @Captor
    ArgumentCaptor<ResponsePackage> responsePackageCaptor;
    @Captor
    ArgumentCaptor<QueryTask> queryTaskCaptor;

    QueryBuilder queryBuilder;
    QueryTask queryTask;
    GitHubRepositoryAPI.QueryTaskCallback callback;
    Query[] queries;


    @Before
    public void beforeConstructor() throws Exception {

        when(body.string()).thenReturn("{\n" +
                "  \"total_count\": 1,\n" +
                "  \"incomplete_results\": false,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"login\": \"dzharii\",\n" +
                "      \"id\": 36020,\n" +
                "      \"avatar_url\": \"https://avatars3.githubusercontent.com/u/36020?v=4\",\n" +
                "      \"gravatar_id\": \"\",\n" +
                "      \"url\": \"https://api.github.com/users/dzharii\",\n" +
                "      \"html_url\": \"https://github.com/dzharii\",\n" +
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
        //when(body.bytes()).thenReturn(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        when(headers.get("Link")).thenReturn(null);
        when(response.body()).thenReturn(body);
        when(response.headers()).thenReturn(headers);
        when(response.isSuccessful()).thenReturn(true);
        when(call.execute()).thenReturn(response);
        when(client.newCall(any(Request.class))).thenReturn(call);

        callback = Mockito.spy(
                new GitHubRepositoryAPIInterface.QueryTaskCallback() {
                    @Override
                    public void onResponseReady(QueryTask queryTask, ResponsePackage responsePackage) {
                        Log.d("QUERYTASK test", "onResultsReady");
                    }
                });

        queryTask = new QueryTask(client, callback);
        queryBuilder = new QueryBuilder();
        queries = queryBuilder.buildSearchQuery().build("test");
    }

    @Test
    public void whenDoInBackgroundAndOnPostExecuteThenCallbackAndVariablesCheck() throws Exception {
        queryTask.onPostExecute(queryTask.doInBackground(queries));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("QueryTask references fault", queryTaskCaptor.getValue(), is(queryTask));
        assertThat("ResponsePackage message fault", responsePackageCaptor.getValue().getMessage(), is("Success"));
        assertThat("ResponsePackage size fault", responsePackageCaptor.getValue().getResponses().size(), is(2));
    }

    @Test
    public void whenDoInBackgroundAndOnPostExecuteAndResponseIsNotSuccesfulThenResponseMessageError() throws Exception {
        when(response.message()).thenReturn("Error");
        when(response.isSuccessful()).thenReturn(false);
        queryTask.onPostExecute(queryTask.doInBackground(queries));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage error message fault", responsePackageCaptor.getValue().getMessage(), is("Error"));
    }

    @Test
    public void whenDoInBackgroundAndCancelledThenResponseMessageInterrupted() throws Exception {
        queryTask.doInBackground(queries);
        queryTask.onCancelled();
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage interrupted message fault", responsePackageCaptor.getValue().getMessage(), is(QueryTask.STATE_INTERRUPTED));
    }

    @Test
    public void whenDoInBackgroundAndConnectionErrorThenResponseMessageConnectionError() throws Exception {
        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenThrow(new UnknownHostException());
        queryTask.onPostExecute(queryTask.doInBackground(queries));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage connection error message fault", responsePackageCaptor.getValue().getMessage(), is(QueryTask.STATE_CONNECTION_ERROR));
    }

    @Test
    public void whenDoInBackgroundAndMalformedURLThenResponseMessageMalformedURL() throws Exception {
        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenThrow(new MalformedURLException());
        queryTask.onPostExecute(queryTask.doInBackground(queries));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage malformed url message fault", responsePackageCaptor.getValue().getMessage(), is(QueryTask.STATE_MALFORMED_URL));
    }

    @Test
    public void whenDoInBackgroundAndIOExceptionThenResponseMessageIOError() throws Exception {
        when(client.newCall(any(Request.class))).thenReturn(call);
        when(call.execute()).thenThrow(new IOException("IO Error"));
        queryTask.onPostExecute(queryTask.doInBackground(queries));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage malformed url message fault", responsePackageCaptor.getValue().getMessage(), is("IO Error"));
    }

    @Test
    public void givenMalformedURLQueryAsNullWhenDoInBackgroundThenResponseMessageMalformedURL() throws Exception {
        Query malformedURLQuery = Mockito.mock(Query.class);
        when(malformedURLQuery.getURL()).thenReturn(null);
        when(malformedURLQuery.getQueryType()).thenReturn(UserSearchResult.TYPE);
        queryTask.onPostExecute(queryTask.doInBackground(malformedURLQuery));
        verify(callback).onResponseReady(queryTaskCaptor.capture(), responsePackageCaptor.capture());
        assertThat("ResponsePackage malformed url message fault", responsePackageCaptor.getValue().getMessage(), is(QueryTask.STATE_MALFORMED_URL));
    }
}