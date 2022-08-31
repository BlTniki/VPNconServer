package com.bitniki.VPNconServer.repository;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
    PeerEntity findByUserAndHostAndPeerConfName(UserEntity user, HostEntity host, String peerConfName);
}
