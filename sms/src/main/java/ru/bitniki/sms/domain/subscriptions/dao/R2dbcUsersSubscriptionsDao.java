package ru.bitniki.sms.domain.subscriptions.dao;

import java.time.LocalDate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domain.subscriptions.dto.R2dbcUserSubscriptionEntity;

public interface R2dbcUsersSubscriptionsDao extends ReactiveCrudRepository<R2dbcUserSubscriptionEntity, Long> {
    Mono<R2dbcUserSubscriptionEntity> findByUserId(long userId);

    Flux<R2dbcUserSubscriptionEntity> findByExpirationDate(LocalDate expirationDate);

    Flux<R2dbcUserSubscriptionEntity> findByExpirationDateBefore(LocalDate expirationDate);
}
