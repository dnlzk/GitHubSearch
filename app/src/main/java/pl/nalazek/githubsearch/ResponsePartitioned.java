package pl.nalazek.githubsearch;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Headers;

/**
 * @author Daniel Nalazek
 */
public class ResponsePartitioned {
    private Headers headers;
    private String body;
    private ExchangeType exchangeType;
    private JsonObject jsonObject;
    private Gson gson;

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public Headers getHeaders() {
        return headers;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    /**
     * Constructor. Automatically converts HTML body to JsonObject in depending on ExchangeType
     * @param headers Headers of the Response
     * @param body Body of the Response
     * @param exchangeType Type of exchange

     */
    public ResponsePartitioned(Headers headers, String body, ExchangeType exchangeType) {
        this.body = body;
        this.headers = headers;
        this.exchangeType = exchangeType;
        gson = createGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

    }

    private void deserializeJSON() {
        switch(exchangeType) {
            case USER_SEARCH:
                jsonObject = gson.fromJson(body, UserSearchResult.class);
                break;
        }
    }

    private Gson createGson(FieldNamingPolicy fieldNamingPolicy) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(fieldNamingPolicy);
        return gsonBuilder.create();
    }
}
