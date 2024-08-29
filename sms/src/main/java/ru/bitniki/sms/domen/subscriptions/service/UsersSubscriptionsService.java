package ru.bitniki.sms.domen.subscriptions.service;

import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.subscriptions.dto.UserSubscription;

public interface UsersSubscriptionsService {
    /**
     * Return user subscription by given id.
     * @param id user subscription id
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domen.exception.EntityNotFoundException if a user subscription with this id not exists
     */
    Mono<UserSubscription> getById(long id);

    /**
     * Applies subscription to user and creates new UserSubscription.
     * @param userId user id
     * @param subscriptionId subscription id
     * @param times to apply new subscription (basically to extend the subscription period)
     * @return {@link UserSubscription}
     * @throws ru.bitniki.sms.domen.exception.BadRequestException if request failed validation
     */
    Mono<UserSubscription> createUserSubscription(long userId, long subscriptionId, int times);
}
