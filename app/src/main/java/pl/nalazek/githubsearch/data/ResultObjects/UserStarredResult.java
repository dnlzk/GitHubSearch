package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class UserStarredResult extends DetailedResult {

    public final static String TYPE = "UserStarredResult";

    private int id;
    private String url;

    public UserStarredResult(int id, String url) {
        super(ExchangeType.USER_DETAILED_STARS);
        this.id = id;
        this.url = url;
    }

    @Override
    public String getResultType() {
        return TYPE;
    }

}
