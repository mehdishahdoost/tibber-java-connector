package com.github.mehdishahdoost.api.subscription;

import com.github.mehdishahdoost.api.model.LiveMeasurement;

/**
 * Interface for handling subscription events from the Tibber API.
 * <p>
 * Implementations of this interface can be registered with a subscription
 * to receive real-time measurement data and connection status updates.
 */
public interface SubscriptionHandler {

    /**
     * Called when a new measurement is received.
     *
     * @param measurement the live measurement data
     */
    void onMeasurement(LiveMeasurement measurement);

    /**
     * Called when the subscription is connected.
     */
    void onConnected();

    /**
     * Called when the subscription is disconnected.
     *
     * @param reason the reason for disconnection
     */
    void onDisconnected(String reason);

    /**
     * Called when an error occurs during the subscription.
     *
     * @param error the error that occurred
     */
    void onError(Throwable error);
}