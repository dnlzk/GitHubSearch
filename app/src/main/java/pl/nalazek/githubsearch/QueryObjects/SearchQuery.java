package pl.nalazek.githubsearch.QueryObjects;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import pl.nalazek.githubsearch.ExchangeType;
import pl.nalazek.githubsearch.SearchAgent;
import pl.nalazek.githubsearch.Showable;

/**
 * This class is representing a SearchQuery.
 * @author Daniel Nalazek
 */
public class SearchQuery extends Query {
    final static String GITHUB_HOST_URL = "https://api.github.com";
    final static String SEARCH_FOR_REPOS_URL = "/search/repositories";
    final static String SEARCH_FOR_USERS_URL = "/search/users";

    private static final String LOG_TAG = "SearchQuery Class";
    private String phrase;
    private Sort sort = Sort.BEST;
    private Order order = Order.DESCENDING;
    private Integer resultsPerPage;


    public enum Sort { STARS, FORKS, UPDATED, BEST }
    public enum Order { ASCENDING, DESCENDING }

    /**
     * Default constructor. Creates a query with parameters. If not set, default is sorting by best match and descending order.
     * @param phrase The string to be searched
     * @param scope The scope of the search
     * @param showable The Showable object where the results will be passed to
     * @see pl.nalazek.githubsearch.SearchAgent.SearchScope
     */
    public SearchQuery(String phrase, SearchAgent.SearchScope scope, Showable showable, Integer resultsPerPage) {

        this.phrase = phrase;
        this.resultsPerPage = resultsPerPage;
        this.showable = showable;

        String scopeURLString;
        String sortURLString;
        String orderURLString;
        // set the proper page url-string
        String pageURLString = "&per_page=" + resultsPerPage.toString();


        // set the proper scope url-string and query type
        switch(scope) {
            case USERS: scopeURLString = SEARCH_FOR_USERS_URL;
                        eType = ExchangeType.USER_SEARCH;
                break;
            case REPOSITORIES:  scopeURLString = SEARCH_FOR_REPOS_URL;
                                eType = ExchangeType.REPOS_SEARCH;
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

    /**
     * Sets the sorting parameter. By default best match sorting is set.
     * @param sort sorting type form enum Sort
     */
    public void setSorting(Sort sort) {
        this.sort = sort;
    }

    /**
     * Sets the ordering parameter. By default ascending order is set.
     * @param order ordering type from enum Order
     */
    public void setOrdering(Order order) { this.order = order; }

    /**
     * Calling this method will set the query as multiple.
     * This method will be used if the search has two or more scopes (now only REPOSITORIES and USERS scopes are available) and
     * there are created two or more SearchQuery/QueryTask instances.
     * The parameter indicates the query is the last one.
     * @param isLastQuery true if the multiple query is the last one, false if otherwise
     * @param  previousRepsonses previous responses list or null when the multiple query is the first one
     */

    /**
     * Gets the phrase of the query
     * @return Phrase
     */
    public String getPhrase() { return phrase; }


}
