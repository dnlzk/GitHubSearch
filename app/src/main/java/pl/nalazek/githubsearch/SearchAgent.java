package pl.nalazek.githubsearch;

import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

/**
 * This singleton class is responsible for the whole search process and displaying results on a CustomListAdapter
 * @author Daniel Nalazek
 */
public class SearchAgent implements Observer {

    enum SearchScope { USERS, REPOSITORIES }

    private static SearchAgent instance = new SearchAgent();
    private static final String LOG_TAG = "SearchAgent";
    private int pResultsPerPage = 50;
    private Boolean pScopeUsers = true;
    private Boolean pScopeRepos = true;
    private Query.Sort pSorting = null;
    private Query.Order pOrdering = null;
    private QueryTask actualOnView = null;
    private QueryTask actualProcessingTask = null;
    private HashSet<Integer> pageCache;
    private static QueryHistory queryHistory;


    /**
     * Gets an instance of a single SearchAgent class
     * @return Single instance of a SearchAgent class
     */
    public static SearchAgent getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private SearchAgent() {
        queryHistory = new QueryHistory();
        queryHistory.addObserver(this);
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
     * Use to set the number of results viewed. Note that if you set the scope both to USERS
     * and REPOSITORIES the number of results viewed will be doubled.
     * Example: passing number = 30
     * if the scope is set only to USERS there will be 30 results on page,
     * if the scope is set to both USERS and REPOSITORIES there will be 60 results on page
     * @param number the number of results
     * @see SearchAgent#setSearchScope(SearchScope...)
     */
    public void setResultsNumber(int number){
        pResultsPerPage = number;
    }

    /**
     * Use to get the the number of results shown
     * @return number of results
     */
    public int getResultsNumber() {
        return pResultsPerPage;
    }

    /**
     * Use to start a search.
     * This method creates a QueryTask, if an earlier QueryTask is still being processed then it is canceled.
     * @param phrase The string to search for
     * @param customListAdapter Adapter to pass the results to
     */
    public void searchForPhrase(String phrase, CustomListAdapter customListAdapter, ProgressBarManager progressBarManager) {

        // Check if an other task is pending and cancel it
        if(actualProcessingTask != null) actualProcessingTask.cancel(true);

        customListAdapter.clear();
        progressBarManager.setProgressBarVisible();


        // Create a query list
        Query[] queryList = getQueryArray(phrase, customListAdapter);

        // Create and set as actual new QueryTask
        QueryTask queryTask = new QueryTask(phrase);
        actualProcessingTask = queryTask;

        //Execute a query task
        queryTask.execute(queryList);
    }

    /**
     * Use to start a search for suggestions
     * This method creates a QueryTask, if an earlier QueryTask is still being processed then it is canceled.
     * @param suggestion The suggestion to search for
     */
    public void searchForSuggestion(String suggestion) {

        // Check if an other task is pending and cancel it
        if(actualProcessingTask != null) actualProcessingTask.cancel(true);
        //todo
    }

    /**
     * This method sets sorting and ordering options for the passed query
     * @param query The query which the options will be set to
     */
    private void setQueryOptions(Query query) {

        // If default changed, configure sorting and ordering options
        if(pOrdering != null)
            query.setOrdering(pOrdering);
        if(pSorting != null)
            query.setSorting(pSorting);
    }

    /**
     * This method creates a Query array where its quantity depends on the selected search scopes.
     * @param phrase The string to search for
     * @param customListAdapter The adapter to pass the results to
     * @return A query array. The quantity of elements depends on the quantity of selected search scopes.
     * E.g. when the search scope is selected only to {@link SearchScope#REPOSITORIES}, only one-element array will be returned.
     * If the search scope is selected to both {@link SearchScope#REPOSITORIES} and {@link SearchScope#USERS}, a two-element array
     * be returned
     */
    private Query[] getQueryArray(String phrase, CustomListAdapter customListAdapter) {

        // Set up search query/queries
        Query searchQuery1 = null, searchQuery2 = null;
        if(pScopeUsers) {
            searchQuery1 = new Query(phrase, SearchScope.USERS, customListAdapter, pResultsPerPage);
            setQueryOptions(searchQuery1);
        }
        if(pScopeRepos) {
            searchQuery2 = new Query(phrase, SearchScope.REPOSITORIES, customListAdapter, pResultsPerPage);
            setQueryOptions(searchQuery2);
        }

        Query[] queryList;

        // Search for users and repositories
        if(pScopeUsers && pScopeRepos) {
            queryList = new Query[]{ searchQuery1,searchQuery2 };
        }
        // Search for users only
        else if (pScopeUsers) {
            queryList = new Query[]{ searchQuery1 };
        }
        // Search for repositories only
        else if (pScopeRepos) {
            queryList = new Query[]{ searchQuery2 };
        }
        // Other - not used
        else {
            queryList = new Query[]{};
            Log.i(LOG_TAG, "No search scope selected");
        }
        return queryList;
    }




    /**
     * Called when a new pair is put into QueryHistory
     * @param observable instance of QueryHistory
     * @param o QueryTask instance which accessed the Observable object, here QueryHistory
     */
    @Override
    public void update(Observable observable, Object o) {
        QueryHistory qHistory = (QueryHistory) observable;
        QueryTask qTask = (QueryTask) o;
        ResponsePackage rp = qHistory.get(qTask);

        // task ended, set value to null
        if(actualProcessingTask == qTask) actualProcessingTask = null;

        // check if successful and set QueryTask as the one which results are on view
        if(rp.getMessage() != null) actualOnView = qTask;
    }

    /**
     * In most cases used to access Observable QueryHistory object by QueryTask when it finishes it task
     * @return QueryHistory
     */
    static QueryHistory getQueryHistory() {
        return queryHistory;
    }
}

