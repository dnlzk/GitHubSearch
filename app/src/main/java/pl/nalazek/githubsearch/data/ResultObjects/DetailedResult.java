package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This data class represents a parent for parsed detailed results.
 * @author Daniel Nalazek
 */
public abstract class DetailedResult extends Result {

    public final static String TYPE = "EmptyDetailedResult";

    protected DetailedResult(ExchangeType exchangeType) {
        super(exchangeType);
    }
}
