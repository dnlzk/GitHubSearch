package pl.nalazek.githubsearch;

import android.view.View;

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
    private URL url;
    private String header;
    private View view;
    private String phrase;
    private Boolean isSingle = true;
    private Boolean isLast = true;
    private LinkedList<Response> previousResponses;

}
