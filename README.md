# Tibber Java Connector

A Java library for interacting with the [Tibber API](https://developer.tibber.com/docs/overview) using GraphQL.

## Features

- Simple, intuitive API for Java applications
- Type-safe models for Tibber API responses
- Support for retrieving user information, home data, and price information
- Support for retrieving consumption and production history
- Real-time data streaming via WebSocket subscriptions
- Comprehensive error handling
- Configurable HTTP client

## Installation

### Maven

```xml
<dependency>
    <groupId>io.github.mehdishahdoost</groupId>
    <artifactId>tibber-java-connector</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.github:tibber-java-connector:1.0.0'
```

## Getting Started

To use the Tibber Java Connector, you need to obtain an access token from the [Tibber Developer Portal](https://developer.tibber.com/).

### Creating a Client

```java
import com.github.tibber.api.client.TibberApiClient;

// Create a client with your access token
TibberApiClient client = new TibberApiClient("YOUR_ACCESS_TOKEN");
```

### Retrieving User Information

```java
import com.github.tibber.api.model.User;
import com.github.tibber.api.query.UserQuery;

// Create a user query
UserQuery userQuery = new UserQuery(client);

// Execute the query
User user = userQuery.execute();

// Access user information
System.out.println("User: " + user.getName());
System.out.println("Account type: " + user.getAccountType());

// Access homes
user.getHomes().forEach(home -> {
    System.out.println("Home ID: " + home.getHomeId());
    System.out.println("Address: " + home.getAddress().getAddress1() + ", " + home.getAddress().getCity());
});
```

### Retrieving Price Information

```java
import com.github.tibber.api.model.Price;
import com.github.tibber.api.query.PriceQuery;

import java.util.List;

// Create a price query
PriceQuery priceQuery = new PriceQuery(client);

// Get the current price for a specific home
String homeId = "YOUR_HOME_ID"; // Get this from the User object
Price currentPrice = priceQuery.getCurrentPrice(homeId);

System.out.println("Current price: " + currentPrice.getTotal() + " " + currentPrice.getCurrency());
System.out.println("Price level: " + currentPrice.getLevel());
System.out.println("Valid from: " + currentPrice.getStartsAt());

// Get today's prices
List<Price> todayPrices = priceQuery.getTodayPrices(homeId);
System.out.println("Today's prices:");
todayPrices.forEach(price -> {
    System.out.println(price.getStartsAt() + ": " + price.getTotal() + " " + price.getCurrency());
});

// Get tomorrow's prices (if available)
List<Price> tomorrowPrices = priceQuery.getTomorrowPrices(homeId);
if (!tomorrowPrices.isEmpty()) {
    System.out.println("Tomorrow's prices:");
    tomorrowPrices.forEach(price -> {
        System.out.println(price.getStartsAt() + ": " + price.getTotal() + " " + price.getCurrency());
    });
}
```

### Retrieving Consumption Data

You can retrieve historical consumption data with different resolutions:

```java
import com.github.tibber.api.model.Consumption;
import com.github.tibber.api.query.ConsumptionQuery;

import java.time.ZonedDateTime;
import java.util.List;

// Create a consumption query
ConsumptionQuery consumptionQuery = new ConsumptionQuery(client);

// Define the time period
ZonedDateTime from = ZonedDateTime.now().minusDays(7);
ZonedDateTime to = ZonedDateTime.now();

// Get hourly consumption data
List<Consumption> hourlyData = consumptionQuery.getHourlyConsumption(homeId, from, to);
System.out.println("Hourly consumption data:");
hourlyData.forEach(consumption -> {
    System.out.println(consumption.getFrom() + " - " + consumption.getTo() + ": " + 
                      consumption.getConsumption() + " kWh, " + 
                      consumption.getCost() + " " + consumption.getCurrency());
});

// Get daily consumption data
List<Consumption> dailyData = consumptionQuery.getDailyConsumption(homeId, from, to);
System.out.println("Daily consumption data:");
dailyData.forEach(consumption -> {
    System.out.println(consumption.getFrom().toLocalDate() + ": " + 
                      consumption.getConsumption() + " kWh, " + 
                      consumption.getCost() + " " + consumption.getCurrency());
});

// Monthly and annual data is also available
// List<Consumption> monthlyData = consumptionQuery.getMonthlyConsumption(homeId, from, to);
// List<Consumption> annualData = consumptionQuery.getAnnualConsumption(homeId, from, to);
```

### Retrieving Production Data

If you have solar panels or other electricity production, you can retrieve production data:

```java
import com.github.tibber.api.model.Production;
import com.github.tibber.api.query.ProductionQuery;

import java.time.ZonedDateTime;
import java.util.List;

// Create a production query
ProductionQuery productionQuery = new ProductionQuery(client);

// Define the time period
ZonedDateTime from = ZonedDateTime.now().minusDays(7);
ZonedDateTime to = ZonedDateTime.now();

// Get hourly production data
List<Production> hourlyData = productionQuery.getHourlyProduction(homeId, from, to);
System.out.println("Hourly production data:");
hourlyData.forEach(production -> {
    System.out.println(production.getFrom() + " - " + production.getTo() + ": " + 
                      production.getProduction() + " kWh, " + 
                      production.getProfit() + " " + production.getCurrency());
});

// Get daily production data
List<Production> dailyData = productionQuery.getDailyProduction(homeId, from, to);
System.out.println("Daily production data:");
dailyData.forEach(production -> {
    System.out.println(production.getFrom().toLocalDate() + ": " + 
                      production.getProduction() + " kWh, " + 
                      production.getProfit() + " " + production.getCurrency());
});

// Monthly and annual data is also available
// List<Production> monthlyData = productionQuery.getMonthlyProduction(homeId, from, to);
// List<Production> annualData = productionQuery.getAnnualProduction(homeId, from, to);
```

### Real-Time Data Subscriptions

You can subscribe to real-time measurement data using WebSockets:

```java
import com.github.tibber.api.client.TibberApiClient;
import com.github.tibber.api.model.LiveMeasurement;
import com.github.tibber.api.subscription.LiveMeasurementSubscription;
import com.github.tibber.api.subscription.SubscriptionHandler;

// Create a subscription handler
SubscriptionHandler handler = new SubscriptionHandler() {
    @Override
    public void onMeasurement(LiveMeasurement measurement) {
        System.out.println("Received measurement at: " + measurement.getTimestamp());
        System.out.println("Power consumption: " + measurement.getPower() + " W");
        System.out.println("Accumulated consumption: " + measurement.getAccumulatedConsumption() + " kWh");
    }

    @Override
    public void onConnected() {
        System.out.println("Connected to Tibber API");
    }

    @Override
    public void onDisconnected(String reason) {
        System.out.println("Disconnected from Tibber API: " + reason);
    }

    @Override
    public void onError(Throwable error) {
        System.err.println("Error in subscription: " + error.getMessage());
    }
};

// Subscribe to live measurements for a specific home
String homeId = "YOUR_HOME_ID"; // Get this from the User object
String subscriptionUrl = user.getWebsocketSubscriptionUrl(); // Get from user info
LiveMeasurementSubscription subscription = client.subscribeToLiveMeasurements(subscriptionUrl, homeId, handler);

// Later, when you want to stop the subscription
subscription.stop();
```

## Customizing the HTTP Client

You can customize the HTTP client used by the Tibber API client:

```java
import com.github.tibber.api.client.TibberApiClient;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

// Create a custom HTTP client
OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build();

// Create a Tibber API client with the custom HTTP client
TibberApiClient client = new TibberApiClient("YOUR_ACCESS_TOKEN", httpClient);
```

## Error Handling

The library throws `IOException` for API errors. You should handle these exceptions in your code:

```java
import com.github.tibber.api.model.User;
import com.github.tibber.api.query.UserQuery;

import java.io.IOException;

try {
    UserQuery userQuery = new UserQuery(client);
    User user = userQuery.execute();
    // Process user information
} catch (IOException e) {
    System.err.println("Error retrieving user information: " + e.getMessage());
    // Handle the error
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.