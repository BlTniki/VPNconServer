package ru.bitniki.sms.domen.subscriptions.service;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.users.dto.Subscription;

public interface SubscriptionsService {
    /**
     * Return subscription by given id.
     * @param id subscription id
     * @return subscription
     */
    Mono<Subscription> getById(long id);

    /**
     * Returns all subscription for given role.
     * @param role specific role
     * @return all subscription for given role
     */
    Flux<Subscription> getByRole(@NotNull String role);
}
