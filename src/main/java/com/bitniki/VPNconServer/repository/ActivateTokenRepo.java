package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.ActivateTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivateTokenRepo extends CrudRepository<ActivateTokenEntity, Long> {
    Optional<ActivateTokenEntity> findByToken(String token);
}
