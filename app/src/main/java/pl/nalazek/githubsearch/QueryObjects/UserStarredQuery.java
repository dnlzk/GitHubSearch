package pl.nalazek.githubsearch.QueryObjects;

import java.net.URL;

import pl.nalazek.githubsearch.ExchangeType;
import pl.nalazek.githubsearch.Showable;

/**
 * @author Daniel Nalazek
 */

public class UserStarredQuery extends Query {
    public UserStarredQuery(URL url, Showable showable) {
        this.showable = showable;
        this.url = url;
        this.eType = ExchangeType.USER_EXPAND_STARS;
    }
}
