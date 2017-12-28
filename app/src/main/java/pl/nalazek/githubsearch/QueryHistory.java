package pl.nalazek.githubsearch;


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

/**
 * This class is used to hold all queries and responses.
 * @author Daniel Nalazek
 */
public class QueryHistory extends Observable {

    private LinkedHashMap<QueryTask, ResponsePackage> qHistory = new LinkedHashMap<>();
    private TreeMap<String, QueryTask> phraseOccurancesTreeMap = new TreeMap<>();


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
        addPhraseToTreeMap(key);
        notifyObservers(key);
    }

    /**
     * Gets a response value based on a QueryTask key
     * @param key QueryTask as the key
     * @return ResponsePackage as the value
     */
    public synchronized ResponsePackage get(String key) {
        QueryTask queryTask = phraseOccurancesTreeMap.get(key);
        return qHistory.get(queryTask);
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

    public synchronized boolean isPhraseInHistory(String phrase) {
        return phraseOccurancesTreeMap.containsKey(phrase);
    }

    private void addPhraseToTreeMap(QueryTask queryTask) {
        String newPhrase = queryTask.getPhraseString();
        phraseOccurancesTreeMap.put(newPhrase, queryTask);
    }
}
