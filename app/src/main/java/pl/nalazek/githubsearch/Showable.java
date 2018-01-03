package pl.nalazek.githubsearch;

import java.util.ArrayList;
import pl.nalazek.githubsearch.ResultObjects.Result;

/**
 * This interface is used to be applied for classes being used to keep and show results parsed from JSON
 * @author Daniel Nalazek
 */

public interface Showable {
    void showResults(ArrayList<? extends Result> resultsArray);
}
