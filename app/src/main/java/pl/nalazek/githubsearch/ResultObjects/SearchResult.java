package pl.nalazek.githubsearch.ResultObjects;

import pl.nalazek.githubsearch.ExchangeType;

/**
 * This data class represents a parsed search result used to fill in the CustomListAdapter
 * @author Daniel Nalazek
 */

public abstract class SearchResult extends Result {

    private String title;
    private String description;

    protected SearchResult(String title, String description, ExchangeType exchangeType ) {
        super(exchangeType);
        this.title = title;
        this.description = description;
    }

    public String getName() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
