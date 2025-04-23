package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * Represents electricity price information in the Tibber API.
 * <p>
 * This class corresponds to the Price object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    private double total;
    private double energy;
    private double tax;
    private ZonedDateTime startsAt;
    private String currency;
    private String level;

    /**
     * Returns the total electricity price including energy, tax, and grid rent.
     *
     * @return the total price
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total electricity price.
     *
     * @param total the total price
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Returns the energy component of the electricity price.
     *
     * @return the energy price
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Sets the energy component of the electricity price.
     *
     * @param energy the energy price
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    /**
     * Returns the tax component of the electricity price.
     *
     * @return the tax amount
     */
    public double getTax() {
        return tax;
    }

    /**
     * Sets the tax component of the electricity price.
     *
     * @param tax the tax amount
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * Returns the time when this price becomes valid.
     *
     * @return the start time
     */
    public ZonedDateTime getStartsAt() {
        return startsAt;
    }

    /**
     * Sets the time when this price becomes valid.
     *
     * @param startsAt the start time
     */
    public void setStartsAt(ZonedDateTime startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * Returns the currency of the price.
     *
     * @return the currency code
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the price.
     *
     * @param currency the currency code
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the price level classification.
     * <p>
     * Possible values are:
     * <ul>
     *   <li>VERY_CHEAP - The price is much lower than normal</li>
     *   <li>CHEAP - The price is lower than normal</li>
     *   <li>NORMAL - The price is normal</li>
     *   <li>EXPENSIVE - The price is higher than normal</li>
     *   <li>VERY_EXPENSIVE - The price is much higher than normal</li>
     * </ul>
     *
     * @return the price level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the price level classification.
     *
     * @param level the price level
     */
    public void setLevel(String level) {
        this.level = level;
    }
}