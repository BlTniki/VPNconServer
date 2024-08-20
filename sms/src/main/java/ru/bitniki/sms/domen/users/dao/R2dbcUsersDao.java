package ru.bitniki.sms.domen.users.dao;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.users.dto.R2dbcUserEntity;

public interface R2dbcUsersDao extends ReactiveCrudRepository<R2dbcUserEntity, Long> {
    Mono<R2dbcUserEntity> findByTelegramId(@NotNull Long id);
}
