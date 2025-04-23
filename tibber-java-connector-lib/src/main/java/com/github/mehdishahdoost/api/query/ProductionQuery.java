package com.github.mehdishahdoost.api.query;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.mehdishahdoost.api.client.TibberApiClient;
import com.github.mehdishahdoost.api.model.Production;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query for retrieving production data from the Tibber API.
 * <p>
 * This class provides methods to retrieve historical production data
 * for a specific home, with options for different resolutions (hourly, daily, monthly, etc.).
 */
public class ProductionQuery {
    private static final String QUERY = "{\n" +
            "  viewer {\n" +
            "    home(id: \"%s\") {\n" +
            "      production(resolution: %s, from: \"%s\", to: \"%s\") {\n" +
            "        nodes {\n" +
            "          from\n" +
            "          to\n" +
            "          unitPrice\n" +
            "          unitPriceVAT\n" +
            "          production\n" +
            "          productionUnit\n" +
            "          profit\n" +
            "          currency\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    private final TibberApiClient client;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new production query with the specified Tibber API client.
     *
     * @param client the Tibber API client
     */
    public ProductionQuery(TibberApiClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();

        // Register JavaTimeModule for proper handling of Java 8 date/time types
        this.objectMapper.registerModule(new JavaTimeModule());

        // Configure ObjectMapper to preserve original timezone
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    /**
     * Retrieves hourly production data for the specified home and time period.
     *
     * @param homeId the ID of the home
     * @param from   the start time of the period
     * @param to     the end time of the period
     * @return the list of hourly production data
     * @throws IOException if an error occurs during the API request
     */
    public List<Production> getHourlyProduction(String homeId, ZonedDateTime from, ZonedDateTime to) throws IOException {
        return getProduction(homeId, "HOURLY", from, to);
    }

    /**
     * Retrieves daily production data for the specified home and time period.
     *
     * @param homeId the ID of the home
     * @param from   the start time of the period
     * @param to     the end time of the period
     * @return the list of daily production data
     * @throws IOException if an error occurs during the API request
     */
    public List<Production> getDailyProduction(String homeId, ZonedDateTime from, ZonedDateTime to) throws IOException {
        return getProduction(homeId, "DAILY", from, to);
    }

    /**
     * Retrieves monthly production data for the specified home and time period.
     *
     * @param homeId the ID of the home
     * @param from   the start time of the period
     * @param to     the end time of the period
     * @return the list of monthly production data
     * @throws IOException if an error occurs during the API request
     */
    public List<Production> getMonthlyProduction(String homeId, ZonedDateTime from, ZonedDateTime to) throws IOException {
        return getProduction(homeId, "MONTHLY", from, to);
    }

    /**
     * Retrieves annual production data for the specified home and time period.
     *
     * @param homeId the ID of the home
     * @param from   the start time of the period
     * @param to     the end time of the period
     * @return the list of annual production data
     * @throws IOException if an error occurs during the API request
     */
    public List<Production> getAnnualProduction(String homeId, ZonedDateTime from, ZonedDateTime to) throws IOException {
        return getProduction(homeId, "ANNUAL", from, to);
    }

    private List<Production> getProduction(String homeId, String resolution, ZonedDateTime from, ZonedDateTime to) throws IOException {
        String formattedQuery = String.format(QUERY, homeId, resolution, formatDateTime(from), formatDateTime(to));
        JsonNode response = client.executeQuery(formattedQuery, new HashMap<>());

        if (response.has("errors")) {
            throw new IOException("Error retrieving production data: " + response.get("errors").toString());
        }

        if (!response.has("data") || 
            !response.get("data").has("viewer") || 
            !response.get("data").get("viewer").has("home") ||
            response.get("data").get("viewer").get("home").isNull()) {
            throw new IOException("Home with ID " + homeId + " not found");
        }

        JsonNode home = response.get("data").get("viewer").get("home");

        if (!home.has("production") || 
            home.get("production").isNull() ||
            !home.get("production").has("nodes")) {
            throw new IOException("Production data not available for home with ID " + homeId);
        }

        JsonNode nodes = home.get("production").get("nodes");
        List<Production> productionList = new ArrayList<>();

        if (nodes.isArray()) {
            for (JsonNode node : nodes) {
                productionList.add(objectMapper.treeToValue(node, Production.class));
            }
        }

        return productionList;
    }

    private String formatDateTime(ZonedDateTime dateTime) {
        return dateTime.toString();
    }
}