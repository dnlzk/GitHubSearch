/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.data.QueryObjects;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
import pl.nalazek.githubsearch.data.ResultObjects.InvalidJsonObjectException;

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
        Log.d("QueryTask", STATE_INTERRUPTED);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponsePackage doInBackground(@NonNull Query... queries) {
        assignQueries(queries);
        waitToAvoidQueryFlow();
        if(isCancelled()) return null;
        return makeResponsePackage();
    }

    @Override
    protected void onPostExecute(ResponsePackage responsePackage) {
        super.onPostExecute(responsePackage);
        callback.onResponseReady(this, responsePackage);
    }


    private void assignQueries(Query[] queries) {
        this.queries.addAll(Arrays.asList(queries));
        this.firstQuery = queries[0];
    }

    private synchronized void waitToAvoidQueryFlow() {
        try {
            wait(800);
        } catch (InterruptedException e) {
            cancel(true);
        }
    }


    @Nullable
    private ResponsePackage makeResponsePackage() {

        responsePackage = new ResponsePackage(firstQuery.getQueryType());
        try {
            executeAllQueries();
        }
        catch (CancellationException e) {
            return null;
        }
        catch (UnsuccessfulResponseException e) {
            responsePackage.addErrorMessageAndQuery(e.getMessage(), processingQuery);
        }
        return responsePackage;
    }


    private void executeAllQueries() throws CancellationException, UnsuccessfulResponseException {

        while(!queries.isEmpty()) {
            if(isCancelled()) throw new CancellationException();
            processingQuery = queries.poll();
            executeQuery();
        }
    }


    private void executeQuery() throws UnsuccessfulResponseException {
        URL url = getURLFromProcessingQuery();
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
        catch (IOException | InvalidJsonObjectException e) {
            throw new UnsuccessfulResponseException(e.getMessage());
        }
    }


    private void checkAndProcessResponse(Response response) throws UnsuccessfulResponseException, InvalidJsonObjectException {
        if(response.isSuccessful())
            addCorrectResponseToResponsePackage(response);
        else
            throw new UnsuccessfulResponseException(response.message());
    }


    private void addCorrectResponseToResponsePackage(Response response) throws InvalidJsonObjectException {
        responsePackage.addResponse(response, STATE_SUCCESS, processingQuery.getExchangeType());
    }


    private URL getURLFromProcessingQuery() throws UnsuccessfulResponseException {
        URL url = processingQuery.getURL();

        if(url != null) return url;
        else throw new UnsuccessfulResponseException(STATE_MALFORMED_URL);
    }
}
