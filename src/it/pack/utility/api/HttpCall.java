package it.pack.utility.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

public final class HttpCall {

    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Execute a Rest API Call and return a pair with status code and body of response.
     *
     * @param url     The url of API call
     * @param auth    The authorization of API
     * @param method  The method of API call (GET, POST, PATCH, etc...)
     * @param bodyReq The body of request
     * @param headers The list of headers
     * @return a {@link Map} with key -> Status Code and value -> Body of Response.
     */
    public static Map<Integer, String> call(String url, String auth, String method, String bodyReq, String[] headers) throws Exception {
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

        // Return a pair with status code and body
        return Map.of(res.statusCode(), res.body());
    }
}
