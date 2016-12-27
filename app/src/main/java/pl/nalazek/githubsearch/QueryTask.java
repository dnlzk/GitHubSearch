package pl.nalazek.githubsearch;

import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * This class represents a query task that is executed when:
 * <ul>
 * <li>app user is searching for repos</li>
 * <li>app user is searching for users</li>
 * <li>app user is searching for both users and repos</li>
 * <li>app user is switching to a non-cashed page</li>
 * <li>app user is extending data about another user</li>
 * </ul>
 * During the task a query is set on a remote host, when a response is correct, it is parsed.
 * @author Daniel Nalazek
 */
public class QueryTask extends AsyncTask<Query, Void, ResponsePackage> {

    //todo: add onCanecelled(Object) method (running on UI)
    @Override
    protected ResponsePackage doInBackground(Query... queries) {

        OkHttpClient client = new OkHttpClient();

        // execute all queries
        for(Query query : queries) {
            Request request = new Request.Builder().url(query.getURL()).build();
        }

        //todo: check periodically isCancelled() value
        return null;
    }
}
