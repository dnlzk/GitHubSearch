package pl.nalazek.githubsearch;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * This class is used to hold all queries and responses. When a new pair has been inserted,
 * the SearchAgent is notified.
 * @author Daniel Nalazek
 * @see SearchAgent
 */
public class QueryHistory extends Observable {

    private LinkedHashMap<QueryTask, ResponsePackage> qHistory = new LinkedHashMap<>();


    /**
     * Adds a new pair
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
     * Gets a response value based on a QueryTask key
     * @param key QueryTask as the key
     * @return ResponsePackage as the value
     */
    public synchronized ResponsePackage get(QueryTask key) {
        return qHistory.get(key);
    }


    public synchronized int getHistorySize() {
        return qHistory.size();
    }

    /**
     * Returns last arrived response
     * @return ResponsePackage or null when no responses are available
     */
    public synchronized ResponsePackage getLastResponse() {
        Iterator<Map.Entry<QueryTask,ResponsePackage>> it = qHistory.entrySet().iterator();
        ResponsePackage responsePackage = null;
        while(it.hasNext())
        {
            Map.Entry<QueryTask,ResponsePackage> entry = it.next();
            responsePackage = entry.getValue();
        }
        return responsePackage;
    }
}
