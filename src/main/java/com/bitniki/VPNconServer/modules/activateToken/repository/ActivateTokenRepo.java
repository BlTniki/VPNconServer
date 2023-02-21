package com.bitniki.VPNconServer.modules.activateToken.repository;

import com.bitniki.VPNconServer.modules.activateToken.entity.ActivateTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivateTokenRepo extends CrudRepository<ActivateTokenEntity, Long> {
    Optional<ActivateTokenEntity> findByToken(String token);
}
