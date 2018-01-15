package pl.nalazek.githubsearch.data.QueryObjects;

import android.support.annotation.Nullable;

import java.net.URL;
import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This abstract class should be inherited by new query types.
 * @author Daniel Nalazek
 */
public abstract class Query {

    protected ExchangeType type;
    protected URL url;

    public abstract String getQueryType();

    public ExchangeType getExchangeType() { return type; }

    @Nullable
    public URL getURL() { return url; }
}
