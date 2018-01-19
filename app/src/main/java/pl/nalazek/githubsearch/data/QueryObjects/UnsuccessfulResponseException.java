package pl.nalazek.githubsearch.data.QueryObjects;

/**
 * @author Daniel Nalazek
 */

public class UnsuccessfulResponseException extends Exception {

    public UnsuccessfulResponseException(String detailMessage) {
        super(detailMessage);
    }
}
