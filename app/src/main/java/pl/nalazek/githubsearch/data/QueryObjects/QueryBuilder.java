package pl.nalazek.githubsearch.data.QueryObjects;

import android.support.annotation.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This builder class is intended to create SearchQueries and UserDetailedQuerries
 * @author Daniel Nalazek
 */

public class QueryBuilder {

    public SearchQuerryBuilder buildSearchQuery() {
        return new SearchQuerryBuilder();
    }

    public UserDetailedQuerryBuilder buildUserDetailedQuerry() {
        return new UserDetailedQuerryBuilder();
    }

    public class SearchQuerryBuilder {

        private SearchQuery.Order ordering = null;
        private SearchQuery.Sort sorting = null;
        private boolean scopeUsers = true;
        private boolean scopeRepos = true;
        private int resultsPerPage = -1;

        public Query[] build(String keyword) throws WhitespaceKeywordException {
            List<SearchQuery> searchQueryList = createQueriesList(keyword);
            return searchQueryList.toArray(new Query[0]);
        }

        public SearchQuerryBuilder setOrdering(SearchQuery.Order ordering) {
            this.ordering = ordering;
            return this;
        }

        public SearchQuerryBuilder setSorting(SearchQuery.Sort sorting) {
            this.sorting = sorting;
            return this;
        }

        public SearchQuerryBuilder setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
            return this;
        }

        public SearchQuerryBuilder setScope(boolean scopeUsers, boolean scopeRepos) {
            this.scopeUsers = scopeUsers;
            this.scopeRepos = scopeRepos;
            return this;
        }

        private List<SearchQuery> createQueriesList(String keyword) throws WhitespaceKeywordException{

            List<SearchQuery> searchQueryList = new ArrayList<>();

            if(scopeUsers) {
                searchQueryList.add(newSearchQuery(keyword, SearchQuery.SearchScope.USERS));
            }
            if(scopeRepos) {
                searchQueryList.add(newSearchQuery(keyword, SearchQuery.SearchScope.REPOSITORIES));
            }

            return searchQueryList;
        }

        private SearchQuery newSearchQuery(String keyword, SearchQuery.SearchScope scope) throws WhitespaceKeywordException{
            SearchQuery searchQuery = new SearchQuery(keyword, scope);
            configureSearchQuery(searchQuery);
            return searchQuery;
        }

        private void configureSearchQuery(SearchQuery searchQuery) {

            if(ordering != null)
                searchQuery.setOrdering(ordering);
            if(sorting != null)
                searchQuery.setSorting(sorting);
            if(resultsPerPage > -1)
                searchQuery.setPerPage(resultsPerPage);
        }
    }

    public class UserDetailedQuerryBuilder{

        public Query[] build(URL userURL, URL userStarredURL, @Nullable URL avatarURL) {
            UserDetailedQuery userExpandedQuery = new UserDetailedQuery(userURL, ExchangeType.USER_DETAILED);
            UserDetailedQuery userStarredQuery = new UserDetailedQuery(userStarredURL, ExchangeType.USER_DETAILED_STARS);
            UserDetailedQuery userAvatarQuery = new UserDetailedQuery(avatarURL, ExchangeType.USER_DETAILED_AVATAR);
            return new Query[] {userExpandedQuery, userStarredQuery, userAvatarQuery};
        }

        public Query build(URL url, ExchangeType type) {
            return new UserDetailedQuery(url, type);
        }
    }

}
