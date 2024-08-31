package ru.bitniki.sms.domain.subscriptions.dao;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.bitniki.sms.domain.subscriptions.dto.R2dbcSubscriptionEntity;

public interface R2dbcSubscriptionsDao extends ReactiveCrudRepository<R2dbcSubscriptionEntity, Long> {
    Flux<R2dbcSubscriptionEntity> findByRole(@NotNull String role);
}
