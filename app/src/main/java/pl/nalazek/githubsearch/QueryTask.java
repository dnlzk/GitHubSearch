package pl.nalazek.githubsearch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.nalazek.githubsearch.ResultObjects.ResultArrayListBuilder;

/**
 * This class represents a query task that is executed when:
 * <ul>
 * <li>app user is searching for repos</li>
 * <li>app user is searching for users</li>
 * <li>app user is searching for both users and repos</li>
 * <li>app user is switching to a non-cashed page</li>
 * <li>app user is extending data about another user</li>
 * </ul>
 * During the task a query is set on a remote host, when the response is correct, it is parsed.
 * @author Daniel Nalazek
 */
public class QueryTask extends AsyncTask<SearchQuery, Void, ResponsePackage> {

    final static String LOG_TAG = "QueryTask Class";
    private Showable showable;
    private String phrase = null;

    public QueryTask() {}

    @Override
    protected void onCancelled() {
        super.onCancelled();
        SearchAgent.getQueryHistory().put(this,new ResponsePackage("Task interrupted"));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponsePackage doInBackground(SearchQuery... queries) {

        OkHttpClient client = new OkHttpClient();
        ResponsePackage responsePackage = new ResponsePackage();
        Response response;

        // execute all queries
        for(SearchQuery searchQuery : queries) {
            if(phrase == null) phrase = searchQuery.getPhrase();
            if(isCancelled()) return null;
            showable = searchQuery.getShowable();
            Request request = new Request.Builder().url(searchQuery.getURL()).build();
            try {
                response = client.newCall(request).execute();
                if(response.isSuccessful()) {
                    responsePackage.addResponse(response, searchQuery.getType());
                    responsePackage.addMessage("Success");
                }
                else responsePackage.addMessage(response.message());
            }
            catch(UnknownHostException e) {
                Log.e(LOG_TAG, e.getMessage());
                responsePackage.addMessage("Unable to resolve host. Check connection");
                return responsePackage;
            }
            catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
                responsePackage.addMessage(e.getMessage());
                return responsePackage;
            }
        }
        return responsePackage;
    }

    @Override
    protected void onPostExecute(ResponsePackage responsePackage) {
        super.onPostExecute(responsePackage);
        SearchAgent.getQueryHistory().put(this,responsePackage);
        showable.showResults(ResultArrayListBuilder.build(responsePackage));
    }


    /**
     * Returns the phrase for the query task
     * @return phrase
     */
    public String getPhraseString() { return phrase; }
}
