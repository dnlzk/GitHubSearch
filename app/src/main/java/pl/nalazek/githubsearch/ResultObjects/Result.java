package pl.nalazek.githubsearch.ResultObjects;

import pl.nalazek.githubsearch.ExchangeType;

/**
 * This abstract class is used as a parent for classes which are used to keep parsed from JSON HTTP requests
 * @author Daniel Nalazek
 */

public abstract class Result {

    protected ExchangeType exchangeType;

    protected Result(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public ExchangeType getType() {
        return exchangeType;
    }
}
