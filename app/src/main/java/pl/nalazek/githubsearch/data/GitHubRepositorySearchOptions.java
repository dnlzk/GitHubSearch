package pl.nalazek.githubsearch.data;

import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;

/**
 * This data class is used to keep options needed to perform a search.
 * The only way get an instance of it is using the attached builder. Note that the fields of the class are final,
 * so when you want to change search options, you have to build a new object.
 * @author Daniel Nalazek
 */
public class GitHubRepositorySearchOptions {

    private final static int RESULTS_PER_PAGE_DEFAULT = 50;

    private final SearchQuery.Order order;
    private final SearchQuery.Sort sorting;
    private final boolean scopeUsers;
    private final boolean scopeRepos;
    private final int resultsPerPage;
    private final boolean searchInHistory;


    private GitHubRepositorySearchOptions(SearchQuery.Order order, SearchQuery.Sort sorting, boolean scopeUsers, boolean scopeRepos, int resultsPerPage, boolean searchInHistory) {
        this.order = order;
        this.sorting = sorting;
        this.scopeUsers = scopeUsers;
        this.scopeRepos = scopeRepos;
        this.resultsPerPage = resultsPerPage;
        this.searchInHistory = searchInHistory;
    }

    public SearchQuery.Order getOrder() {
        return order;
    }

    public SearchQuery.Sort getSorting() {
        return sorting;
    }

    public boolean isScopeUsers() {
        return scopeUsers;
    }

    public boolean isScopeRepos() {
        return scopeRepos;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public boolean isSearchInHistory() {
        return searchInHistory;
    }

    public static GitHubRepositorySearchOptionsBuilder build() {
        return new GitHubRepositorySearchOptionsBuilder();
    }

    public static class GitHubRepositorySearchOptionsBuilder {

        private SearchQuery.Order order = SearchQuery.Order.ASCENDING;
        private SearchQuery.Sort sorting = SearchQuery.Sort.BEST;
        private boolean scopeUsers = true;
        private boolean scopeRepos = true;
        private int resultsPerPage = RESULTS_PER_PAGE_DEFAULT;


        public GitHubRepositorySearchOptionsBuilder setOrdering(SearchQuery.Order order) {
            this.order = order;
            return this;
        }

        public GitHubRepositorySearchOptionsBuilder setSorting(SearchQuery.Sort sorting) {
            this.sorting = sorting;
            return this;
        }

        public GitHubRepositorySearchOptionsBuilder setScope(boolean scopeUsers, boolean scopeRepos) {
            this.scopeUsers = scopeUsers;
            this.scopeRepos = scopeRepos;
            return this;
        }

        public GitHubRepositorySearchOptionsBuilder setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
            return this;
        }

        public GitHubRepositorySearchOptions forceSearchInHistory(boolean forceSearchInHistory) {
            return new GitHubRepositorySearchOptions(order,
                                                     sorting,
                                                     scopeUsers,
                                                     scopeRepos,
                                                     resultsPerPage,
                                                     forceSearchInHistory
                                                    );
        }
    }
}
