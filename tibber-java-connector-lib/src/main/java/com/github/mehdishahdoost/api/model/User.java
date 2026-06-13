package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Represents a Tibber user.
 * <p>
 * This class corresponds to the User object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String userId;
    private String name;
    private List<String> accountType;
    private String websocketSubscriptionUrl;
    private List<Home> homes;

    /**
     * Returns the unique identifier for the user.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userId the user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the full name of the user.
     *
     * @return the user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     *
     * @param name the user's full name
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    public String getWebsocketSubscriptionUrl() {
        return websocketSubscriptionUrl;
    }

    /**
     * Sets the WebSocket subscription URL for real-time data.
     *
     * @param websocketSubscriptionUrl the WebSocket subscription URL
     */
    public void setWebsocketSubscriptionUrl(String websocketSubscriptionUrl) {
        this.websocketSubscriptionUrl = websocketSubscriptionUrl;
    }

    /**
     * Returns the homes associated with the user.
     *
     * @return the list of homes
     */
    public List<Home> getHomes() {
        return homes;
    }

    /**
     * Sets the homes associated with the user.
     *
     * @param homes the list of homes
     */
    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }
}