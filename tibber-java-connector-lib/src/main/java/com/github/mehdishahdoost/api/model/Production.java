package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * Represents production data in the Tibber API.
 * <p>
 * This class corresponds to the Production object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Production {
    private ZonedDateTime from;
    private ZonedDateTime to;
    private Double unitPrice;
    private Double unitPriceVAT;
    private Double production;
    private String productionUnit;
    private Double profit;
    private String currency;

    /**
     * Returns the start time of the production period.
     *
     * @return the start time
     */
    public ZonedDateTime getFrom() {
        return from;
    }

    /**
     * Sets the start time of the production period.
     *
     * @param from the start time
     */
    public void setFrom(ZonedDateTime from) {
        this.from = from;
    }

    /**
     * Returns the end time of the production period.
     *
     * @return the end time
     */
    public ZonedDateTime getTo() {
        return to;
    }

    /**
     * Sets the end time of the production period.
     *
     * @param to the end time
     */
    public void setTo(ZonedDateTime to) {
        this.to = to;
    }

    /**
     * Returns the unit price of electricity during the production period.
     *
     * @return the unit price
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of electricity during the production period.
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
     * Returns the production amount in kWh.
     *
     * @return the production amount
     */
    public Double getProduction() {
        return production;
    }

    /**
     * Sets the production amount in kWh.
     *
     * @param production the production amount
     */
    public void setProduction(Double production) {
        this.production = production;
    }

    /**
     * Returns the production unit (typically "kWh").
     *
     * @return the production unit
     */
    public String getProductionUnit() {
        return productionUnit;
    }

    /**
     * Sets the production unit.
     *
     * @param productionUnit the production unit
     */
    public void setProductionUnit(String productionUnit) {
        this.productionUnit = productionUnit;
    }

    /**
     * Returns the profit from the production.
     *
     * @return the profit
     */
    public Double getProfit() {
        return profit;
    }

    /**
     * Sets the profit from the production.
     *
     * @param profit the profit
     */
    public void setProfit(Double profit) {
        this.profit = profit;
    }

    /**
     * Returns the currency of the profit.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the profit.
     *
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}