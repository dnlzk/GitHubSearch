package pl.nalazek.githubsearch;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Response;

/**
 * todo code edition
 * @author Daniel Nalazek
 */
public class ResponsePackage
{
    private static final String LOG_TAG = "ResponsePackage Class";
    private ArrayList<ResponsePartitioned> responses;
    private String messageHTTP = null;

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

}
