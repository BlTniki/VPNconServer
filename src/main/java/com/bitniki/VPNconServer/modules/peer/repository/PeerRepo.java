package com.bitniki.VPNconServer.modules.peer.repository;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
    PeerEntity findByUserAndHostAndPeerConfName(UserEntity user, HostEntity host, String peerConfName);
    List<PeerEntity> findByUser(UserEntity user);
    PeerEntity findByHostAndPeerIp(HostEntity host, String ip);
}
