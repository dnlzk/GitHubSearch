package pl.nalazek.githubsearch;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Response;

/**
 * This class is used as a response holder. Depending on the amount of passed queries to QueryTask it can hold one or two responses.
 * In case of errors occurred during the exchange there is an error message available. The response array may be empty
 * at that time.
 * @author Daniel Nalazek
 */
public class ResponsePackage
{
    private static final String LOG_TAG = "ResponsePackage Class";
    private ArrayList<ResponsePartitioned> responses = new ArrayList<>();
    private String message = null;

    private String nextURL;

    /**
     * Default constructor
     */
    public ResponsePackage() {}

    /**
     * Constructor with String parameter. Typically used when QueryTask was cancelled and no responses would be added.
     * @param message Message to pass
     */
    public ResponsePackage(String message) {
        this.message = message;
    }

    /**
     * Adds a new response to the responses array
     * @param response Response from OkHttp library to add
     * @return Returns true if response is complete, false otherway. If false it means that the server want to send the response in parts, divided to pages.
     * Next page URL can be achieved by calling {@link #getNextURL()} method.
     */
    //TODO back to int return
    public boolean addResponse(Response response, ExchangeType exchangeType) {
        ResponsePartitioned responsePartitioned;
        try {
            responsePartitioned = new ResponsePartitioned(response.headers(), response.body().string(), exchangeType);
            responses.add(responsePartitioned);
            return isResponseComplete(responsePartitioned);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,e.getMessage());
        }
        return true;
    }

    /**
     * Sets a status message.
     * @param message the message string
     */
    public void addMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the status message if response is invalid.
     * Otherwise returns null.
     * @return String with message or null when response/s are valid.
     */
    public String getMessage() {
        if(message != null) return message;
        else return null;
    }

    /**
     * Returns an ArrayList of all responses in that ResponsePackage
     * @return ArrayList with ResponsePartitioned type parameter
     * @see ResponsePartitioned
     */
    public ArrayList<ResponsePartitioned> getResponses() {
        return responses;
    }

    public String getNextURL() {
        return parseNextPageURL(nextURL);
    }

    public boolean isLastResponseComplete() {
        return isResponseComplete(responses.get(responses.size()-1));
    }
    private boolean isResponseComplete(ResponsePartitioned responsePartitioned) {
        String linkHeaderEntry = responsePartitioned.getHeaders().get("Link");
        nextURL = linkHeaderEntry;
        return linkHeaderEntry == null;
    }

    private String parseNextPageURL(String linkHeaderEntry) {
        return getFirstUrl(linkHeaderEntry);
    }

    private String getFirstUrl(String input) {
        int start = input.indexOf('<');
        int stop = input.indexOf('>');
        return input.substring(start+1,stop);
    }
}
