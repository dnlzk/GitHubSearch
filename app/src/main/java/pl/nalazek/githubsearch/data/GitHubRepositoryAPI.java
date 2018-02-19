package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import pl.nalazek.githubsearch.data.QueryObjects.*;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;
import pl.nalazek.githubsearch.data.ResultObjects.UserSearchResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Daniel Nalazek
 */
public class GitHubRepositoryAPI implements GitHubRepositoryAPIInterface,
        GitHubRepositoryAPIInterface.QueryTaskCallback {

    public static final String STATE_MALFORMED_URL = "Malformed URL at starred query builder";
    public static final String STATE_NULL_QUERY = "Query cannot be null";
    public static final String STATE_BUFFER_COMBINE_ERROR =
            "Cannot combine response package with buffer";

    private QueryTask actualProcessingTask = null;
    private ResultsAPICallback callback;
    private OkHttpClient client;
    private QueryBuilder queryBuilder;
    private ResponsePackage bufferedResponse = null;
    private boolean shouldMakeAnotherQuery = false;
    private Predicate<Query> isNullQuery;


    public GitHubRepositoryAPI(OkHttpClient client) {
        this.client = client;
        queryBuilder = new QueryBuilder();
        createNullQueryPredicate();
    }



    /**
     * Starts a search. Note that keywords with whithespaces only will be ignored;
     */
    @Override
    public void startSearch(@NonNull String keyword,
                            @NonNull GitHubRepositorySearchOptions searchOptions,
                            @NonNull SearchAPICallback searchCallback) {

        callback = searchCallback;
        stopLastTask();

        Query[] queries = tryToCreateSearchQueries(keyword, searchOptions);
        proceedSearchWith(queries);
    }
    


    @Override
    public void getDetailedData(@NonNull SearchResult searchResult,
                                @NonNull DetailedDataAPICallback detailedDataCallback) {

        callback = detailedDataCallback;
        stopLastTask();
        proceedDataFrom(searchResult);
    }



    @Override
    public void stopLastTask() {
        try {
            stopLastTask(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    


    @Override
    public void onResponseReady(QueryTask queryTask, ResponsePackage responsePackage) {

        actualProcessingTask = null;

        if(hasResponsePackageError(responsePackage)) {
            callback.onError(getErrorStrings(responsePackage));
            clearBufferedResponse();
        }

        else if(shouldMakeAnotherQuery)
            proceedAnotherQuery(responsePackage);

        else
            callbackAndRefresh(responsePackage);

    }



    @Override
    public void finish() throws InterruptedException {
        stopLastTask(true);
        closeClientConnections();
    }




    private void createNullQueryPredicate() {

        isNullQuery = new Predicate<Query>() {
            @Override
            public boolean apply(Query query) {
                return query == null;
            }
        };
    }



    private void stopLastTask(boolean safe) throws InterruptedException {
        if(actualProcessingTask!=null) {
            actualProcessingTask.cancel(true);
            if(safe) waitForTaskIsCancelled();
        }
    }



    private void waitForTaskIsCancelled() throws InterruptedException {
        while(!actualProcessingTask.isCancelled())
            wait(50);
    }



    @Nullable
    private Query[] tryToCreateSearchQueries(String keyword, GitHubRepositorySearchOptions options)
    {
        try{
            return buildSearchQueries(keyword, options);
        }
        catch (WhitespaceKeywordException e) {
            callback.onResultsReady(new ResponsePackage(SearchQuery.TYPE));
            return null;
        }
    }



    private Query[] buildSearchQueries(String keyword, GitHubRepositorySearchOptions options)
            throws WhitespaceKeywordException {

        return queryBuilder.buildSearchQuery().setOrdering(options.getOrder())
                .setSorting(options.getSorting())
                .setResultsPerPage(options.getResultsPerPage())
                .setScope(options.isScopeUsers(), options.isScopeRepos())
                .build(keyword);
    }



    private void proceedSearchWith(Query[] queries) {
        if(queries != null)
            startNewQueryTask(queries);
    }



    private void startNewQueryTask(Query... queries) throws NullPointerException {

        if (anyIsNull(queries)) {
            callback.onError(STATE_NULL_QUERY);
            return;
        }

        runQueryTaskFrom(queries);
    }



    private boolean anyIsNull(Query[] queries) {
        return Iterables.any(Arrays.asList(queries), isNullQuery);
    }



    private void runQueryTaskFrom(Query[] queries) {

        QueryTask queryTask = new QueryTask(client, this);
        actualProcessingTask = queryTask;
        queryTask.execute(queries);
    }



    private void proceedDataFrom(SearchResult searchResult) {

        String type = searchResult.getResultType();

        switch(type) {
            case UserSearchResult.TYPE:
                executeRequestWith((UserSearchResult) searchResult);
                break;
            default:
                //TODO implement in future RepoSearchResult
        }
    }



    private void executeRequestWith(UserSearchResult userSearchResult) {

        Query[] queries = getQueriesForUserSearchResult(userSearchResult);
        shouldMakeAnotherQuery = true;
        startNewQueryTask(queries);
    }



    @NonNull
    private Query[] getQueriesForUserSearchResult(UserSearchResult userSearchResult) {

        Query starredQuery, userQuery, avatarQuery;

        starredQuery = tryToBuildSingleQuery(
                userSearchResult.getStarredURL(),
                ExchangeType.USER_DETAILED_STARS);

        userQuery = tryToBuildSingleQuery(
                userSearchResult.getUserURL(),
                ExchangeType.USER_DETAILED);

        avatarQuery = tryToBuildSingleQuery(
                userSearchResult.getAvatarURL(),
                ExchangeType.USER_DETAILED_AVATAR);

        return new Query[] {userQuery, avatarQuery, starredQuery};
    }



    @Nullable
    private Query tryToBuildSingleQuery(String url, ExchangeType exchangeType) {

        try {
            return queryBuilder.buildUserDetailedQuerry().build(new URL(url), exchangeType);
        }
        catch (MalformedURLException e)
        {
            callback.onError(QueryTask.STATE_MALFORMED_URL);
            e.printStackTrace();
            return null;
        }
    }



    private boolean hasResponsePackageError(ResponsePackage responsePackage) {
        return !responsePackage.getErrorMessagesMap().isEmpty();
    }



    private String getErrorStrings(ResponsePackage responsePackage) {
        String errorMessage = "";
        for(String error : responsePackage.getErrorMessagesMap().keySet()) {
            errorMessage += errorMessage.isEmpty() ? error : "\n" + error;
        }
        return errorMessage;
    }



    private void clearBufferedResponse() {
        bufferedResponse = null;
    }



    private void proceedAnotherQuery(ResponsePackage responsePackage) {

        ExchangeType type = responsePackage.getLastResponseExchangeType();
        checkNotNull(type);

        switch (type) {

            case USER_DETAILED_STARS:
                updateResponsePackageBuffer(responsePackage);
                requestLastPage(responsePackage, ExchangeType.USER_DETAILED_STARS);
                shouldMakeAnotherQuery = false;
                break;

            //TODO Many pages support
            default: callbackAndRefresh(responsePackage);
        }
    }



    private void requestLastPage(ResponsePackage from, ExchangeType type) {

        ResponsePartitioned response =
                from.getLastResponseWithExchangeType(type);

        checkNotNull(response);

        String url = response.getLastPageURL();

        if(url == null) flushBufferAndCallback();
        else {
            Query query = tryToBuildSingleQuery(url, type);
            startNewQueryTask(query);
        }
    }



    private void callbackAndRefresh(ResponsePackage responsePackage) {

        if(bufferedResponse !=null) {
           updateAndFlushBufferAndCallback(responsePackage);
        }
        else
            callback.onResultsReady(responsePackage);
    }



    private void updateAndFlushBufferAndCallback(ResponsePackage responsePackage) {
        updateResponsePackageBuffer(responsePackage);
        flushBufferAndCallback();
    }



    private void updateResponsePackageBuffer(ResponsePackage responsePackage) {

        if(bufferedResponse == null)
            bufferedResponse = responsePackage;
        else
            tryCombiningWithBuffer(responsePackage);
    }



    private void flushBufferAndCallback() {
        callback.onResultsReady(bufferedResponse);
        clearBufferedResponse();
    }



    private void tryCombiningWithBuffer(ResponsePackage responsePackage) {

        try {
            bufferedResponse.combineResponsePackages(responsePackage);
        } catch (InvalidObjectException e) {
            e.printStackTrace();
            callback.onError(STATE_BUFFER_COMBINE_ERROR);
        }
    }



    private void closeClientConnections() {
        new Thread(
                (new Runnable() {
                    @Override
                    public void run() {
                        client.dispatcher().executorService().shutdown();
                        client.connectionPool().evictAll();
                    }
                })).start();
    }
}
