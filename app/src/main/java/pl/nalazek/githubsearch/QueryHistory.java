package pl.nalazek.githubsearch;

import java.util.LinkedHashMap;
import java.util.Observable;

/**
 * This class is used to hold all queries and responses. When a new pair has been inserted,
 * the SearchAgent is notified.
 * @author Daniel Nalazek
 * @see SearchAgent
 */
public class QueryHistory extends Observable {

    private LinkedHashMap<QueryTask, ResponsePackage> qHistory;

    /**
     * Add a new pair
     * @param key QueryTask as the key
     * @param value ResponsePackage as the value
     */
    public void put(QueryTask key, ResponsePackage value) {
        synchronized (this) {
            qHistory.put(key, value);
        }
        setChanged();
        notifyObservers(key);
    }

    /**
     * Get a pair based on a QueryTask key
     * @param key QueryTask as the key
     * @return ResponsePackage as the value
     */
    public synchronized ResponsePackage get(QueryTask key) {
        return qHistory.get(key);
    }
}
