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

package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.common.collect.Iterables;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Response;
import pl.nalazek.githubsearch.data.QueryObjects.Query;
import pl.nalazek.githubsearch.data.ResultObjects.InvalidJsonObjectException;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class is used as a response holder.
 */
public class ResponsePackage
{
    private final String queryType;
    private ArrayList<ResponsePartitioned> responses = new ArrayList<>();
    private ArrayMap<String, Query> errorMessages = new ArrayMap<>();
    private TreeMap<ExchangeType, ArrayList<ResponsePartitioned>> exchangeTypeMap = new TreeMap<>();



    /**
     * @param queryType Type of queries to which this response package is created
     * @throws NullPointerException when parameter is <code>null</code>
     * @see pl.nalazek.githubsearch.data.QueryObjects.SearchQuery#TYPE
     * @see pl.nalazek.githubsearch.data.QueryObjects.UserDetailedQuery#TYPE
     */
    public ResponsePackage(@NonNull String queryType) {
        checkNotNull(queryType);
        this.queryType = queryType;
    }



    /**
     * Adds a new response that will be previously converted to {@link ResponsePartitioned}.
     * @throws InvalidJsonObjectException when an paring error occurs.
     * @throws NullPointerException when one of parameters is <code>null</code>
     */
    public ResponsePackage addResponse(@NonNull Response response,
                                       @NonNull String message,
                                       @NonNull ExchangeType exchangeType)
                                        throws InvalidJsonObjectException {
        checkNotNull(response);
        checkNotNull(message);
        checkNotNull(exchangeType);

        ResponsePartitioned responsePartitioned =
                new ResponsePartitioned(response.headers(), response.body(), message, exchangeType);

        responses.add(responsePartitioned);
        updateExchangeTypeMap(responsePartitioned);

        return this;
    }


    /**
     * Adds a new response.
     * @throws NullPointerException when parameter is <code>null</code>
     */
    public ResponsePackage addResponse(@NonNull ResponsePartitioned responsePartitioned) {

        checkNotNull(responsePartitioned);

        responses.add(responsePartitioned);
        updateExchangeTypeMap(responsePartitioned);

        return this;
    }



    public ResponsePackage addResponses(List<ResponsePartitioned> responsesPartitioned) {

        responses.addAll(responsesPartitioned);
        updateExchangeTypeMap(responsesPartitioned);
        return this;
    }


    public ResponsePackage addErrorMessageAndQuery(String message, Query unsuccessfulQuery) {
        errorMessages.put(message, unsuccessfulQuery);
        return this;
    }


    public ArrayList<ResponsePartitioned> getResponses() {
        return responses;
    }


    public ArrayMap<String, Query> getErrorMessagesMap() {
        return errorMessages;
    }


    @NonNull
    public String getQueryType() { return queryType; }


    /**
     * @return <code>null</code> when package is empty, otherwise {@link ExchangeType}.
     */
    @Nullable
    public ExchangeType getLastResponseExchangeType() {
        if(!isEmpty())
            return responses.get(responses.size()-1).getExchangeType();
        else return null;
    }


    /**
     * @return <code>null</code> when response not found, otherwise {@link ResponsePartitioned}.
     */
    @Nullable
    public ResponsePartitioned getLastResponseWithExchangeType(ExchangeType exchangeType) {
        List<ResponsePartitioned> responses = exchangeTypeMap.get(exchangeType);
        return responses != null ? Iterables.getLast(responses) : null;
    }



    /**
     * @return <code>null</code> when responses not found, otherwise list
     * of {@link ResponsePartitioned}.
     */
    @Nullable
    public List<ResponsePartitioned> getAllResponsesWith(ExchangeType exchangeType) {
        return exchangeTypeMap.get(exchangeType);
    }



    /**
     * @throws InvalidObjectException when query types of packages are different
     * @see #getQueryType()
     */
    public void combineResponsePackages(ResponsePackage responsePackage) throws InvalidObjectException{

        checkIfTypeCorrect(responsePackage);
        responses.addAll(responsePackage.responses);
        updateExchangeTypeMap(responsePackage.responses);
        errorMessages.putAll((Map<String,Query>)responsePackage.errorMessages);
    }



    private void checkIfTypeCorrect(ResponsePackage responsePackage) throws InvalidObjectException {
        if(!queryType.equals(responsePackage.queryType))
            throw new InvalidObjectException("Cannot combine packages. Query type incompatibility!");
    }


    /**
     * @return <code>true</code> when package has no responses and no error messages,
     * <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return hasNoResponses() && hasNoErrorMessages();
    }


    public boolean hasNoResponses() {
        return responses.isEmpty();
    }


    public boolean hasNoErrorMessages() {
        return errorMessages.isEmpty();
    }


    private void updateExchangeTypeMap(List<ResponsePartitioned> responsesPartitioned) {
        for(ResponsePartitioned response : responsesPartitioned)
            updateExchangeTypeMap(response);
    }


    private void updateExchangeTypeMap(ResponsePartitioned responsePartitioned) {

        ExchangeType exchangeType = responsePartitioned.getExchangeType();
        ArrayList<ResponsePartitioned> responsesList = exchangeTypeMap.get(exchangeType);

        if(responsesList != null)
            responsesList.add(responsePartitioned);
        else
            exchangeTypeMap.put(exchangeType,
                    new ArrayList<>(Collections.singletonList(responsePartitioned)));
    }
}
