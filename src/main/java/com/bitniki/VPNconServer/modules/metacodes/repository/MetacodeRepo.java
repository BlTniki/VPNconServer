package com.bitniki.VPNconServer.modules.metacodes.repository;

import com.bitniki.VPNconServer.modules.metacodes.entity.MetacodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MetacodeRepo extends CrudRepository<MetacodeEntity, Long> {
    Optional<MetacodeEntity> findByCode(String code);
}
