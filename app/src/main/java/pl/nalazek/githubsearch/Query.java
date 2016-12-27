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
    private Sort sort = Sort.BEST;
    private Order order = Order.DESCENDING;
    private Integer resultsPerPage;

    public enum Sort { STARS, FORKS, UPDATED, BEST }
    public enum Order { ASCENDING, DESCENDING }

    /**
     * Default constructor. Creates a query with parameters. If not set, default is sorting by best match and descending order.
     * @param phrase The string to be searched
     * @param scope The scope of the search
     * @param view The view that will be passed to QueryTask to show the results
     * @see pl.nalazek.githubsearch.SearchAgent.SearchScope
     */
    public Query(String phrase, SearchAgent.SearchScope scope, View view, Integer resultsPerPage) {

        this.phrase = phrase;
        this.resultsPerPage = resultsPerPage;
        this.view = view;

        String scopeURLString;
        String sortURLString;
        String orderURLString;
        // set the proper page url-string
        String pageURLString = "&per_page=" + resultsPerPage.toString();


        // set the proper scope url-string
        switch(scope) {
            case USERS: scopeURLString = SEARCH_FOR_USERS_URL;
                break;
            case REPOSITORIES: scopeURLString = SEARCH_FOR_REPOS_URL;
                break;
            default: scopeURLString = "";
        }

        // set the proper sort url-string
        switch(sort) {
            case STARS:
                sortURLString = "&sort=stars";
                break;
            case FORKS:
                sortURLString = "&sort=forks";
                break;
            case UPDATED:
                sortURLString = "&sort=updated";
                break;
            case BEST:
            default:
                sortURLString = "";
        }

        // set the proper order URL-string
        switch(order) {
            case ASCENDING:
                orderURLString = "&order=asc";
                break;
            case DESCENDING:
            default:
                orderURLString = "";
        }

        // construct the query url
        try{
            url = new URL(GITHUB_HOST_URL + scopeURLString + "?q=" + phrase + pageURLString + orderURLString + sortURLString);
        }
        catch(MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


}
