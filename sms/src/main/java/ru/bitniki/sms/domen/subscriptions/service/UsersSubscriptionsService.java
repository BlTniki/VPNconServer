package ru.bitniki.sms.domen.subscriptions.service;

import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.subscriptions.dto.UserSubscription;

public interface UsersSubscriptionsService {
    /**
     * Return user subscription by given userId.
     * @param userId user id
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domen.exception.EntityNotFoundException if a user subscription with this userId not exists
     */
    Mono<UserSubscription> getByUserId(long userId);

    /**
     * Applies subscription to the user and creates a new user subscription.
     * If the user already has a subscription, the subscription is renewed for the given times.
     * In this case, the subscriptionId must match the existing user's subscription id.
     * @param userId user id
     * @param subscriptionId subscription id
     * @param times to apply new subscription (basically to extend the subscription period). Must be greater than 0
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domen.exception.BadRequestException if request failed validation
     */
    Mono<UserSubscription> addUserSubscription(long userId, long subscriptionId, int times);

    /**
     * Removes subscription from user.
     * @param userId user id
     * @return removed user subscription
     */
    Mono<UserSubscription> removeUserSubscription(long userId);
}
