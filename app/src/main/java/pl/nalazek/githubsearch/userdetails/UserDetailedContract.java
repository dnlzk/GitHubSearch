package pl.nalazek.githubsearch.userdetails;

import java.util.List;

import pl.nalazek.githubsearch.data.ResultObjects.Result;
import pl.nalazek.githubsearch.data.ResultObjects.SearchResult;

/**
 * @author Daniel Nalazek
 */

public interface UserDetailedContract {

    interface Showable {

        void showResults(List<? extends Result> resultsArray);

        void showError(String error);

        void showBusy();
    }



    interface UserActionListener {

        void requestDetailedData(SearchResult searchResult);
    }
}
