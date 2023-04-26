package com.bitniki.VPNconServer.modules.user.repository;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin (String login);
    Optional<UserEntity> findByTelegramId(Long telegramId);
//    List<UserEntity> findBySubscriptionExpirationDayIsNotNull();
}
