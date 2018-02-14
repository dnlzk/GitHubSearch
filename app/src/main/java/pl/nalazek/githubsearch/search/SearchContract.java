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

        void requestSearch(String phrase, boolean checkInHistory);

        void finish();

        void stopSearch();
    }



}
