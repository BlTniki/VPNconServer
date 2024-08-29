package ru.bitniki.sms.domen.subscriptions.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.subscriptions.dto.R2dbcUserSubscriptionEntity;

public interface R2dbcUsersSubscriptionsDao extends ReactiveCrudRepository<R2dbcUserSubscriptionEntity, Long> {
    Mono<R2dbcUserSubscriptionEntity> findByUserId(long userId);
}
