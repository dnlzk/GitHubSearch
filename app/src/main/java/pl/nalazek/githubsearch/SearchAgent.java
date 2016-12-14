package pl.nalazek.githubsearch;

import android.support.v4.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * This singleton class is responsible for the whole search process and displaying results on a NestedScrollView
 * @author Daniel Nalazek
 */
public class SearchAgent implements Observer {

    enum SearchScope { USERS, REPOSITORIES }

    private static SearchAgent instance = new SearchAgent();

    private static NestedScrollView scrollViewDisplay;
    private int pResultsPerPage = 50;
    private Boolean pScopeUsers = false;
    private Boolean pScopeRepos = false;
    private QueryTask actualOnView = null;
    private QueryTask actualProcessing = null;
    private HashSet<Integer> pageCache;
    private QueryHistory queryHistory;




    /**
     * Gets an instance of a single SearchAgent class
     * @param scrollViewDisplay The scroll view used to show the results of a search query
     * @return Single instance of a SearchAgent class
     */
    public static SearchAgent getInstance(NestedScrollView scrollViewDisplay) {
        SearchAgent.scrollViewDisplay = scrollViewDisplay;
        return instance;
    }

    /**
     * Private constructor
     */
    private SearchAgent() {
    }

    /**
     * Use to set the scope of searching
     * @param searchScope pass the SearchScope enums to define the scope or none to disable searching
     */
    public void setSearchScope(SearchScope... searchScope) {
        pScopeUsers = false;
        pScopeRepos = false;
        for(SearchScope scope : searchScope) {
            switch(scope){
                case USERS: pScopeUsers = true; break;
                case REPOSITORIES: pScopeRepos = true; break;
            }
        }
    }


    /**
     * Called when a new pair is put into QueryHistory
     * @param observable instance of QueryHistory
     * @param o not used
     */
    @Override
    public void update(Observable observable, Object o) {
        QueryHistory qHistory = (QueryHistory) observable;
        QueryTask qTask = (QueryTask) o;
    }
}

