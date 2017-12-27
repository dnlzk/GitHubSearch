package pl.nalazek.githubsearch;

/**
 * This data class represents a parsed search result used to fill in the CustomListAdapter
 * @author Daniel Nalazek
 */

public class SearchResult {

    private String text;
    private String description;
    private ExchangeType exchangeType;

    /**
     * @param name Name to show on list
     * @param description Description of the result
     * @param exchangeType Type of search entry 0-user, 1-repo
     */
    public SearchResult(String name, String description, ExchangeType exchangeType) {
        this.text = name;
        this.description = description;
        this.exchangeType = exchangeType;

    }

    public String getName() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public ExchangeType getType() {
        return exchangeType;
    }
}
