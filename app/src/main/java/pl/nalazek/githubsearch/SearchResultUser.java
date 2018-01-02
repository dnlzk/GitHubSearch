package pl.nalazek.githubsearch;

/**
 * @author Daniel Nalazek
 */

public class SearchResultUser extends SearchResult {

    private String userURL;

    /**
     * @param name Name to show on list
     * @param description Description of the result
     * @param exchangeType Type of search entry 0-user, 1-repo
     */
    public SearchResultUser(String name, String description, String userURL, ExchangeType exchangeType) {
        super(name, description, exchangeType);
        this.userURL = userURL;
    }

}
