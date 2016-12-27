package pl.nalazek.githubsearch;

import java.util.ArrayList;

/**
 * todo code edition
 * @author Daniel Nalazek
 */
public class ResponsePackage
{
    private ArrayList<String> responses;

    /**
     * Adds a new response to the responses array
     * @param response response string to add
     * @return returns the size of the responses array
     */
    public int addResponse(String response) {
        responses.add(response);
        return responses.size();
    }
}
