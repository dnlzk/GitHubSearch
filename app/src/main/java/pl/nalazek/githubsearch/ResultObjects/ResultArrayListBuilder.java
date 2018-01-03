package pl.nalazek.githubsearch.ResultObjects;

import java.util.ArrayList;

import pl.nalazek.githubsearch.ExchangeType;
import pl.nalazek.githubsearch.JsonObjects.JsonRepoSearchResult;
import pl.nalazek.githubsearch.JsonObjects.JsonUserSearchResult;
import pl.nalazek.githubsearch.ResponsePackage;
import pl.nalazek.githubsearch.ResponsePartitioned;

/**
 * This builder class performs conversion from a ResponsePackage object to an ArrayList parametrized by Result.
 * Note that an object implementing ArrayList<> interface is needed to pass into a Showable object
 * @author Daniel Nalazek
 * @see SearchResult
 * @see ResponsePackage
 */

public class ResultArrayListBuilder {

    private ResultArrayListBuilder() {}

    /**
     * Static method to parse a ResponsePackage into an ArrayList
     * @param responsePackage Input ResponsePackage to convert
     * @return ArrayList with SearchResult
     */
    public static ArrayList<Result> build(ResponsePackage responsePackage) {
        ArrayList<Result> resultList = new ArrayList<>();
        ArrayList<ResponsePartitioned> responsePartitioned = responsePackage.getResponses();

        for(ResponsePartitioned response : responsePartitioned) {
            ExchangeType type = response.getExchangeType();

            //TODO Starred exclusion
            switch(type) {
                case USER_SEARCH:
                case USER_PAGE:

                    JsonUserSearchResult jsonObject0 = (JsonUserSearchResult) response.getJsonObject();
                    for(JsonUserSearchResult.Item item : jsonObject0.getItems()) {
                        resultList.add(new UserSearchResult(item.getLogin(), item.getHtmlUrl(), item.getUrl(), item.getStarredUrl(), type));
                    }


                    break;
                case REPOS_SEARCH:
                case REPOS_PAGE:
                    JsonRepoSearchResult jsonObject1 = (JsonRepoSearchResult) response.getJsonObject();
                    for(JsonRepoSearchResult.Item item : jsonObject1.getItems()) {
                        resultList.add(new RepoSearchResult(item.getName(), item.getHtmlUrl(), type));
                    }
                    break;
                case USER_EXPAND:

                    break;
                case USER_EXPAND_STARS:

                    break;
            }

        }
        return resultList;
    }
}
