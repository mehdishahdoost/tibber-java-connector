package com.github.mehdishahdoost.api.query;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mehdishahdoost.api.client.TibberApiClient;
import com.github.mehdishahdoost.api.model.Price;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query for retrieving price information from the Tibber API.
 * <p>
 * This class provides methods to retrieve current and future electricity prices
 * for a specific home.
 */
public class PriceQuery {
    private static final String QUERY = "{\n" +
            "  viewer {\n" +
            "    home(id: \"%s\") {\n" +
            "      currentSubscription {\n" +
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
            "    }\n" +
            "  }\n" +
            "}";

    private final TibberApiClient client;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new price query with the specified Tibber API client.
     *
     * @param client the Tibber API client
     */
    public PriceQuery(TibberApiClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();

        // Register JavaTimeModule for proper handling of Java 8 date/time types
        this.objectMapper.registerModule(new JavaTimeModule());

        // Configure ObjectMapper to preserve original timezone
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    /**
     * Retrieves the current electricity price for the specified home.
     *
     * @param homeId the ID of the home
     * @return the current price information
     * @throws IOException if an error occurs during the API request
     */
    public Price getCurrentPrice(String homeId) throws IOException {
        JsonNode response = executeQuery(homeId);
        JsonNode priceInfo = getPriceInfoNode(response, homeId);

        if (!priceInfo.has("current")) {
            throw new IOException("Current price information not available");
        }

        return objectMapper.treeToValue(priceInfo.get("current"), Price.class);
    }

    /**
     * Retrieves the electricity prices for today for the specified home.
     *
     * @param homeId the ID of the home
     * @return the list of prices for today
     * @throws IOException if an error occurs during the API request
     */
    public List<Price> getTodayPrices(String homeId) throws IOException {
        JsonNode response = executeQuery(homeId);
        JsonNode priceInfo = getPriceInfoNode(response, homeId);

        if (!priceInfo.has("today")) {
            throw new IOException("Today's price information not available");
        }

        return convertToPriceList(priceInfo.get("today"));
    }

    /**
     * Retrieves the electricity prices for tomorrow for the specified home.
     *
     * @param homeId the ID of the home
     * @return the list of prices for tomorrow, or an empty list if not available
     * @throws IOException if an error occurs during the API request
     */
    public List<Price> getTomorrowPrices(String homeId) throws IOException {
        JsonNode response = executeQuery(homeId);
        JsonNode priceInfo = getPriceInfoNode(response, homeId);

        if (!priceInfo.has("tomorrow") || priceInfo.get("tomorrow").isNull()) {
            return new ArrayList<>();
        }

        return convertToPriceList(priceInfo.get("tomorrow"));
    }

    private JsonNode executeQuery(String homeId) throws IOException {
        String formattedQuery = String.format(QUERY, homeId);
        return client.executeQuery(formattedQuery, new HashMap<>());
    }

    private JsonNode getPriceInfoNode(JsonNode response, String homeId) throws IOException {
        if (response.has("errors")) {
            throw new IOException("Error retrieving price information: " + response.get("errors").toString());
        }

        if (!response.has("data") || 
            !response.get("data").has("viewer") || 
            !response.get("data").get("viewer").has("home") ||
            response.get("data").get("viewer").get("home").isNull()) {
            throw new IOException("Home with ID " + homeId + " not found");
        }

        JsonNode home = response.get("data").get("viewer").get("home");

        if (!home.has("currentSubscription") || 
            home.get("currentSubscription").isNull() ||
            !home.get("currentSubscription").has("priceInfo")) {
            throw new IOException("Price information not available for home with ID " + homeId);
        }

        return home.get("currentSubscription").get("priceInfo");
    }

    private List<Price> convertToPriceList(JsonNode priceNode) throws IOException {
        List<Price> prices = new ArrayList<>();

        if (priceNode.isArray()) {
            for (JsonNode node : priceNode) {
                prices.add(objectMapper.treeToValue(node, Price.class));
            }
        }

        return prices;
    }
}