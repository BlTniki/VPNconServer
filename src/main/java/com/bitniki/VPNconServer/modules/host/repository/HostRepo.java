package com.bitniki.VPNconServer.modules.host.repository;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HostRepo extends CrudRepository<HostEntity, Long> {
    Optional<HostEntity> findByIpaddress(String ipaddress);
    Optional<HostEntity> findByName(String name);
}
