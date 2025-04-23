package com.github.mehdishahdoost.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mehdishahdoost.api.model.LiveMeasurement;
import com.github.mehdishahdoost.api.subscription.LiveMeasurementSubscription;
import com.github.mehdishahdoost.api.subscription.SubscriptionHandler;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Client for interacting with the Tibber GraphQL API.
 * <p>
 * This client handles authentication and execution of GraphQL queries against
 * the Tibber API. For more information about the Tibber API, see:
 * <a href="https://developer.tibber.com/docs/overview">Tibber API Documentation</a>
 */
public class TibberApiClient {
    private static final String API_ENDPOINT = "https://api.tibber.com/v1-beta/gql";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String accessToken;

    /**
     * Creates a new Tibber API client with the specified access token.
     *
     * @param accessToken the Tibber API access token
     */
    public TibberApiClient(String accessToken) {
        this(accessToken, new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build());
    }

    /**
     * Creates a new Tibber API client with the specified access token and HTTP client.
     *
     * @param accessToken the Tibber API access token
     * @param httpClient  the HTTP client to use for API requests
     */
    public TibberApiClient(String accessToken, OkHttpClient httpClient) {
        this.accessToken = Objects.requireNonNull(accessToken, "Access token must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "HTTP client must not be null");
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Executes a GraphQL query against the Tibber API.
     *
     * @param query     the GraphQL query to execute
     * @param variables the variables to include with the query (may be null)
     * @return the JSON response from the API
     * @throws IOException if an error occurs during the HTTP request
     */
    public JsonNode executeQuery(String query, Map<String, Object> variables) throws IOException {
        RequestBody body = createRequestBody(query, variables);
        Request request = createRequest(body);

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response from Tibber API");
            }

            return objectMapper.readTree(responseBody.string());
        }
    }

    private RequestBody createRequestBody(String query, Map<String, Object> variables) throws IOException {
        return RequestBody.create(
                objectMapper.writeValueAsString(Map.of(
                        "query", query,
                        "variables", variables != null ? variables : Map.of()
                )),
                JSON
        );
    }

    private Request createRequest(RequestBody body) {
        return new Request.Builder()
                .url(API_ENDPOINT)
                .post(body)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();
    }

    /**
     * Creates a subscription for real-time measurement data for the specified home.
     * <p>
     * This method establishes a WebSocket connection to the Tibber API and
     * subscribes to real-time measurement data for the specified home. The handler
     * will be notified of connection status changes and incoming measurement data.
     *
     * @param homeId  the ID of the home to subscribe to
     * @param handler the handler for subscription events
     * @return a subscription object that can be used to manage the subscription
     */
    public LiveMeasurementSubscription subscribeToLiveMeasurements(String homeId, SubscriptionHandler handler) {
        LiveMeasurementSubscription subscription = new LiveMeasurementSubscription(accessToken, homeId, handler, httpClient);
        subscription.start();
        return subscription;
    }
}