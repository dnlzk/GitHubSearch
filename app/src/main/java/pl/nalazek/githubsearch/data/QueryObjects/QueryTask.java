package pl.nalazek.githubsearch.data.QueryObjects;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CancellationException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.nalazek.githubsearch.data.GitHubRepositoryAPIInterface;
import pl.nalazek.githubsearch.data.ResponsePackage;

/**
 * This class represents a query task that is executed when:
 * <ul>
 * <li>app user is searching for repos</li>
 * <li>app user is searching for users</li>
 * <li>app user is searching for both users and repos</li>
 * <li>app user is getting a non-cashed page</li>
 * <li>app user is extending data about another user</li>
 * <li>app is making default html calls for images, data, etc.</li>
 * </ul>
 * @author Daniel Nalazek
 */
public class QueryTask extends AsyncTask<Query, Void, ResponsePackage> {

    public final static String STATE_SUCCESS = "Success";
    public final static String STATE_INTERRUPTED = "Task interrupted";
    public final static String STATE_MALFORMED_URL = "Malformed URL";
    public final static String STATE_CONNECTION_ERROR = "Unable to resolve host. Check connection";

    private OkHttpClient client;
    private Queue<Query> queries = new LinkedList<>();
    private Query firstQuery;
    private Query processingQuery;
    private ResponsePackage responsePackage;
    private GitHubRepositoryAPIInterface.QueryTaskCallback callback;

    public QueryTask(OkHttpClient client, GitHubRepositoryAPIInterface.QueryTaskCallback callback) {
        this.callback = callback;
        this.client = client;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.onResponseReady(this, createResponsePackageOnCancelled());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponsePackage doInBackground(@NonNull Query... queries) {
        assignQueries(queries);
        return createResponsePackage();
    }

    @Override
    protected void onPostExecute(ResponsePackage responsePackage) {
        super.onPostExecute(responsePackage);
        callback.onResponseReady(this, responsePackage);
    }

    private ResponsePackage createResponsePackageOnCancelled() {
        try {
            return new ResponsePackage(firstQuery.getQueryType(), STATE_INTERRUPTED);
        }
        catch (IndexOutOfBoundsException e) {
            return new ResponsePackage(null, STATE_INTERRUPTED);
        }
    }

    private void assignQueries(Query[] queries) {
        this.queries.addAll(Arrays.asList(queries));
        this.firstQuery = queries[0];
    }

    @Nullable
    private ResponsePackage createResponsePackage() {

        responsePackage = new ResponsePackage(firstQuery.getQueryType());
        try {
            executeAllQueries();
        }
        catch (CancellationException e) {
            return null;
        }
        catch (UnsuccessfulResponseException e) {
            responsePackage.addMessage(e.getMessage());
        }
        return responsePackage;
    }

    private void executeAllQueries() throws CancellationException, UnsuccessfulResponseException {

        while(!queries.isEmpty()) {
            processingQuery = queries.poll();
            if(isCancelled()) throw new CancellationException();
            executeQuery();
        }
    }

    private void executeQuery() throws UnsuccessfulResponseException {
        URL url = getURLFromQuery(processingQuery);
        Request request = new Request.Builder().url(url).build();
        callRequest(request);
    }

    private void callRequest(Request request) throws UnsuccessfulResponseException {

        try {
            Response response = client.newCall(request).execute();
            checkAndProcessResponse(response);
        }
        catch(MalformedURLException e) {
            throw new UnsuccessfulResponseException(STATE_MALFORMED_URL);
        }
        catch(UnknownHostException e) {
            throw new UnsuccessfulResponseException(STATE_CONNECTION_ERROR);
        }
        catch (IOException e) {
            throw new UnsuccessfulResponseException(e.getMessage());
        }
    }

    private void checkAndProcessResponse(Response response) throws MalformedURLException, UnsuccessfulResponseException {
        if(response.isSuccessful())
            addCorrectResponseToResponsePackage(response);
        else
            throw new UnsuccessfulResponseException(response.message());
    }

    private void addCorrectResponseToResponsePackage(Response response) {
        responsePackage.addResponse(response, processingQuery.getExchangeType());
        responsePackage.addMessage(STATE_SUCCESS);
    }

    private URL getURLFromQuery(Query query) throws UnsuccessfulResponseException {
        try {
            return processingQuery.getURL();
        }
        catch (NullPointerException e) {
            throw new UnsuccessfulResponseException(STATE_MALFORMED_URL);
        }

    }
}
