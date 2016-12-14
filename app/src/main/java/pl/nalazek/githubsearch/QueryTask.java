package pl.nalazek.githubsearch;

import android.os.AsyncTask;

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
public class QueryTask extends AsyncTask<Query, Void, Response> {

    @Override
    protected Response doInBackground(Query... queries) {
        return null;
    }
}
