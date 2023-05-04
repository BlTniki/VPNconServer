package com.bitniki.VPNconServer.modules.peer.repository;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
    @Query("SELECT p FROM peers p WHERE p.peer_conf_name = :peerConfName AND p.user_id = :userId AND p.host_id = :hostId")
    Optional<PeerEntity> findByPeerConfNameAndUserIdAndHostId(String peerConfName, Long userId, Long hostId);
    @Query("SELECT p FROM peers p WHERE p.user.login = :login")
    Iterable<PeerEntity> findAllWithUserLogin(String login);
    @Query("SELECT p FROM peers p WHERE p.id = :id AND p.user.login = :login")
    Optional<PeerEntity> findByIdAndUserLogin(Long id, String login);
    @Query("SELECT p FROM peers p WHERE p.host_id = :hostId")
    List<PeerEntity> findAllByHostId(Long hostId);
    @Query("SELECT p FROM peers p WHERE p.peer_ip = :peerIp AND p.host_id = :hostId")
    Optional<PeerEntity> findByPeerIpAndHostId(String peerIp, Long hostId);
}
