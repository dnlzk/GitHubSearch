package pl.nalazek.githubsearch.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import pl.nalazek.githubsearch.data.JsonObjects.*;
import pl.nalazek.githubsearch.data.ResultObjects.InvalidJsonObjectException;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A holder class of partitioned response.
 * According to exchange type, parses body to a {@link JsonObject}, {@link List<JsonUserStarred>} or {@link android.graphics.Bitmap}.
 * @author Daniel Nalazek
 */
public class ResponsePartitioned {

    private Headers headers;
    private ResponseBody body;

    private JsonObject jsonObject = null;
    private List<JsonUserStarred> jsonObjectsList = Collections.emptyList();

    private Bitmap avatar = null;
    private String nextPage = null;
    private String lastPage = null;

    private int lastPageNumber = -1;
    private boolean isFirstPage = false;
    private boolean isLastPage = false;

    private final String message;
    private final ExchangeType exchangeType;



    /**
     * Constructor. Automatically parses {@link ResponseBody} to JsonObject depending on the ExchangeType. Additionally sets up pagination
     * variables from {@link Headers}.
     * @throws InvalidJsonObjectException when an parse error occurs.
     * @throws NullPointerException when an input argument is <code>null</code>
     */
    public ResponsePartitioned(@NonNull Headers headers,
                               @NonNull ResponseBody body,
                               @NonNull String message,
                               @NonNull ExchangeType exchangeType)
                                throws InvalidJsonObjectException {

        checkNotNull(headers);
        checkNotNull(body);
        checkNotNull(message);
        checkNotNull(exchangeType);

        this.body = body;
        this.headers = headers;
        this.message = message;
        this.exchangeType = exchangeType;

        parseAndSetPagination();
    }


    public ExchangeType getExchangeType() {
        return exchangeType;
    }



    /**
     * Getter for List of JsonObjects. Will return proper results when ExchangeType passed in constructor is equal to
     * {@link ExchangeType#USER_DETAILED_STARS}. Otherwise and in case of parse error will return empty list.
     * @return List of JsonObjects when ExchangeType is {@link ExchangeType#USER_DETAILED_STARS} or empty list
     */
    public List<JsonUserStarred> getJsonObjectsList() {
        return jsonObjectsList;
    }



    /**
     * Getter for JsonObject. Available for all ExchangeType's instead of {@link ExchangeType#USER_DETAILED_STARS} and {@link ExchangeType#USER_DETAILED_AVATAR}.
     * @return JsonObject or null for {@link ExchangeType#USER_DETAILED_STARS}, {@link ExchangeType#USER_DETAILED_AVATAR} and parsing error
     */
    @Nullable
    public JsonObject getJsonObject() {
        return jsonObject;
    }


    /**
     * Getter for Avatar.
     * @return Bitmap when ExchangeType is {@link ExchangeType#USER_DETAILED_AVATAR}, otherwise null
     */
    @Nullable
    public Bitmap getAvatar() {
        return avatar;
    }


    @Nullable
    public String getNextPageURL() {
        return nextPage;
    }


    @Nullable
    public String getLastPageURL() {
        return lastPage;
    }


    public int getLastPageNumber() {
        return lastPageNumber;
    }


    public boolean isFirstPage() {
        return isFirstPage;
    }


    public boolean isLastPage() {
        return isLastPage;
    }


    public String getMessage() {
        return message;
    }


    private void parseAndSetPagination() throws InvalidJsonObjectException {
        new JsonFactory().deserializeBody();
        new GitHubHeaderParser().setPageVariables();
    }



    private class GitHubHeaderParser {

        private final static String GITHUB_NEXT_PAGE = "; rel=\"next\"";
        private final static String GITHUB_LAST_PAGE = "; rel=\"last\"";
        private final static String GITHUB_FIRST_PAGE = "; rel=\"first\"";
        private final static String GITHUB_PREV_PAGE = "; rel=\"prev\"";
        private final static String GITHUB_PAGE = "page=";



        void setPageVariables() {

            String[] links = getLinksHeader(headers);
            if(links != null) parseLinksHeader(links);
            else {
                setSingletonPage();
            }

            if(isSingletonPage())
                lastPageNumber = 1;
        }



        @Nullable
        private String[] getLinksHeader(Headers headers) {

            String linkHeaderEntry = headers.get("Link");
            if(linkHeaderEntry != null)
                return linkHeaderEntry.split(",");
            else return null;
        }



        private void parseLinksHeader(String[] links) {

            for (String link : links) {
                if (link.contains(GITHUB_NEXT_PAGE)) {
                    nextPage = trimOutUrl(link);
                }
                if (link.contains(GITHUB_LAST_PAGE)) {
                    lastPage = trimOutUrl(link);
                    lastPageNumber = trimOutPageNumber(lastPage);
                }
            }
            checkAndSetFirstPageLastPage(links);
        }



        private void checkAndSetFirstPageLastPage(String[] links) {
            if (isLastPage(links)) isLastPage = true;
            if (isFirstPage(links)) isFirstPage = true;
        }



        private boolean isLastPage(String[] links) {

            StringList linkList = new StringList(links);
            return linkList.containsAnywhereString(GITHUB_FIRST_PAGE) &&
                    linkList.containsAnywhereString(GITHUB_PREV_PAGE) &&
                    !linkList.containsAnywhereString(GITHUB_NEXT_PAGE) &&
                    !linkList.containsAnywhereString(GITHUB_LAST_PAGE);
        }



        private boolean isFirstPage(String[] links) {

            StringList linkList = new StringList(links);
            return !linkList.containsAnywhereString(GITHUB_FIRST_PAGE) &&
                    !linkList.containsAnywhereString(GITHUB_PREV_PAGE) &&
                    linkList.containsAnywhereString(GITHUB_NEXT_PAGE) &&
                    linkList.containsAnywhereString(GITHUB_LAST_PAGE);
        }



        private String trimOutUrl(String input) {
            int start = input.indexOf('<');
            int stop = input.indexOf('>');
            return input.substring(start+1,stop);
        }



        private int trimOutPageNumber(String input) {

            int position = input.lastIndexOf(GITHUB_PAGE);
            position += GITHUB_PAGE.length();
            String trimmedPage = input.substring(position);
            return Integer.valueOf(trimmedPage);
        }



        private void setSingletonPage() {
            isFirstPage = true;
            isLastPage = true;
        }


        private boolean isSingletonPage() {
            return isLastPage && isFirstPage;
        }
    }




    private class JsonFactory {

        private Gson gson;

        JsonFactory() {
            this.gson = createGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }


        void deserializeBody() throws InvalidJsonObjectException {

            switch(exchangeType) {

                case USER_SEARCH:
                case USER_PAGE:
                    jsonObject = generateFromJson(JsonUserSearchResult.class);
                    break;

                case REPOS_SEARCH:
                case REPOS_PAGE:
                    jsonObject = generateFromJson(JsonRepoSearchResult.class);
                    break;

                case USER_DETAILED:
                    jsonObject = generateFromJson(JsonUserDetailed.class);
                    break;

                case USER_DETAILED_STARS:
                    Type userStarredType = new TypeToken<ArrayList<JsonUserStarred>>(){}.getType();
                    jsonObjectsList = generateFromJson(userStarredType);
                    break;

                case USER_DETAILED_AVATAR:
                    InputStream stream =  body.byteStream();
                    avatar = BitmapFactory.decodeStream(stream);
                    break;
            }
        }


        @NonNull
        private Gson createGson(FieldNamingPolicy fieldNamingPolicy) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(fieldNamingPolicy);
            return gsonBuilder.create();
        }


        @Nullable
        private <T> T generateFromJson(Class<T> classOfT) throws InvalidJsonObjectException {

            try {
                return gson.fromJson(body.string(), classOfT);
            }
            catch (IOException e) {
                System.out.println("Cannot resolve string from body");
                e.printStackTrace();
                return null;
            }
            catch (JsonParseException e) {
                throw new InvalidJsonObjectException("Json parse exception");
            }
        }


        @Nullable
        private <T> T generateFromJson(Type type) throws InvalidJsonObjectException {

            try {
                return gson.fromJson(body.string(), type);
            }
            catch (IOException e) {
                System.out.println("Cannot resolve string from body");
                e.printStackTrace();
                return null;
            }
            catch (JsonParseException e) {
                throw new InvalidJsonObjectException("Json parse exception");
            }
        }
    }



    private class StringList {

        private List<String> list;


        StringList(String[] input) {
            list = Arrays.asList(input);
        }


        boolean containsAnywhereString(String match) {
            for(String element : list) {
                if(element.contains(match)) return true;
            }
            return false;
        }
    }
}
