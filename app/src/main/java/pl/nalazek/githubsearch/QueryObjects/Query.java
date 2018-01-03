package pl.nalazek.githubsearch.QueryObjects;

import java.net.URL;

import pl.nalazek.githubsearch.ExchangeType;
import pl.nalazek.githubsearch.Showable;

/**
 * @author Daniel Nalazek
 */

public abstract class Query {

    protected URL url;
    protected Showable showable;
    protected ExchangeType eType = null;

    public URL getURL() {
        return url;
    }

    public Showable getShowable() {
        return showable;
    }

    public ExchangeType getType() { return eType; }

}
