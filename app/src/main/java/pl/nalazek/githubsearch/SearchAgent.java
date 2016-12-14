package pl.nalazek.githubsearch;

import android.support.v4.widget.NestedScrollView;

import java.util.Observable;
import java.util.Observer;

/**
 * This singleton class is responsible for the whole search process and displaying results on a NestedScrollView
 * @author Daniel Nalazek
 */
public class SearchAgent implements Observer {

    private static SearchAgent instance = new SearchAgent();

    private static NestedScrollView scrollViewDisplay;
    private static int pResultsPerPage = 50;
    private static Boolean pScopeUsers = false;
    private static Boolean pScopeRepos = false;

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

    @Override
    public void update(Observable observable, Object o) {

    }
}

