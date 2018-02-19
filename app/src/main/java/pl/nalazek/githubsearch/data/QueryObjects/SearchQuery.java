/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.data.QueryObjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This class is representing a search query
 */
public class SearchQuery extends Query {

    public static final String TYPE = "SearchQuery";

    public enum Sort { STARS, FORKS, UPDATED, BEST }
    public enum Order { ASCENDING, DESCENDING }
    public enum SearchScope { USERS, REPOSITORIES }

    private static final String GITHUB_HOST_URL = "https://api.github.com";
    private static final String SEARCH_FOR_REPOS_PATH = "/search/repositories";
    private static final String SEARCH_FOR_USERS_PATH = "/search/users";
    private static final int DEFAULT_PER_PAGE = 30;

    private String keyword;
    private SearchScope scope;
    private int perPage = DEFAULT_PER_PAGE;
    private Sort sort = Sort.BEST;
    private Order order = Order.DESCENDING;

    /**
     * Default constructor. Creates a query with parameters. If not set, default is sorting by best match and descending order.
     * @param keyword The search keyword. It should contain at least one alphanumeric character.
     * @param scope The scope of the search.
     * @see SearchScope
     */
    SearchQuery(@NonNull String keyword, @NonNull SearchScope scope) throws WhitespaceKeywordException {

        if(areOnlyWhitespaces(keyword)) throw new WhitespaceKeywordException();

        this.keyword = keyword;
        this.scope = scope;

        buildURL();
    }

    /**
     * Sets the sorting parameter. By default is set {@link Sort#BEST}
     * @param sort Sorting parameter {@link Sort}.
     */
    void setSorting(@NonNull Sort sort) {
        this.sort = sort;
        buildURL();
    }

    /**
     * Sets the ordering parameter. By default ascending order is set.
     * @param order ordering type from enum Order
     */
    void setOrdering(@NonNull Order order) {
        this.order = order;
        buildURL();
    }

    /**
     * Sets the amount of results per page
     * @param perPage Amount of results on one page. Should be positive integer > 0. According to GitHub API doc - max is 100.
     *                When input exceeds the acceptable range, it will be set to default = 30.
     */
    void setPerPage(int perPage) {
        this.perPage = perPage;
        buildURL();
    }


    String getKeyword() { return keyword; }

    @Override
    public String getQueryType() {
        return TYPE;
    }

    private boolean areOnlyWhitespaces(String keyword) {
        return !keyword.matches(".*\\S.*");
    }

    private void buildURL() {
        url = new URLBuilder().build();
    }

    private class URLBuilder {

        private String scopePathString;
        private String sortFieldString;
        private String orderFieldString;
        private String pageFieldString;
        //To be implemented in future
        private String searchQualifiers = "";

        @Nullable
        URL build() {

            if(isPerPageValueOutOfRange())
                setPerPageValueToDefault();
            setPerPageURLString();
            setScopeURLStringAndExchangeType();
            setSortURLString();
            setOrderURLString();

            return constructURL();
        }

        private boolean isPerPageValueOutOfRange() {
            return !(perPage > 0 && perPage <=100);
        }

        private void setPerPageValueToDefault() {
            perPage = DEFAULT_PER_PAGE;
        }

        private void setPerPageURLString() {
            pageFieldString = "&per_page=" + Integer.toString(perPage);
        }

        private void setScopeURLStringAndExchangeType() {

            switch(scope) {
                case USERS:
                    scopePathString = SEARCH_FOR_USERS_PATH;
                    type = ExchangeType.USER_SEARCH;
                    break;
                case REPOSITORIES:
                    scopePathString = SEARCH_FOR_REPOS_PATH;
                    type = ExchangeType.REPOS_SEARCH;
                    break;
            }
        }

        private void setSortURLString() {
            switch(sort) {
                case STARS:
                    sortFieldString = "&sort=stars";
                    break;
                case FORKS:
                    sortFieldString = "&sort=forks";
                    break;
                case UPDATED:
                    sortFieldString = "&sort=updated";
                    break;
                case BEST:
                    sortFieldString = "";
            }
        }

        private void setOrderURLString() {

            switch(order) {
                case ASCENDING:
                    orderFieldString = "&order=asc";
                    break;
                case DESCENDING:
                    orderFieldString = "";
            }
        }

        @Nullable
        private URL constructURL() {

            try{
                return new URL(GITHUB_HOST_URL +
                        scopePathString +
                        "?q=" +
                        keyword +
                        searchQualifiers +
                        orderFieldString +
                        sortFieldString +
                        pageFieldString);
            }
            catch(MalformedURLException e) {
                return null;
            }
        }
    }
}
