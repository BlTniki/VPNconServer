package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByLogin (String login);
    Optional<UserEntity> findByTelegramId(Long telegramId);
}
