package com.bitniki.VPNconServer.modules.peer.repository;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
    @Query(
            value = "SELECT p FROM peers p WHERE p.peer_conf_name = :peerConfName AND p.user_id = :userId AND p.host_id = :hostId",
            nativeQuery = true
    )
    Optional<PeerEntity> findByPeerConfNameAndUserIdAndHostId(String peerConfName, Long userId, Long hostId);

    @Query(
        value = "SELECT p FROM peers p INNER JOIN users u ON u.id = p.user_id WHERE u.login = :login",
        nativeQuery = true
    )
    Iterable<PeerEntity> findAllWithUserLogin(String login);

    @Query(
            value = "SELECT p FROM peers p WHERE p.id = :id AND p.user.login = :login",
            nativeQuery = true
    )
    Optional<PeerEntity> findByIdAndUserLogin(Long id, String login);

    @Query(
            value = "SELECT p FROM peers p WHERE p.host_id = :hostId",
            nativeQuery = true
    )
    List<PeerEntity> findAllByHostId(Long hostId);

    @Query(
            value = "SELECT p FROM peers p WHERE p.peer_ip = :peerIp AND p.host_id = :hostId",
            nativeQuery = true
    )
    Optional<PeerEntity> findByPeerIpAndHostId(String peerIp, Long hostId);
}
