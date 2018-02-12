package pl.nalazek.githubsearch.data;

import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;

/**
 * This data class is used to pass options needed to perform a search.
 * The only way to get an instance of it is using the attached builder. Note that fields of
 * the class are final, so when you want to change the search options, you have to build a new object.
 * @author Daniel Nalazek
 */
public class GitHubRepositorySearchOptions {

    private final static int RESULTS_PER_PAGE_DEFAULT = 50;

    private final SearchQuery.Order order;
    private final SearchQuery.Sort sorting;
    private final boolean scopeUsers;
    private final boolean scopeRepos;
    private final short resultsPerPage;
    private final boolean searchInHistory;


    private GitHubRepositorySearchOptions(SearchQuery.Order order, SearchQuery.Sort sorting, boolean scopeUsers, boolean scopeRepos, short resultsPerPage, boolean searchInHistory) {
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


    public short getResultsPerPage() {
        return resultsPerPage;
    }


    public boolean isForcedSearchInHistory() {
        return searchInHistory;
    }


    public static GitHubRepositorySearchOptionsBuilder build() {
        return new GitHubRepositorySearchOptionsBuilder();
    }


    /**
     * To get a {@link GitHubRepositorySearchOptions} object you have to call always and at least
     * {@link #isForcedSearchInHistory()} method.
     * When calling only this, all options are set to defaults:
     * <li>1.{@link SearchQuery.Order#ASCENDING}</li>
     * <li>2.{@link SearchQuery.Sort#BEST}</li>
     * <li>3.Search scope - users and repositories</li>
     * <li>4.Results per page - 50</li>
     */
    public static class GitHubRepositorySearchOptionsBuilder {

        private SearchQuery.Order order = SearchQuery.Order.ASCENDING;
        private SearchQuery.Sort sorting = SearchQuery.Sort.BEST;
        private boolean scopeUsers = true;
        private boolean scopeRepos = true;
        private short resultsPerPage = RESULTS_PER_PAGE_DEFAULT;


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


        /**
         * @param resultsPerPage Acceptable values: 1 to 100
         * @throws IllegalArgumentException when value is out of range
         */
        public GitHubRepositorySearchOptionsBuilder setResultsPerPage(short resultsPerPage) throws
                                                                        IllegalArgumentException {

            if(resultsPerPage < 1 || resultsPerPage > 100)
                throw new IllegalArgumentException("Acceptable value is between 1-100");

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
