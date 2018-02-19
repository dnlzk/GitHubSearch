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

import java.util.List;
import pl.nalazek.githubsearch.data.ResultObjects.*;

/**
 * This interface is the main entry point for accessing GitHub repository
 */
public interface GitHubRepository {

    void requestSearch(@NonNull String keyword, @NonNull GitHubRepositorySearchOptions searchOptions, @NonNull SearchResultsCallback searchResultsCallback);

    void requestDetailedData(@NonNull SearchResult searchResult, @NonNull DetailedResultsCallback detailedResultsCallback);

    void close();

    void stopSearch();



    interface SearchResultsCallback {
        void onSearchResultsReady(List<? extends Result> results);
        void onError(String message);
    }



    interface DetailedResultsCallback {
        void onDetailedDataResultReady(List<? extends Result> result);
        void onError(String message);
    }
}
