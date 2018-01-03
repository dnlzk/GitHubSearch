package pl.nalazek.githubsearch.QueryObjects;

import android.util.Log;

import pl.nalazek.githubsearch.SearchAgent;
import pl.nalazek.githubsearch.Showable;

/**
 * @author Daniel Nalazek
 */

public class QueryBuilder {

    private QueryBuilder() {}
    private final static String LOG_TAG = "QueryBuilder";
    private SearchQuery.Order ordering = null;
    private SearchQuery.Sort sorting = null;
    private int resultsPerPage = 50;

    public Query[] build(String phrase, Showable showable, boolean scopeUsers, boolean scopeRepos) {
        // Set up search query/queries
        SearchQuery searchQuery1 = null, searchQuery2 = null;
        if(scopeUsers) {
            searchQuery1 = new SearchQuery(phrase, SearchAgent.SearchScope.USERS, showable, resultsPerPage);
            setQueryOptions(searchQuery1);
        }
        if(scopeRepos) {
            searchQuery2 = new SearchQuery(phrase, SearchAgent.SearchScope.REPOSITORIES, showable, resultsPerPage);
            setQueryOptions(searchQuery2);
        }

        SearchQuery[] searchQueryList;

        // Search for users and repositories
        if(scopeUsers && scopeRepos) {
            searchQueryList = new SearchQuery[]{ searchQuery1,searchQuery2 };
        }
        // Search for users only
        else if (scopeUsers) {
            searchQueryList = new SearchQuery[]{ searchQuery1 };
        }
        // Search for repositories only
        else if (scopeRepos) {
            searchQueryList = new SearchQuery[]{ searchQuery2 };
        }
        // Other - not used
        else {
            searchQueryList = new SearchQuery[]{};
            Log.i(LOG_TAG, "No search scope selected");
        }
        return searchQueryList;
    }

    public QueryBuilder setOrdering(SearchQuery.Order ordering) {
        this.ordering = ordering;
        return this;
    }

    public QueryBuilder setSorting(SearchQuery.Sort sorting) {
        this.sorting = sorting;
        return this;
    }

    public QueryBuilder setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
        return this;
    }

    private void setQueryOptions(SearchQuery searchQuery) {

        // If default changed, configure sorting and ordering options
        if(ordering != null)
            searchQuery.setOrdering(ordering);
        if(sorting != null)
            searchQuery.setSorting(sorting);
    }
}
