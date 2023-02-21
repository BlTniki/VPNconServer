package com.bitniki.VPNconServer.modules.host.repository;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import org.springframework.data.repository.CrudRepository;

public interface HostRepo extends CrudRepository<HostEntity, Long> {
    HostEntity findByIpadress (String ipadress);
}
