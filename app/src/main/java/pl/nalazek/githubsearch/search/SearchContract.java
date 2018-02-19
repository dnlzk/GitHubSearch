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

package pl.nalazek.githubsearch.search;

import java.util.List;

import pl.nalazek.githubsearch.data.ResultObjects.Result;

public interface SearchContract {

    interface Showable {

        void showResults(List<? extends Result> resultsArray);

        void showError(String error);

        void showBusy();

        void hideBusy();
    }



    interface UserActionListener {

        /**
         * Requests search for keyword
         * @param checkInHistory Determines if an initial check in search history shoul be performed.
         *                       If result is found, no new request to external database will
         *                       be established. Result will be obtained form cache.
         */
        void requestSearch(String keyword, boolean checkInHistory);


        /**
         * Closes all connections with external databases.
         */
        void finish();

        void stopSearch();
    }



}
