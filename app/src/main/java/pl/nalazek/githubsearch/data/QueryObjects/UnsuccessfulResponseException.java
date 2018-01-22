package pl.nalazek.githubsearch.data.QueryObjects;

/**
 * Thrown when an error occured during query execution in QueryTask
 * @see QueryTask
 * @author Daniel Nalazek
 */
public class UnsuccessfulResponseException extends Exception {

    public UnsuccessfulResponseException(String detailMessage) {
        super(detailMessage);
    }
}
