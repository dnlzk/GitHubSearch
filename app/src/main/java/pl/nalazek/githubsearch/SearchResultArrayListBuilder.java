package pl.nalazek.githubsearch;

import java.util.ArrayList;

import pl.nalazek.githubsearch.JsonObjects.RepoSearchResult;
import pl.nalazek.githubsearch.JsonObjects.UserSearchResult;

/**
 * @author Daniel Nalazek
 */

public class SearchResultArrayListBuilder{

    private SearchResultArrayListBuilder() {}

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
                        searchResultList.add(new SearchResult(item.getLogin(), item.getHtmlUrl(), type));
                    }


                    break;
                case REPOS_SEARCH:
                case REPOS_PAGE:
                    RepoSearchResult jsonObject1 = (RepoSearchResult) response.getJsonObject();
                    for(RepoSearchResult.Item item : jsonObject1.getItems()) {
                        searchResultList.add(new SearchResult(item.getName(), item.getHtmlUrl(), type));
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
