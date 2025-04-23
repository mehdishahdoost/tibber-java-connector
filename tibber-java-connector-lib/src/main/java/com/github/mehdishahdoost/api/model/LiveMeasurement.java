package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

/**
 * Represents real-time measurement data from the Tibber API.
 * <p>
 * This class corresponds to the LiveMeasurement object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveMeasurement {
    private ZonedDateTime timestamp;
    private Double power;
    private Double lastMeterConsumption;
    private Double accumulatedConsumption;
    private Double accumulatedProduction;
    private Double accumulatedCost;
    private Double accumulatedReward;
    private String currency;
    private Double minPower;
    private Double averagePower;
    private Double maxPower;
    private Double powerProduction;
    private Double powerFactor;
    private Double voltagePhase1;
    private Double voltagePhase2;
    private Double voltagePhase3;
    private Double currentPhase1;
    private Double currentPhase2;
    private Double currentPhase3;
    private Double signalStrength;

    /**
     * Returns the timestamp of the measurement.
     *
     * @return the timestamp
     */
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the measurement.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the power consumption in watts.
     *
     * @return the power consumption
     */
    public Double getPower() {
        return power;
    }

    /**
     * Sets the power consumption in watts.
     *
     * @param power the power consumption
     */
    public void setPower(Double power) {
        this.power = power;
    }

    /**
     * Returns the last meter consumption in kWh.
     *
     * @return the last meter consumption
     */
    public Double getLastMeterConsumption() {
        return lastMeterConsumption;
    }

    /**
     * Sets the last meter consumption in kWh.
     *
     * @param lastMeterConsumption the last meter consumption
     */
    public void setLastMeterConsumption(Double lastMeterConsumption) {
        this.lastMeterConsumption = lastMeterConsumption;
    }

    /**
     * Returns the accumulated consumption in kWh.
     *
     * @return the accumulated consumption
     */
    public Double getAccumulatedConsumption() {
        return accumulatedConsumption;
    }

    /**
     * Sets the accumulated consumption in kWh.
     *
     * @param accumulatedConsumption the accumulated consumption
     */
    public void setAccumulatedConsumption(Double accumulatedConsumption) {
        this.accumulatedConsumption = accumulatedConsumption;
    }

    /**
     * Returns the accumulated production in kWh.
     *
     * @return the accumulated production
     */
    public Double getAccumulatedProduction() {
        return accumulatedProduction;
    }

    /**
     * Sets the accumulated production in kWh.
     *
     * @param accumulatedProduction the accumulated production
     */
    public void setAccumulatedProduction(Double accumulatedProduction) {
        this.accumulatedProduction = accumulatedProduction;
    }

    /**
     * Returns the accumulated cost.
     *
     * @return the accumulated cost
     */
    public Double getAccumulatedCost() {
        return accumulatedCost;
    }

    /**
     * Sets the accumulated cost.
     *
     * @param accumulatedCost the accumulated cost
     */
    public void setAccumulatedCost(Double accumulatedCost) {
        this.accumulatedCost = accumulatedCost;
    }

    /**
     * Returns the accumulated reward.
     *
     * @return the accumulated reward
     */
    public Double getAccumulatedReward() {
        return accumulatedReward;
    }

    /**
     * Sets the accumulated reward.
     *
     * @param accumulatedReward the accumulated reward
     */
    public void setAccumulatedReward(Double accumulatedReward) {
        this.accumulatedReward = accumulatedReward;
    }

    /**
     * Returns the currency of the cost and reward.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the cost and reward.
     *
     * @param currency the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Returns the minimum power consumption in watts.
     *
     * @return the minimum power
     */
    public Double getMinPower() {
        return minPower;
    }

    /**
     * Sets the minimum power consumption in watts.
     *
     * @param minPower the minimum power
     */
    public void setMinPower(Double minPower) {
        this.minPower = minPower;
    }

    /**
     * Returns the average power consumption in watts.
     *
     * @return the average power
     */
    public Double getAveragePower() {
        return averagePower;
    }

    /**
     * Sets the average power consumption in watts.
     *
     * @param averagePower the average power
     */
    public void setAveragePower(Double averagePower) {
        this.averagePower = averagePower;
    }

    /**
     * Returns the maximum power consumption in watts.
     *
     * @return the maximum power
     */
    public Double getMaxPower() {
        return maxPower;
    }

    /**
     * Sets the maximum power consumption in watts.
     *
     * @param maxPower the maximum power
     */
    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * Returns the power production in watts.
     *
     * @return the power production
     */
    public Double getPowerProduction() {
        return powerProduction;
    }

    /**
     * Sets the power production in watts.
     *
     * @param powerProduction the power production
     */
    public void setPowerProduction(Double powerProduction) {
        this.powerProduction = powerProduction;
    }

    /**
     * Returns the power factor.
     *
     * @return the power factor
     */
    public Double getPowerFactor() {
        return powerFactor;
    }

    /**
     * Sets the power factor.
     *
     * @param powerFactor the power factor
     */
    public void setPowerFactor(Double powerFactor) {
        this.powerFactor = powerFactor;
    }

    /**
     * Returns the voltage of phase 1 in volts.
     *
     * @return the voltage of phase 1
     */
    public Double getVoltagePhase1() {
        return voltagePhase1;
    }

    /**
     * Sets the voltage of phase 1 in volts.
     *
     * @param voltagePhase1 the voltage of phase 1
     */
    public void setVoltagePhase1(Double voltagePhase1) {
        this.voltagePhase1 = voltagePhase1;
    }

    /**
     * Returns the voltage of phase 2 in volts.
     *
     * @return the voltage of phase 2
     */
    public Double getVoltagePhase2() {
        return voltagePhase2;
    }

    /**
     * Sets the voltage of phase 2 in volts.
     *
     * @param voltagePhase2 the voltage of phase 2
     */
    public void setVoltagePhase2(Double voltagePhase2) {
        this.voltagePhase2 = voltagePhase2;
    }

    /**
     * Returns the voltage of phase 3 in volts.
     *
     * @return the voltage of phase 3
     */
    public Double getVoltagePhase3() {
        return voltagePhase3;
    }

    /**
     * Sets the voltage of phase 3 in volts.
     *
     * @param voltagePhase3 the voltage of phase 3
     */
    public void setVoltagePhase3(Double voltagePhase3) {
        this.voltagePhase3 = voltagePhase3;
    }

    /**
     * Returns the current of phase 1 in amperes.
     *
     * @return the current of phase 1
     */
    public Double getCurrentPhase1() {
        return currentPhase1;
    }

    /**
     * Sets the current of phase 1 in amperes.
     *
     * @param currentPhase1 the current of phase 1
     */
    public void setCurrentPhase1(Double currentPhase1) {
        this.currentPhase1 = currentPhase1;
    }

    /**
     * Returns the current of phase 2 in amperes.
     *
     * @return the current of phase 2
     */
    public Double getCurrentPhase2() {
        return currentPhase2;
    }

    /**
     * Sets the current of phase 2 in amperes.
     *
     * @param currentPhase2 the current of phase 2
     */
    public void setCurrentPhase2(Double currentPhase2) {
        this.currentPhase2 = currentPhase2;
    }

    /**
     * Returns the current of phase 3 in amperes.
     *
     * @return the current of phase 3
     */
    public Double getCurrentPhase3() {
        return currentPhase3;
    }

    /**
     * Sets the current of phase 3 in amperes.
     *
     * @param currentPhase3 the current of phase 3
     */
    public void setCurrentPhase3(Double currentPhase3) {
        this.currentPhase3 = currentPhase3;
    }

    /**
     * Returns the signal strength in percentage.
     *
     * @return the signal strength
     */
    public Double getSignalStrength() {
        return signalStrength;
    }

    /**
     * Sets the signal strength in percentage.
     *
     * @param signalStrength the signal strength
     */
    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }
}