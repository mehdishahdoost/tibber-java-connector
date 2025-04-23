package com.github.mehdishahdoost.api.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mehdishahdoost.api.client.TibberApiClient;
import com.github.mehdishahdoost.api.model.User;

import java.io.IOException;
import java.util.Collections;

/**
 * Query for retrieving user information from the Tibber API.
 * <p>
 * This class provides methods to retrieve information about the authenticated user,
 * including their homes and associated data.
 */
public class UserQuery {
    private static final String QUERY = "{\n" +
            "  viewer {\n" +
            "    userId: id\n" +
            "    name\n" +
            "    firstName\n" +
            "    lastName\n" +
            "    accountType\n" +
            "    email\n" +
            "    homes {\n" +
            "      homeId: id\n" +
            "      timeZone\n" +
            "      address {\n" +
            "        address1\n" +
            "        address2\n" +
            "        address3\n" +
            "        city\n" +
            "        postalCode\n" +
            "        country\n" +
            "        latitude\n" +
            "        longitude\n" +
            "      }\n" +
            "      meteringPointData {\n" +
            "        consumptionEan\n" +
            "        gridCompany\n" +
            "        gridAreaCode\n" +
            "        priceAreaCode\n" +
            "        productionEan\n" +
            "        energyTaxType\n" +
            "        vatType\n" +
            "      }\n" +
            "      currentSubscription {\n" +
            "        subscriptionId: id\n" +
            "        status\n" +
            "        priceInfo {\n" +
            "          current {\n" +
            "            total\n" +
            "            energy\n" +
            "            tax\n" +
            "            startsAt\n" +
            "            currency\n" +
            "            level\n" +
            "          }\n" +
            "          today {\n" +
            "            total\n" +
            "            energy\n" +
            "            tax\n" +
            "            startsAt\n" +
            "            currency\n" +
            "            level\n" +
            "          }\n" +
            "          tomorrow {\n" +
            "            total\n" +
            "            energy\n" +
            "            tax\n" +
            "            startsAt\n" +
            "            currency\n" +
            "            level\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "      features {\n" +
            "        realTimeConsumptionEnabled\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private final TibberApiClient client;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new user query with the specified Tibber API client.
     *
     * @param client the Tibber API client
     */
    public UserQuery(TibberApiClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves information about the authenticated user.
     *
     * @return the user information
     * @throws IOException if an error occurs during the API request
     */
    public User execute() throws IOException {
        JsonNode response = client.executeQuery(QUERY, Collections.emptyMap());

        if (response.has("errors")) {
            throw new IOException("Error retrieving user information: " + response.get("errors").toString());
        }

        if (!response.has("data") || !response.get("data").has("viewer")) {
            throw new IOException("Invalid response format from Tibber API");
        }

        return objectMapper.treeToValue(response.get("data").get("viewer"), User.class);
    }
}