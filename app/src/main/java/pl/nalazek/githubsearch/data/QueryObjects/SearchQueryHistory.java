package pl.nalazek.githubsearch.data.QueryObjects;

import android.support.annotation.NonNull;

import java.util.Observable;
import java.util.TreeMap;

import pl.nalazek.githubsearch.data.ResponsePackage;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class is used to hold all searched keywords and responses.
 * @author Daniel Nalazek
 */
public class SearchQueryHistory extends Observable {

    private TreeMap<String, ResponsePackage> historyMap = new TreeMap<>();

    /**
     * Adds a new pair of a keyword and a ResponsePackage. If a keyword exists it will be overwritten.
     * @param keyword QueryTask as the keyword
     * @param responsePackage ResponsePackage as the responsePackage
     */
    synchronized public void put(@NonNull String keyword, @NonNull ResponsePackage responsePackage) {

        checkNotNull(responsePackage);
        historyMap.put(keyword, responsePackage);
        setChanged();
        notifyObservers(keyword);
    }

    /**
     * Gets a ResponsePackage value based on a String keyword.
     * @param keyword Keyword String
     * @return ResponsePackage value
     */
    public synchronized ResponsePackage get(String keyword) {
        return historyMap.get(keyword);
    }

    public synchronized int getHistorySize() {
        return historyMap.size();
    }

    /**
     * Returns last saved response
     * @return ResponsePackage or null when no responses are available
     */
    public synchronized ResponsePackage getLastResponse() {
        return historyMap.lastEntry().getValue();
    }

    public synchronized boolean isKeywordInHistory(@NonNull String keyword) {
        return historyMap.containsKey(keyword);
    }
}
