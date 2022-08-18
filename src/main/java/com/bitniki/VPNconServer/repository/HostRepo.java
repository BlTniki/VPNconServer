package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.HostEntity;
import org.springframework.data.repository.CrudRepository;

public interface HostRepo extends CrudRepository<HostEntity, Long> {
    HostEntity findByIpadress (String ipadress);
}
