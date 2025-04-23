package com.github.mehdishahdoost.api.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mehdishahdoost.api.model.LiveMeasurement;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Subscription for receiving real-time measurement data from the Tibber API.
 * <p>
 * This class handles the WebSocket connection to the Tibber API and processes
 * the subscription data. For more information about the Tibber API, see:
 * <a href="https://developer.tibber.com/docs/guides/calling-api">Tibber API Documentation</a>
 */
public class LiveMeasurementSubscription {
    private static final String SUBSCRIPTION_ENDPOINT = "wss://api.tibber.com/v1-beta/gql/subscriptions";
    private static final String SUBSCRIPTION_QUERY = "subscription {\n" +
            "  liveMeasurement(homeId: \"%s\") {\n" +
            "    timestamp\n" +
            "    power\n" +
            "    lastMeterConsumption\n" +
            "    accumulatedConsumption\n" +
            "    accumulatedProduction\n" +
            "    accumulatedCost\n" +
            "    accumulatedReward\n" +
            "    currency\n" +
            "    minPower\n" +
            "    averagePower\n" +
            "    maxPower\n" +
            "    powerProduction\n" +
            "    powerFactor\n" +
            "    voltagePhase1\n" +
            "    voltagePhase2\n" +
            "    voltagePhase3\n" +
            "    currentPhase1\n" +
            "    currentPhase2\n" +
            "    currentPhase3\n" +
            "    signalStrength\n" +
            "  }\n" +
            "}";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String accessToken;
    private final String homeId;
    private final SubscriptionHandler handler;
    private final String subscriptionId;
    
    private WebSocket webSocket;
    private boolean connected = false;

    /**
     * Creates a new live measurement subscription for the specified home.
     *
     * @param accessToken the Tibber API access token
     * @param homeId      the ID of the home to subscribe to
     * @param handler     the handler for subscription events
     */
    public LiveMeasurementSubscription(String accessToken, String homeId, SubscriptionHandler handler) {
        this(accessToken, homeId, handler, new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS) // No timeout for WebSocket connections
                .writeTimeout(10, TimeUnit.SECONDS)
                .pingInterval(30, TimeUnit.SECONDS) // Keep connection alive
                .build());
    }

    /**
     * Creates a new live measurement subscription with a custom HTTP client.
     *
     * @param accessToken the Tibber API access token
     * @param homeId      the ID of the home to subscribe to
     * @param handler     the handler for subscription events
     * @param httpClient  the HTTP client to use for the WebSocket connection
     */
    public LiveMeasurementSubscription(String accessToken, String homeId, SubscriptionHandler handler, OkHttpClient httpClient) {
        this.accessToken = Objects.requireNonNull(accessToken, "Access token must not be null");
        this.homeId = Objects.requireNonNull(homeId, "Home ID must not be null");
        this.handler = Objects.requireNonNull(handler, "Handler must not be null");
        this.httpClient = Objects.requireNonNull(httpClient, "HTTP client must not be null");
        this.objectMapper = new ObjectMapper();
        this.subscriptionId = UUID.randomUUID().toString();
    }

    /**
     * Starts the subscription to real-time measurement data.
     * <p>
     * This method establishes a WebSocket connection to the Tibber API and
     * initiates the subscription. The handler will be notified of connection
     * status changes and incoming measurement data.
     */
    public void start() {
        if (connected) {
            return;
        }

        Request request = new Request.Builder()
                .url(SUBSCRIPTION_ENDPOINT)
                .header("Authorization", "Bearer " + accessToken)
                .header("Sec-WebSocket-Protocol", "graphql-ws")
                .build();

        webSocket = httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Send connection initialization message
                try {
                    String initMessage = objectMapper.writeValueAsString(Map.of(
                            "type", "connection_init",
                            "payload", Map.of()
                    ));
                    webSocket.send(initMessage);
                } catch (IOException e) {
                    handler.onError(e);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JsonNode message = objectMapper.readTree(text);
                    String type = message.has("type") ? message.get("type").asText() : "";

                    switch (type) {
                        case "connection_ack":
                            // Connection acknowledged, send subscription request
                            String subscriptionMessage = objectMapper.writeValueAsString(Map.of(
                                    "id", subscriptionId,
                                    "type", "start",
                                    "payload", Map.of(
                                            "query", String.format(SUBSCRIPTION_QUERY, homeId),
                                            "variables", Map.of()
                                    )
                            ));
                            webSocket.send(subscriptionMessage);
                            break;

                        case "data":
                            // Process subscription data
                            if (message.has("payload") && 
                                message.get("payload").has("data") && 
                                message.get("payload").get("data").has("liveMeasurement")) {
                                
                                JsonNode liveMeasurementNode = message.get("payload").get("data").get("liveMeasurement");
                                LiveMeasurement measurement = objectMapper.treeToValue(liveMeasurementNode, LiveMeasurement.class);
                                handler.onMeasurement(measurement);
                            }
                            break;

                        case "complete":
                            // Subscription completed
                            if (!connected) {
                                connected = true;
                                handler.onConnected();
                            }
                            break;

                        case "error":
                            // Subscription error
                            String errorMessage = message.has("payload") ? message.get("payload").toString() : "Unknown error";
                            handler.onError(new IOException("Subscription error: " + errorMessage));
                            break;

                        case "ka":
                            // Keep alive message, ignore
                            break;

                        default:
                            // Unknown message type
                            handler.onError(new IOException("Unknown message type: " + type));
                            break;
                    }
                } catch (Exception e) {
                    handler.onError(e);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                connected = false;
                handler.onError(t);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                connected = false;
                handler.onDisconnected(reason);
            }
        });
    }

    /**
     * Stops the subscription to real-time measurement data.
     * <p>
     * This method closes the WebSocket connection and terminates the subscription.
     */
    public void stop() {
        if (webSocket != null) {
            try {
                // Send stop message
                String stopMessage = objectMapper.writeValueAsString(Map.of(
                        "id", subscriptionId,
                        "type", "stop"
                ));
                webSocket.send(stopMessage);
                
                // Close WebSocket with normal closure status
                webSocket.close(1000, "Subscription stopped by client");
                webSocket = null;
                connected = false;
            } catch (IOException e) {
                handler.onError(e);
            }
        }
    }

    /**
     * Returns whether the subscription is currently connected.
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }
}