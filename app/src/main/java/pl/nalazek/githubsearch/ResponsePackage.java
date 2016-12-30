package pl.nalazek.githubsearch;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Response;

/**
 * todo code edition
 * @author Daniel Nalazek
 */
public class ResponsePackage
{
    private static final String LOG_TAG = "ResponsePackage Class";
    private ArrayList<ResponsePartitioned> responses = new ArrayList<>();
    private String messageHTTP = null;

    /**
     * Constructor. Typically used when QueryTask was cancelled and no responses would be added.
     * @param message Message to pass
     */
    public ResponsePackage(String message) {
        messageHTTP = message;
    }

    /**
     * Adds a new response to the responses array
     * @param response Response from OkHttp library to add
     * @return returns the size of the responses array
     */
    public int addResponse(Response response, ExchangeType exchangeType) {
        try {
            responses.add(new ResponsePartitioned(response.headers(), response.body().string(), exchangeType));
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,e.getMessage());
        }
        return responses.size();
    }

    /**
     * Adds a HTTP status message.
     * @param message the message string
     */
    public void addMessage(String message) {
        messageHTTP = message;
    }

    /**
     * Returns the HTTP status message if response is invalid.
     * Otherwise returns null.
     * @return String with message or null when response is proper.
     */
    public String getMessage() {
        if(messageHTTP != null) return messageHTTP;
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

}
