package pl.nalazek.githubsearch;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import pl.nalazek.githubsearch.JsonObjects.JsonObject;
import pl.nalazek.githubsearch.JsonObjects.RepoSearchResult;
import pl.nalazek.githubsearch.JsonObjects.UserExpanded;
import pl.nalazek.githubsearch.JsonObjects.UserSearchResult;
import pl.nalazek.githubsearch.JsonObjects.UserStarred;

/**
 * This class is a holder of a response divided into two parts: headers and body (as a Java class inheriting from JsonObject).
 * The inheriting class is automatically chosen in order to ExchangeType passed in the constructor.
 * @author Daniel Nalazek
 */
public class ResponsePartitioned {
    private Headers headers;
    private String body;
    private ExchangeType exchangeType;
    private JsonObject jsonObject;
    private List<JsonObject> jsonObjectsList;

    /**
     * Constructor. Automatically converts HTML body to JsonObject depending on the ExchangeType.
     * @param headers Headers of the Response
     * @param body Body of the Response
     * @param exchangeType Type of exchange
     */
    public ResponsePartitioned(Headers headers, String body, ExchangeType exchangeType) {
        this.body = body;
        this.headers = headers;
        this.exchangeType = exchangeType;
        Gson gson = createGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        deserializeJSON(gson);
    }

    /**
     * Getter for ExchangeType
     * @return ExchangeType
     */
    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    /**
     * Getter for List of JsonObjects. Is only available when ExchangeType passed in constructor is equal to
     * {@link ExchangeType#USER_EXPAND_STARS}.
     * @return List of JsonObjects when ExchangeType is {@link ExchangeType#USER_EXPAND_STARS}, otherwise will return empty list
     */
    public List<JsonObject> getJsonObjectsList() {
        return jsonObjectsList;
    }

    /**
     * Getter for header of the response
     * @return Headers of the response
     */
    public Headers getHeaders() {
        return headers;
    }

    /**
     * Getter for the JsonObject. Available for all ExchangeType's instead of {@link ExchangeType#USER_EXPAND_STARS}.
     * @return JsonObject or null for {@link ExchangeType#USER_EXPAND_STARS}.
     */
    public JsonObject getJsonObject() {
        return jsonObject;
    }

    private void deserializeJSON(Gson gson) {
        switch(exchangeType) {
            case USER_SEARCH:
            case USER_PAGE:
                jsonObject = gson.fromJson(body, UserSearchResult.class);
                break;
            case REPOS_SEARCH:
            case REPOS_PAGE:
                jsonObject = gson.fromJson(body, RepoSearchResult.class);
                break;
            case USER_EXPAND:
                jsonObject = gson.fromJson(body, UserExpanded.class);
                break;
            case USER_EXPAND_STARS:
                Type userStarredType = new TypeToken<ArrayList<UserStarred>>(){}.getType();
                jsonObjectsList = gson.fromJson(body, userStarredType);
                break;
        }
    }

    @NonNull
    private Gson createGson(FieldNamingPolicy fieldNamingPolicy) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(fieldNamingPolicy);
        return gsonBuilder.create();
    }
}
