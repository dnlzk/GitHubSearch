package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This abstract class is used as a parent for classes which are used to keep parsed data from JSON HTTP requests.
 * Instances of Result class should ve created via {@link ResultCreator}
 * @author Daniel Nalazek
 */

public abstract class Result {

    protected ExchangeType exchangeType;

    protected Result(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    /**
     * Gets the {@link ExchangeType} of the Resut
     * @return {@link ExchangeType}
     */
    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    /**
     * Gets the type of the Result.
     */
    public abstract String getResultType();
}
