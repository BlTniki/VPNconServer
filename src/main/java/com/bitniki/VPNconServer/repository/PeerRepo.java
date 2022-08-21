package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.PeerEntity;
import org.springframework.data.repository.CrudRepository;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
}
