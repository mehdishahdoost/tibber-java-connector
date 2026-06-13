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
    private static final String SUBSCRIPTION_QUERY = "subscription {\n" +
            "  liveMeasurement(homeId: \"%s\") {\n" +
            "    accumulatedConsumption\n" +
            "    accumulatedConsumptionLastHour\n" +
            "    accumulatedCost\n" +
            "    accumulatedProduction\n" +
            "    accumulatedProductionLastHour\n" +
            "    accumulatedReward\n" +
            "    averagePower\n" +
            "    currency\n" +
            "    currentL1\n" +
            "    currentL2\n" +
            "    currentL3\n" +
            "    lastMeterConsumption\n" +
            "    lastMeterProduction\n" +
            "    maxPower\n" +
            "    minPower\n" +
            "    power\n" +
            "    powerFactor\n" +
            "    powerProduction\n" +
            "    powerReactive\n" +
            "    signalStrength\n" +
            "    timestamp\n" +
            "    voltagePhase1\n" +
            "    voltagePhase2\n" +
            "    voltagePhase3\n" +
            "  }\n" +
            "}";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String subscriptionUrl;
    private final String accessToken;
    private final String homeId;
    private final SubscriptionHandler handler;
    private final String subscriptionId;
    
    private WebSocket webSocket;
    private boolean connected = false;

    /**
     * Creates a new live measurement subscription for the specified home.
     *
     * @param subscriptionUrl the WebSocket subscription URL (from viewer.websocketSubscriptionUrl)
     * @param accessToken     the Tibber API access token
     * @param homeId          the ID of the home to subscribe to
     * @param handler         the handler for subscription events
     */
    public LiveMeasurementSubscription(String subscriptionUrl, String accessToken, String homeId, SubscriptionHandler handler) {
        this(subscriptionUrl, accessToken, homeId, handler, new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .pingInterval(30, TimeUnit.SECONDS)
                .build());
    }

    /**
     * Creates a new live measurement subscription with a custom HTTP client.
     *
     * @param subscriptionUrl the WebSocket subscription URL (from viewer.websocketSubscriptionUrl)
     * @param accessToken     the Tibber API access token
     * @param homeId          the ID of the home to subscribe to
     * @param handler         the handler for subscription events
     * @param httpClient      the HTTP client to use for the WebSocket connection
     */
    public LiveMeasurementSubscription(String subscriptionUrl, String accessToken, String homeId, SubscriptionHandler handler, OkHttpClient httpClient) {
        this.subscriptionUrl = Objects.requireNonNull(subscriptionUrl, "Subscription URL must not be null");
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
                .url(subscriptionUrl)
                .header("Sec-WebSocket-Protocol", "graphql-transport-ws")
                .build();

        webSocket = httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                try {
                    String initMessage = objectMapper.writeValueAsString(Map.of(
                            "type", "connection_init",
                            "payload", Map.of("token", accessToken)
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
                            String subscriptionMessage = objectMapper.writeValueAsString(Map.of(
                                    "id", subscriptionId,
                                    "type", "subscribe",
                                    "payload", Map.of(
                                            "query", String.format(SUBSCRIPTION_QUERY, homeId),
                                            "variables", Map.of()
                                    )
                            ));
                            webSocket.send(subscriptionMessage);
                            break;

                        case "next":
                            if (message.has("payload") && 
                                message.get("payload").has("data") && 
                                message.get("payload").get("data").has("liveMeasurement")) {
                                
                                JsonNode liveMeasurementNode = message.get("payload").get("data").get("liveMeasurement");
                                LiveMeasurement measurement = objectMapper.treeToValue(liveMeasurementNode, LiveMeasurement.class);
                                handler.onMeasurement(measurement);
                            }
                            if (!connected) {
                                connected = true;
                                handler.onConnected();
                            }
                            break;

                        case "error":
                            String errorMessage = message.has("payload") ? message.get("payload").toString() : "Unknown error";
                            handler.onError(new IOException("Subscription error: " + errorMessage));
                            break;

                        case "complete":
                            break;

                        case "ka":
                            break;

                        default:
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
                String stopMessage = objectMapper.writeValueAsString(Map.of(
                        "id", subscriptionId,
                        "type", "unsubscribe"
                ));
                webSocket.send(stopMessage);
                
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