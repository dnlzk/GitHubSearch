package pl.nalazek.githubsearch.ResultObjects;

import java.util.ArrayList;
import java.util.List;

import pl.nalazek.githubsearch.ExchangeType;
import pl.nalazek.githubsearch.JsonObjects.JsonObject;
import pl.nalazek.githubsearch.JsonObjects.JsonRepoSearchResult;
import pl.nalazek.githubsearch.JsonObjects.JsonUserExpanded;
import pl.nalazek.githubsearch.JsonObjects.JsonUserSearchResult;
import pl.nalazek.githubsearch.JsonObjects.JsonUserStarred;
import pl.nalazek.githubsearch.ResponsePackage;
import pl.nalazek.githubsearch.ResponsePartitioned;

/**
 * This builder class performs conversion from a ResponsePackage object to an ArrayList parametrized by Result.
 * Note that an object implementing ArrayList<> interface is needed to pass into a Showable object
 * @author Daniel Nalazek
 * @see SearchResult
 * @see ResponsePackage
 */
//TODO Refactor
public class ResultArrayListBuilder {

    private static String nameBuffer;
    private static Integer followersBuffer;
    private static ExchangeType typeBuffer;

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
                        resultList.add(new UserSearchResult(item.getLogin(), item.getHtmlUrl(), item.getUrl(), item.getStarredUrl(), item.getAvatarUrl(), type));
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
                    JsonUserExpanded jsonObject2 = (JsonUserExpanded) response.getJsonObject();
                    nameBuffer = jsonObject2.getName();
                    followersBuffer = jsonObject2.getFollowers();
                    typeBuffer = type;
                    break;

                case USER_EXPAND_STARS:
                    List<JsonObject> jsonObject3 = response.getJsonObjectsList();
                    int stars = jsonObject3.size();
                    //TODO null reference
                    resultList.add(new UserDetailedResult(nameBuffer, followersBuffer, stars, null, typeBuffer));
                    nameBuffer = "";
                    followersBuffer = -1;
                    break;
            }
        }
        return resultList;
    }
}
