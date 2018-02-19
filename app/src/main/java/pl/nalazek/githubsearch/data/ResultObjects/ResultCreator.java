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

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;

import pl.nalazek.githubsearch.data.ExchangeType;
import pl.nalazek.githubsearch.data.QueryObjects.SearchQuery;
import pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery;
import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResponsePartitioned;

/**
 * A creator which makes results. Provides creating-safe {@link SearchResult}
 * and {@link DetailedResult} concrete objects.
 */
public class ResultCreator implements ResultFactory {

    private ResultFactory resultFactory;


    /**
     * Creates results depending on passed {@link ResponsePartitioned}.
     * @return {@link SearchResult} or {@link DetailedResult} arrays. If passed a response with an unrecognized exchange type,
     * an empty array will be returned.
     */
    @Override
    public Result[] makeResults(ResponsePartitioned responsePartitioned) {

        ExchangeType type = responsePartitioned.getExchangeType();

        switch(type) {
            case USER_SEARCH:
            case REPOS_SEARCH:
                resultFactory = SearchResultFactory.getInstance();
                return makeResultsArray(responsePartitioned);

            case USER_DETAILED:
            case USER_DETAILED_STARS:
            case USER_DETAILED_AVATAR:
                resultFactory = new DetailedResultFactory();
                return makeResultsArray(responsePartitioned);

            default:
                return new Result[0];
        }
    }



    /**
     * Creates results list depending on passed {@link ResponsePackage}.
     * @return {@link SearchResult} or {@link DetailedResult} lists. If passed a response package
     * with an unrecognized {@link pl.nalazek.githubsearch.data.QueryObjects.Query} type an empty list will be returned.
     */
    @Override
    public List<? extends Result> makeResults(ResponsePackage responsePackage) {

        String queryType = responsePackage.getQueryType();

        switch(queryType) {
            case SearchQuery.TYPE:
                resultFactory = SearchResultFactory.getInstance();
                return makeResultsList(responsePackage);

            case UserDetailedQuery.TYPE:
                resultFactory = new DetailedResultFactory();
                return makeResultsList(responsePackage);

            default:
                return Collections.emptyList();
        }

    }



    private Result[] makeResultsArray(ResponsePartitioned responsePartitioned) {

        try {
            return resultFactory.makeResults(responsePartitioned);
        }
        catch (InvalidJsonObjectException e) {
            e.printStackTrace();
            return new Result[0];
        }
    }


    private List<? extends Result> makeResultsList(ResponsePackage responsePackage) {

        try {
            return resultFactory.makeResults(responsePackage);
        }
        catch (InvalidJsonObjectException | InvalidObjectException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
