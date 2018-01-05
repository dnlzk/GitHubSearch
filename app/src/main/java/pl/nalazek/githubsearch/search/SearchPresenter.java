package pl.nalazek.githubsearch.search;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import pl.nalazek.githubsearch.Showable;
import pl.nalazek.githubsearch.data.QueryHistory;
import pl.nalazek.githubsearch.data.QueryObjects.Query;
import pl.nalazek.githubsearch.data.QueryObjects.QueryBuilder;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.QueryTask;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResultObjects.ResultArrayListBuilder;

/**
 * This singleton class is responsible for the whole search process and displaying results on a SearchResultListAdapter
 * @author Daniel Nalazek
 */
public class SearchPresenter implements Observer {

    public enum SearchScope { USERS, REPOSITORIES }

    static final String CHECK_IN_HISTORY = "CheckInHistory";

    private static SearchPresenter instance = new SearchPresenter();
    private static final String LOG_TAG = "SearchPresenter";
    private int pResultsPerPage = 50;
    private Boolean pScopeUsers = true;
    private Boolean pScopeRepos = true;
    private SearchQuery.Sort pSorting = null;
    private SearchQuery.Order pOrdering = null;
    private ResponsePackage actualResponsePackageOnView = null;
    private QueryTask actualProcessingTask = null;
    private HashSet<Integer> pageCache;
    private static QueryHistory queryHistory;


    /**
     * Gets an instance of a single SearchPresenter class
     * @return Single instance of a SearchPresenter class
     */
    public static SearchPresenter getInstance() {
        return instance;
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
     * @see SearchPresenter#setSearchScope(SearchScope...)
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
     * @param checkInHistory FALSE for beginning a new search, TRUE for checking results in search history and if found present them
     * @param showable Showable object to show results on
     */
    public void searchForPhrase(String phrase, boolean checkInHistory, Showable showable) {

        // Check if an other task is pending and cancel it
        if(actualProcessingTask != null) actualProcessingTask.cancel(true);

        showable.showBusy();

        if(checkInHistory && getQueryHistory().isPhraseInHistory(phrase)) {
            publishResultsFromHistory(phrase, showable);
            return;
        }

        // Create a query array
        QueryBuilder queryBuilder = new QueryBuilder();
        Query[] queries = queryBuilder.setOrdering(pOrdering)
                .setSorting(pSorting)
                .setResultsPerPage(pResultsPerPage)
                .setScope(pScopeUsers, pScopeRepos)
                .build(phrase, showable);

        // Create and set as actual new QueryTask
        QueryTask queryTask = new QueryTask();
        actualProcessingTask = queryTask;

        //Execute a query task
        queryTask.execute(queries);
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
        actualResponsePackageOnView = rp;
    }

    /**
     * In most cases used to access Observable QueryHistory object by QueryTask when it finishes it task
     * @return QueryHistory
     */
    public static QueryHistory getQueryHistory() {
        return queryHistory;
    }

    private void publishResultsFromHistory(String phrase,Showable showable) {
        ResponsePackage responsePackage = getQueryHistory().get(phrase);
        showable.showResults(ResultArrayListBuilder.build(responsePackage));
        actualResponsePackageOnView = responsePackage;
    }

    /**
     * Private constructor
     */
    private SearchPresenter() {
        queryHistory = new QueryHistory();
        queryHistory.addObserver(this);
    }
}

