package pl.nalazek.githubsearch.data.QueryObjects;

import java.net.URL;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * Use this query in order to get details about user
 * @author Daniel Nalazek
 */

public class UserDetailedQuery extends Query {

    public static final String TYPE = "Detailed";

    public UserDetailedQuery(URL url, ExchangeType exchangeType) {
        this.url = url;
        this.type = exchangeType;
    }

    @Override
    public String getQueryType() {
        return TYPE;
    }
}
