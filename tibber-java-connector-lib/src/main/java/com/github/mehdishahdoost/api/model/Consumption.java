package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * Represents consumption data in the Tibber API.
 * <p>
 * This class corresponds to the Consumption object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Consumption {
    private ZonedDateTime from;
    private ZonedDateTime to;
    private Double unitPrice;
    private Double unitPriceVAT;
    private Double consumption;
    private String consumptionUnit;
    private Double cost;
    private String currency;

    /**
     * Returns the start time of the consumption period.
     *
     * @return the start time
     */
    public ZonedDateTime getFrom() {
        return from;
    }

    /**
     * Sets the start time of the consumption period.
     *
     * @param from the start time
     */
    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    /**
     * Returns the end time of the consumption period.
     *
     * @return the end time
     */
    public ZonedDateTime getTo() {
        return to;
    }

    /**
     * Sets the end time of the consumption period.
     *
     * @param to the end time
     */
    public void setTo(ZonedDateTime to) {
        this.to = to;
    }

    /**
     * Returns the unit price of electricity during the consumption period.
     *
     * @return the unit price
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of electricity during the consumption period.
     *
     * @param unitPrice the unit price
     */
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Returns the VAT component of the unit price.
     *
     * @return the unit price VAT
     */
    public Double getUnitPriceVAT() {
        return unitPriceVAT;
    }

    /**
     * Sets the VAT component of the unit price.
     *
     * @param unitPriceVAT the unit price VAT
     */
    public void setUnitPriceVAT(Double unitPriceVAT) {
        this.unitPriceVAT = unitPriceVAT;
    }

    /**
     * Returns the consumption amount in kWh.
     *
     * @return the consumption amount
     */
    public Double getConsumption() {
        return consumption;
    }

    /**
     * Sets the consumption amount in kWh.
     *
     * @param consumption the consumption amount
     */
    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    /**
     * Returns the consumption unit (typically "kWh").
     *
     * @return the consumption unit
     */
    public String getConsumptionUnit() {
        return consumptionUnit;
    }

    /**
     * Sets the consumption unit.
     *
     * @param consumptionUnit the consumption unit
     */
    public void setConsumptionUnit(String consumptionUnit) {
        this.consumptionUnit = consumptionUnit;
    }

    /**
     * Returns the cost of the consumption.
     *
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the consumption.
     *
     * @param cost the cost
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Returns the currency of the cost.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the cost.
     *
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}