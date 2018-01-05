package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class UserSearchResult extends SearchResult {

    private String userURL;
    private String starredURL;
    private String avatarURL;



    /**
     * @param name Name to show on list
     * @param description Description of the result
     * @param exchangeType Type of search entry
     */
    public UserSearchResult(String name, String description, String userURL, String starredURL, String avatarURL, ExchangeType exchangeType) {
        super(name, description, exchangeType);
        this.userURL = userURL;
        this.starredURL = starredURL;
        this.avatarURL = avatarURL;
    }

    public String getUserURL() {
        return userURL;
    }

    public String getStarredURL() { return starredURL; }

    public String getAvatarURL() { return avatarURL; }

}
