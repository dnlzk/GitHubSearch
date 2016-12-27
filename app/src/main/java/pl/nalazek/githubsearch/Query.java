package pl.nalazek.githubsearch;

import android.util.Log;
import android.view.View;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * This class is representing a Query.
 * @author Daniel Nalazek
 */
public class Query {
    final static String GITHUB_HOST_URL = "https://api.github.com";
    final static String SEARCH_FOR_REPOS_URL = "/search/repositories";
    final static String SEARCH_FOR_USERS_URL = "/search/users";
    private static final String LOG_TAG = "Query Class";
    private URL url;
    private String header;
    private View view;
    private String phrase;
    private Boolean isSingle = true;
    private Boolean isLast = true;
    private LinkedList<Response> previousResponses;


    /**
     * Simple constructor. Creates a query with no specific parameters.
     * @param phrase The string to be searched
     * @param scope The scope of the search
     * @param view The view that will be passed to QueryTask to show the results
     * @see pl.nalazek.githubsearch.SearchAgent.SearchScope
     */
    public Query(String phrase, SearchAgent.SearchScope scope, View view) {

        this.phrase = phrase;

        // set the proper scope url string
        String searchURL;
        switch(scope) {
            case USERS: searchURL = SEARCH_FOR_USERS_URL;
                break;
            case REPOSITORIES: searchURL = SEARCH_FOR_REPOS_URL;
                break;
            default: searchURL = "";
        }

        // construct the query url
        try{
            url = new URL(GITHUB_HOST_URL + searchURL + "?q=" + phrase);
        }
        catch(MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}
