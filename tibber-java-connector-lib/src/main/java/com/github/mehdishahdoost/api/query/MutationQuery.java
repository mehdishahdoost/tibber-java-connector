package com.github.mehdishahdoost.api.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mehdishahdoost.api.client.TibberApiClient;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/** Helpers for Tibber's supported GraphQL mutations. */
public class MutationQuery {
    private final TibberApiClient client;

    public MutationQuery(TibberApiClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    /** Updates the supplied home profile fields. Input keys follow UpdateHomeInput names. */
    public JsonNode updateHome(Map<String, Object> input) throws IOException {
        return execute("mutation UpdateHome($input: UpdateHomeInput!) { updateHome(input: $input) { id appNickname appAvatar size type numberOfResidents primaryHeatingSource hasVentilationSystem mainFuseSize } }", input,
                "updateHome");
    }

    /** Sends a notification to the user's registered Tibber app devices. */
    public JsonNode sendPushNotification(Map<String, Object> input) throws IOException {
        return execute("mutation SendPushNotification($input: PushNotificationInput!) { sendPushNotification(input: $input) { successful pushedToNumberOfDevices } }", input,
                "sendPushNotification");
    }

    private JsonNode execute(String query, Map<String, Object> input, String field) throws IOException {
        Objects.requireNonNull(input, "input must not be null");
        JsonNode response = client.executeQuery(query, Map.of("input", input));
        if (response.has("errors")) {
            throw new IOException("Tibber mutation failed: " + response.get("errors"));
        }
        JsonNode data = response.path("data").path(field);
        if (data.isMissingNode() || data.isNull()) {
            throw new IOException("Invalid Tibber mutation response for " + field);
        }
        return data;
    }
}
