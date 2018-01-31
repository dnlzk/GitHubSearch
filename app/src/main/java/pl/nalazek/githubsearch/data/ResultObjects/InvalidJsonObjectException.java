package pl.nalazek.githubsearch.data.ResultObjects;

/**
 * @author Daniel Nalazek
 */

public class InvalidJsonObjectException extends Exception {

    public InvalidJsonObjectException(String detailMessage) {
        super(detailMessage);
    }
}
