package it.pack.utility.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

public final class HttpCall {

    /**
     * The key name that corresponds to the API call status code.
     */
    public static final String STATUS   = "Status";

    /**
     * The key name that corresponds to the body of the response returned by the API call
     */
    public static final String BODY     = "Body";

    /**
     * The Http Client.
     */
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * <p style="line-height: 1.3">
     * Execute a Rest API Call and return a map with status code and body of response.<br>
     * To recover the values, use the follow constants:
     * <ul><ul>
     *     <li> {@link #STATUS}: get the Status Code of API Call
     *     <li> {@link #BODY}:   get the body of the response
     * </ul></ul>
     * </p>
     * @param url     The url of API call
     * @param auth    The authorization of API
     * @param method  The method of API call (GET, POST, PATCH, etc...)
     * @param bodyReq The body of request
     * @param headers The list of headers
     * @return a {@link Map} with Status Code and the body of Response.
     */
    public static Map<String, String> call(String url, String auth, String method, String bodyReq, String[] headers) throws Exception {
        BodyPublisher bodyPublisher = BodyPublishers.ofString(bodyReq);

        // Create the request
        Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("Authorization", auth);

        // If there are headers and are paired, then add to the request
        if (headers.length > 0 && headers.length % 2 == 0)
            builder.headers(headers);

        // Set the method to call
        if (method.equalsIgnoreCase("GET"))
            builder.GET();
        else if (method.equalsIgnoreCase("POST"))
            builder.POST(bodyPublisher);
        else
            builder.method(method.toUpperCase(), bodyPublisher);

        // Send the request
        HttpResponse<String> res = client.send(builder.build(), BodyHandlers.ofString());

        // Return a new map with status code and body
        return Map.of(
                STATUS, "" + res.statusCode(),
                BODY, res.body()
        );
    }
}
