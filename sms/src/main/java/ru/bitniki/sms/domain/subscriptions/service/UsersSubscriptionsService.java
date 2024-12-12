package ru.bitniki.sms.domain.subscriptions.service;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;

public interface UsersSubscriptionsService {
    /**
     * Return user subscription by given userId.
     * @param userId user id
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domain.exception.EntityNotFoundException if a user subscription
     * with this userId not exists
     */
    Mono<UserSubscription> getByUserId(long userId);

    /**
     * Return user subscriptions with given expiration date.
     * @param expirationDate user subscription expiration date
     * @return all user subscriptions with given expiration date
     */
    Flux<UserSubscription> getByExpirationDate(@NotNull LocalDate expirationDate);

    /**
     * Returns expired user subscriptions by given reference day.
     * A user subscription is considered expired relative to a given reference date
     * if its expirationDate is equal or before than the given reference date.
     * @param referenceDate the day on which the subscription validity is determined
     * @return all user subscriptions which expirationDate <= given referenceDate
     */
    Flux<UserSubscription> getExpired(@NotNull LocalDate referenceDate);

    /**
     * Applies subscription to the user and creates a new user subscription.
     * If the user already has a subscription, the subscription is renewed for the given times.
     * In this case, the subscriptionId must match the existing user's subscription id.
     * @param userId user id
     * @param subscriptionId subscription id
     * @param times to apply new subscription (basically to extend the subscription period). Must be greater than 0
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domain.exception.BadRequestException if request failed validation
     */
    Mono<UserSubscription> addUserSubscription(long userId, long subscriptionId, int times);

    /**
     * Removes subscription from user.
     * @param userId user id
     * @return removed user subscription
     */
    Mono<UserSubscription> removeUserSubscription(long userId);
}
