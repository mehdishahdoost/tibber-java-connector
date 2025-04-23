package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String firstName;
    private String lastName;
    private String accountType;
    private String email;
    private List<Home> homes;

    /**
     * Returns the unique identifier for the user.
     *
     * @return the user ID
     */
    @JsonProperty("id")
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userId the user ID
     */
    @JsonProperty("id")
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

    /**
     * Returns the first name of the user.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the account type of the user.
     *
     * @return the account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the account type of the user.
     *
     * @param accountType the account type
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address
     */
    public void setEmail(String email) {
        this.email = email;
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