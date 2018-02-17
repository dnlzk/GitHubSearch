package pl.nalazek.githubsearch.search;

import java.util.List;

import pl.nalazek.githubsearch.data.ResultObjects.Result;

/**
 * @author Daniel Nalazek
 */
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
