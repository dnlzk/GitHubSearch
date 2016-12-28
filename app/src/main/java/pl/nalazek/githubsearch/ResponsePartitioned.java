package pl.nalazek.githubsearch;

import okhttp3.Headers;
import okhttp3.internal.http2.Header;

/**
 * @author Daniel Nalazek
 */
public class ResponsePartitioned {
    private Headers headers;
    private String body;
    private ExchangeType exchangeType;
    private JsonObject jsonObject;

    public ResponsePartitioned(Headers headers, String body, ExchangeType exchangeType) {
        this.body = body;
        this.headers = headers;
        this.exchangeType = exchangeType;
    }
}
