package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This data class represents a parsed search result used to fill in the SearchResultListAdapter
 * @author Daniel Nalazek
 */

public abstract class SearchResult extends Result implements Parcelable {

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
