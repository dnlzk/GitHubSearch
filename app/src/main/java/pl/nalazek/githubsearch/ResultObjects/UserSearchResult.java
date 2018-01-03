package pl.nalazek.githubsearch.ResultObjects;

import pl.nalazek.githubsearch.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class UserSearchResult extends SearchResult {

    private String userURL;
    private String starredURL;

    /**
     * @param name Name to show on list
     * @param description Description of the result
     * @param exchangeType Type of search entry
     */
    public UserSearchResult(String name, String description, String userURL, String starredURL, ExchangeType exchangeType) {
        super(name, description, exchangeType);
        this.userURL = userURL;
        this.starredURL = starredURL;
    }

    public String getUserURL() {
        return userURL;
    }

}
