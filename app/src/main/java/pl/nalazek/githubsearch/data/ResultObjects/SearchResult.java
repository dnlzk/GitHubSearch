package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcelable;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This data class represents a parent for parsed search results.
 * @author Daniel Nalazek
 */

public abstract class SearchResult extends Result implements Parcelable {

    public final static String PARCELABLE_TAG = "searchResult";
    public final static String TYPE = "SearchResult";

    private String title;
    private String description;

    protected SearchResult(String title, String description, ExchangeType exchangeType ) {
        super(exchangeType);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
