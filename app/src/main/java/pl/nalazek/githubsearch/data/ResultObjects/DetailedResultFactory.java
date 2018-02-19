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

package pl.nalazek.githubsearch.data.ResultObjects;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.nalazek.githubsearch.data.ExchangeType;
import pl.nalazek.githubsearch.data.JsonObjects.JsonObject;
import pl.nalazek.githubsearch.data.JsonObjects.JsonUserDetailed;
import pl.nalazek.githubsearch.data.JsonObjects.JsonUserStarred;
import pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResponsePartitioned;

/**
 * Produces DetailedResult Objects. Do not use directly to avoid
 * throwing {@link InvalidJsonObjectException} and {@link InvalidObjectException}.
 * Instead use {@link ResultCreator}.
 */

class DetailedResultFactory implements ResultFactory {

    private List<DetailedResult> results = new ArrayList<>();

    private Bitmap bitmap;
    private int pagesCount = 0;
    private int starsOnFirstPage = -1;
    private int starsOnLastPage = -1;


    /**
     * @return DetailedResult array for {@link ExchangeType#USER_DETAILED_STARS},
     * singleton array for {@link ExchangeType#USER_DETAILED}
     * and a singleton array with empty result for {@link ExchangeType#USER_DETAILED_AVATAR}.
     *
     * Note that {@link ExchangeType#USER_DETAILED} will show
     * results with stars count equal to 0 and avatar image equal to null.
     * To have a full detailed user result ({@link UserDetailedResult}), pass a correct response package
     * to {@link #makeResults(ResponsePackage)}.
     *
     * @throws InvalidJsonObjectException Thrown in two cases:
     * when passed {@link ResponsePartitioned} object in {@link ResponsePackage}
     * contains impermissible {@link ExchangeType},
     * while counting stars input object {@link ResponsePartitioned} ain't or missing first or last page.
     *
     * @see #makeResults(ResponsePackage)
     */
    @Override
    public DetailedResult[] makeResults(ResponsePartitioned responsePartitioned) throws InvalidJsonObjectException {

        return createConcreteResults(responsePartitioned);
    }



    /**
     * This works correctly if passed response package contains {@link ResponsePartitioned} objects from:
     * <li>1. {@link ExchangeType#USER_DETAILED} </li>
     * <li>2. {@link ExchangeType#USER_DETAILED_AVATAR} </li>
     * <li>3. {@link ExchangeType#USER_DETAILED_STARS} - first page </li>
     * <li>4. {@link ExchangeType#USER_DETAILED_STARS} - last page (optionally) </li>
     * These elements are required to produce a correct {@link UserDetailedResult}
     *
     * @throws InvalidJsonObjectException Thrown in two cases:
     * when passed {@link ResponsePartitioned} object in {@link ResponsePackage} contains impermissible {@link ExchangeType},
     * while counting stars input object {@link ResponsePartitioned} ain't or missing first or last page.
     *
     * @throws InvalidObjectException Thrown when {@link ResponsePackage} is not {@link UserDetailedQuery#TYPE}
     */
    @Override
    public List<DetailedResult> makeResults(ResponsePackage responsePackage) throws InvalidJsonObjectException, InvalidObjectException {

        if(responsePackage.isEmpty()) return results;

        switch(responsePackage.getQueryType()) {

            case UserDetailedQuery.TYPE:
                createUserDetailedResult(responsePackage);
                return results;

            default: throw new InvalidObjectException("Invalid query type for response package. Excepected type UserDetailedQuery.");
        }
    }



    private DetailedResult[] createConcreteResults(ResponsePartitioned responsePartitioned)
            throws InvalidJsonObjectException {

        ExchangeType type = responsePartitioned.getExchangeType();

        switch (type) {
            case USER_DETAILED:
                return getUserDetailedResults(responsePartitioned);

            case USER_DETAILED_STARS:
                return getUserStarredResults(responsePartitioned);

            case USER_DETAILED_AVATAR:
                return setBitmap(responsePartitioned);

            default:
                throw new InvalidJsonObjectException("Expected USER_DETAILED, USER_DETAILED_STARS " +
                        "or USER_DETAILED_AVATAR exchange type! Got: " + type.toString());
        }
    }



    private void createUserDetailedResult(ResponsePackage responsePackage) throws InvalidJsonObjectException {

        try {
            getStarsFromResponsePackage(responsePackage);
            getAvatarFromResponsePackage(responsePackage);
            getDetailedDataFromResponsePackage(responsePackage);
        }
        catch(NullPointerException e) {
            throw new InvalidJsonObjectException("Not found all needed ResponsePartitioned objects");
        }
    }



    private DetailedResult[] getUserDetailedResults(ResponsePartitioned responsePartitioned) {
        JsonObject jsonObject = responsePartitioned.getJsonObject();
        return createUserDetailedResultsSingletonArray( (JsonUserDetailed) jsonObject);
    }



    private DetailedResult[] getUserStarredResults(ResponsePartitioned responsePartitioned) {
        List<JsonUserStarred> jsonObjectsList = responsePartitioned.getJsonObjectsList();
        return createUserStarredResults(jsonObjectsList);
    }



    private DetailedResult[] setBitmap(ResponsePartitioned responsePartitioned) {
        bitmap = responsePartitioned.getAvatar();
        return singletonArrayWithEmptyResult();
    }



    private void getStarsFromResponsePackage(ResponsePackage responsePackage)
                                            throws InvalidJsonObjectException, NullPointerException {

        for(ResponsePartitioned responsePartitioned :
                responsePackage.getAllResponsesWith(ExchangeType.USER_DETAILED_STARS)) {
            DetailedResult[] starsResults = makeResults(responsePartitioned);

            if(responsePartitioned.isFirstPage() && responsePartitioned.isLastPage()) {
                countStarsOnFirstPage(starsResults);
                countStarsOnLastPage(starsResults);
                setPagesCount(responsePartitioned.getLastPageNumber());
            }

            else if(responsePartitioned.isFirstPage()) {
                countStarsOnFirstPage(starsResults);
                setPagesCount(responsePartitioned.getLastPageNumber());
            }

            else if(responsePartitioned.isLastPage())
                countStarsOnLastPage(starsResults);

            else throw new InvalidJsonObjectException(
                    "Expected first and/or last page of USER_DETAILED_STARS exchange.");
        }
        checkIfAreAllNeededPages();
    }


    private void getAvatarFromResponsePackage(ResponsePackage responsePackage)
                                        throws InvalidJsonObjectException, NullPointerException {

        ResponsePartitioned avatarResponse =
                responsePackage.getAllResponsesWith(ExchangeType.USER_DETAILED_AVATAR).get(0);
        makeResults(avatarResponse);
    }



    private void getDetailedDataFromResponsePackage(ResponsePackage responsePackage)
                                        throws InvalidJsonObjectException, NullPointerException {

        ResponsePartitioned userResponse =
                responsePackage.getAllResponsesWith(ExchangeType.USER_DETAILED).get(0);
        results.addAll(Arrays.asList(makeResults(userResponse)));
    }



    private DetailedResult[] createUserDetailedResultsSingletonArray(JsonUserDetailed jsonObject) {

        DetailedResult[] results = new DetailedResult[1];
        results[0] = new UserDetailedResult(
                jsonObject.getName(),
                jsonObject.getLogin(),
                jsonObject.getFollowers(),
                countStars(),
                bitmap,
                jsonObject.getId());
        return results;
    }



    private DetailedResult[] createUserStarredResults(List<JsonUserStarred> jsonObjectsList) {

        int size = jsonObjectsList.size();
        int i = 0;
        DetailedResult[] results = new DetailedResult[size];

        for(JsonUserStarred jsonUserStarred : jsonObjectsList) {
            results[i++] = createStarredResult(jsonUserStarred);
        }
        return results;
    }



    private DetailedResult[] singletonArrayWithEmptyResult() {

        return new DetailedResult[] {
                new DetailedResult(ExchangeType.USER_DETAILED_AVATAR, 0) {
                    @Override
                    public String getResultType() {
                        return TYPE;
                    }
                } };
    }



    private void countStarsOnFirstPage(DetailedResult[] starsResults) {
        starsOnFirstPage = starsResults.length;
    }



    private void setPagesCount(int i) {
        pagesCount = i;
    }



    private void countStarsOnLastPage(DetailedResult[] starsResults) {
        starsOnLastPage = starsResults.length;
    }



    private void checkIfAreAllNeededPages() throws InvalidJsonObjectException{
        if(starsOnFirstPage == -1 || starsOnLastPage == -1)
            throw new InvalidJsonObjectException("First or last page of stars exchange missing");
    }



    private int countStars() {
        if(pagesCount > 1)
            return starsOnFirstPage * (pagesCount-1) + starsOnLastPage;
        else
            return starsOnFirstPage;
    }



    private UserStarredResult createStarredResult(JsonUserStarred jsonUserStarred) {
        return new UserStarredResult(jsonUserStarred.getId(), jsonUserStarred.getUrl());
    }
}
