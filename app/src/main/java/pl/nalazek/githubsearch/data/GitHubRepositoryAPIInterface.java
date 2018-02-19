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

import pl.nalazek.githubsearch.data.QueryObjects.QueryTask;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;


public interface GitHubRepositoryAPIInterface {

    void startSearch(@NonNull String keyword,
                     @NonNull GitHubRepositorySearchOptions searchOptions,
                     @NonNull SearchAPICallback res);



    void getDetailedData(@NonNull SearchResult searchResult,
                         @NonNull DetailedDataAPICallback detailedDataCallback);



    /*
     * Finishes all connections with external data storages. Should be always called to free resources.
     */
    void finish() throws InterruptedException;



    /**
     * Stops last task. Usually used to avoid no more needed result callbacks.
     */
    void stopLastTask();



    interface ResultsAPICallback {
        void onResultsReady(ResponsePackage responsePackage);
        void onError(String message);
    }


    interface SearchAPICallback extends ResultsAPICallback {}


    interface DetailedDataAPICallback extends ResultsAPICallback {}


    interface QueryTaskCallback {
        void onResponseReady(QueryTask queryTask, ResponsePackage responsePackage);
    }
}
