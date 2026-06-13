package com.github.mehdishahdoost.sample;

import com.github.mehdishahdoost.api.client.TibberApiClient;
import com.github.mehdishahdoost.api.model.User;
import com.github.mehdishahdoost.api.model.Price;
import com.github.mehdishahdoost.api.query.UserQuery;
import com.github.mehdishahdoost.api.query.PriceQuery;

import java.io.IOException;
import java.util.List;

/**
 * Sample application demonstrating the use of the Tibber Java Connector.
 */
public class TibberSampleApp {

    public static void main(String[] args) {
        System.out.println("Tibber Java Connector Sample Application");
        System.out.println("----------------------------------------");

        if (args.length < 1) {
            System.out.println("Please provide your Tibber API access token as a command line argument.");
            System.out.println("Usage: java -jar tibber-java-connector-sample.jar YOUR_ACCESS_TOKEN");
            return;
        }

        String accessToken = args[0];
        
        try {
            // Create a client with the access token
            TibberApiClient client = new TibberApiClient(accessToken);
            System.out.println("Connected to Tibber API");
            
            // Get user information
            UserQuery userQuery = new UserQuery(client);
            User user = userQuery.execute();
            
            System.out.println("\nUser Information:");
            System.out.println("----------------");
            System.out.println("Name: " + user.getName());
            System.out.println("Account Type: " + user.getAccountType());
            
            // Display homes
            System.out.println("\nHomes:");
            System.out.println("------");
            if (user.getHomes() == null || user.getHomes().isEmpty()) {
                System.out.println("No homes found.");
                return;
            }
            
            user.getHomes().forEach(home -> {
                System.out.println("Home ID: " + home.getHomeId());
                if (home.getAddress() != null) {
                    System.out.println("Address: " + 
                            (home.getAddress().getAddress1() != null ? home.getAddress().getAddress1() : "") + ", " + 
                            (home.getAddress().getCity() != null ? home.getAddress().getCity() : ""));
                }
                
                // Get price information if available
                if (home.getCurrentSubscription() != null) {
                    try {
                        PriceQuery priceQuery = new PriceQuery(client);
                        Price currentPrice = priceQuery.getCurrentPrice(home.getHomeId());
                        
                        System.out.println("\nCurrent Price Information:");
                        System.out.println("-------------------------");
                        System.out.println("Total price: " + currentPrice.getTotal() + " " + currentPrice.getCurrency());
                        System.out.println("Energy component: " + currentPrice.getEnergy());
                        System.out.println("Tax component: " + currentPrice.getTax());
                        System.out.println("Price level: " + currentPrice.getLevel());
                        System.out.println("Valid from: " + currentPrice.getStartsAt());
                        
                        // Get today's prices
                        List<Price> todayPrices = priceQuery.getTodayPrices(home.getHomeId());
                        System.out.println("\nToday's Prices:");
                        System.out.println("--------------");
                        todayPrices.forEach(price -> {
                            System.out.println(price.getStartsAt() + ": " + price.getTotal() + " " + price.getCurrency() + " (" + price.getLevel() + ")");
                        });
                        
                        // Get tomorrow's prices if available
                        List<Price> tomorrowPrices = priceQuery.getTomorrowPrices(home.getHomeId());
                        if (!tomorrowPrices.isEmpty()) {
                            System.out.println("\nTomorrow's Prices:");
                            System.out.println("-----------------");
                            tomorrowPrices.forEach(price -> {
                                System.out.println(price.getStartsAt() + ": " + price.getTotal() + " " + price.getCurrency() + " (" + price.getLevel() + ")");
                            });
                        } else {
                            System.out.println("\nTomorrow's prices not available yet.");
                        }
                    } catch (IOException e) {
                        System.err.println("Error retrieving price information: " + e.getMessage());
                    }
                } else {
                    System.out.println("No subscription information available for this home.");
                }
            });
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}