package pl.nalazek.githubsearch.ResultObjects;

import pl.nalazek.githubsearch.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class UserSearchResult extends SearchResult {

    private String userURL;

    /**
     * @param name Name to show on list
     * @param description Description of the result
     * @param exchangeType Type of search entry 0-user, 1-repo
     */
    public UserSearchResult(String name, String description, String userURL, ExchangeType exchangeType) {
        super(name, description, exchangeType);
        this.userURL = userURL;
    }

    public String getUserURL() {
        return userURL;
    }

}
