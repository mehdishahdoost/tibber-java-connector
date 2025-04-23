package com.github.mehdishahdoost.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a home in the Tibber API.
 * <p>
 * This class corresponds to the Home object in the Tibber GraphQL API.
 * For more information, see: <a href="https://developer.tibber.com/docs/reference">Tibber API Reference</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Home {
    private String homeId;
    private String timeZone;
    private Address address;
    private MeteringPointData meteringPointData;
    private Subscription currentSubscription;
    private Features features;

    /**
     * Returns the unique identifier for the home.
     *
     * @return the home ID
     */
    @JsonProperty("id")
    public String getHomeId() {
        return homeId;
    }

    /**
     * Sets the unique identifier for the home.
     *
     * @param homeId the home ID
     */
    @JsonProperty("id")
    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    /**
     * Returns the time zone of the home.
     *
     * @return the time zone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the time zone of the home.
     *
     * @param timeZone the time zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Returns the address of the home.
     *
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the address of the home.
     *
     * @param address the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Returns the metering point data for the home.
     *
     * @return the metering point data
     */
    public MeteringPointData getMeteringPointData() {
        return meteringPointData;
    }

    /**
     * Sets the metering point data for the home.
     *
     * @param meteringPointData the metering point data
     */
    public void setMeteringPointData(MeteringPointData meteringPointData) {
        this.meteringPointData = meteringPointData;
    }

    /**
     * Returns the current subscription for the home.
     *
     * @return the current subscription
     */
    public Subscription getCurrentSubscription() {
        return currentSubscription;
    }

    /**
     * Sets the current subscription for the home.
     *
     * @param currentSubscription the current subscription
     */
    public void setCurrentSubscription(Subscription currentSubscription) {
        this.currentSubscription = currentSubscription;
    }

    /**
     * Returns the features available for the home.
     *
     * @return the features
     */
    public Features getFeatures() {
        return features;
    }

    /**
     * Sets the features available for the home.
     *
     * @param features the features
     */
    public void setFeatures(Features features) {
        this.features = features;
    }

    /**
     * Represents an address in the Tibber API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String address1;
        private String address2;
        private String address3;
        private String city;
        private String postalCode;
        private String country;
        private double latitude;
        private double longitude;

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    /**
     * Represents metering point data in the Tibber API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MeteringPointData {
        private String consumptionEan;
        private String gridCompany;
        private String gridAreaCode;
        private String priceAreaCode;
        private String productionEan;
        private String energyTaxType;
        private String vatType;

        public String getConsumptionEan() {
            return consumptionEan;
        }

        public void setConsumptionEan(String consumptionEan) {
            this.consumptionEan = consumptionEan;
        }

        public String getGridCompany() {
            return gridCompany;
        }

        public void setGridCompany(String gridCompany) {
            this.gridCompany = gridCompany;
        }

        public String getGridAreaCode() {
            return gridAreaCode;
        }

        public void setGridAreaCode(String gridAreaCode) {
            this.gridAreaCode = gridAreaCode;
        }

        public String getPriceAreaCode() {
            return priceAreaCode;
        }

        public void setPriceAreaCode(String priceAreaCode) {
            this.priceAreaCode = priceAreaCode;
        }

        public String getProductionEan() {
            return productionEan;
        }

        public void setProductionEan(String productionEan) {
            this.productionEan = productionEan;
        }

        public String getEnergyTaxType() {
            return energyTaxType;
        }

        public void setEnergyTaxType(String energyTaxType) {
            this.energyTaxType = energyTaxType;
        }

        public String getVatType() {
            return vatType;
        }

        public void setVatType(String vatType) {
            this.vatType = vatType;
        }
    }

    /**
     * Represents a subscription in the Tibber API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Subscription {
        private String subscriptionId;
        private String status;
        private PriceInfo priceInfo;

        @JsonProperty("id")
        public String getSubscriptionId() {
            return subscriptionId;
        }

        @JsonProperty("id")
        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PriceInfo getPriceInfo() {
            return priceInfo;
        }

        public void setPriceInfo(PriceInfo priceInfo) {
            this.priceInfo = priceInfo;
        }
    }

    /**
     * Represents price information in the Tibber API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceInfo {
        private Price current;
        private List<Price> today;
        private List<Price> tomorrow;

        public Price getCurrent() {
            return current;
        }

        public void setCurrent(Price current) {
            this.current = current;
        }

        public List<Price> getToday() {
            return today;
        }

        public void setToday(List<Price> today) {
            this.today = today;
        }

        public List<Price> getTomorrow() {
            return tomorrow;
        }

        public void setTomorrow(List<Price> tomorrow) {
            this.tomorrow = tomorrow;
        }
    }

    /**
     * Represents features available for a home in the Tibber API.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Features {
        private boolean realTimeConsumptionEnabled;

        public boolean isRealTimeConsumptionEnabled() {
            return realTimeConsumptionEnabled;
        }

        public void setRealTimeConsumptionEnabled(boolean realTimeConsumptionEnabled) {
            this.realTimeConsumptionEnabled = realTimeConsumptionEnabled;
        }
    }
}