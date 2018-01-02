package pl.nalazek.githubsearch;

import java.util.ArrayList;

import pl.nalazek.githubsearch.JsonObjects.RepoSearchResult;
import pl.nalazek.githubsearch.JsonObjects.UserSearchResult;

/**
 * This builder class performs conversion from a ResponsePackage object to an ArrayList parametrized by SearchResult.
 * Note that an object implementing Collection interface is needed to pass into a CustomListAdapter
 * @author Daniel Nalazek
 * @see SearchResult
 * @see ResponsePackage
 */

public class SearchResultArrayListBuilder{

    private SearchResultArrayListBuilder() {}

    /**
     * Static method to parse a ResponsePackage into an ArrayList
     * @param responsePackage Input ResponsePackage to convert
     * @return ArrayList with SearchResult
     */
    public static ArrayList<SearchResult> build(ResponsePackage responsePackage) {
        ArrayList<SearchResult> searchResultList = new ArrayList<>();
        ArrayList<ResponsePartitioned> responsePartitioned = responsePackage.getResponses();

        for(ResponsePartitioned response : responsePartitioned) {
            ExchangeType type = response.getExchangeType();

            //TODO Starred exclusion
            switch(type) {
                case USER_SEARCH:
                case USER_PAGE:

                    UserSearchResult jsonObject0 = (UserSearchResult) response.getJsonObject();
                    for(UserSearchResult.Item item : jsonObject0.getItems()) {
                        searchResultList.add(new SearchResultUser(item.getLogin(), item.getHtmlUrl(), item.getUrl(), type));
                    }


                    break;
                case REPOS_SEARCH:
                case REPOS_PAGE:
                    RepoSearchResult jsonObject1 = (RepoSearchResult) response.getJsonObject();
                    for(RepoSearchResult.Item item : jsonObject1.getItems()) {
                        searchResultList.add(new SearchResultRepo(item.getName(), item.getHtmlUrl(), type));
                    }
                    break;
                case USER_EXPAND:

                    break;
                case USER_EXPAND_STARS:

                    break;
            }

        }
        return searchResultList;
    }
}
