package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * @author Daniel Nalazek
 */

public class RepoSearchResult extends SearchResult {

    public RepoSearchResult(String title, String descrition, ExchangeType exchangeType) {
        super(title, descrition, exchangeType);
    }
}
