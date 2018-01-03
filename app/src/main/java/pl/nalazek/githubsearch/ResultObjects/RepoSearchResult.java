package pl.nalazek.githubsearch.ResultObjects;

import pl.nalazek.githubsearch.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class RepoSearchResult extends SearchResult {

    public RepoSearchResult(String title, String descrition, ExchangeType exchangeType) {
        super(title, descrition, exchangeType);
    }
}
