package ru.bitniki.sms.domain.subscriptions.service.event;

import jakarta.validation.constraints.NotNull;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;

/**
 * This interface reflects the functionality of recording events of the UserSubscription service in a queue.
 */
public interface UserSubscriptionEventProducer {
    /**
     * Writes a "paid user subscription" event into a queue
     * @param userSubscription a "paid user subscription"
     */
    void createPaidEvent(@NotNull UserSubscription userSubscription);

    /**
     * Writes a "burn soon user subscription" event into a queue
     * @param userSubscription a "burn soon user subscription"
     */
    void createBurnSoonEvent(@NotNull UserSubscription userSubscription);

    /**
     * Writes a "burned user subscription" event into a queue
     * @param userSubscription a "burned user subscription"
     */
    void createBurnedEvent(@NotNull UserSubscription userSubscription);
}
