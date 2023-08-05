package com.bitniki.VPNconServer.modules.peer.repository;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PeerRepo extends CrudRepository<PeerEntity, Long> {
    @Query("SELECT p FROM PeerEntity p WHERE p.peerConfName = :peerConfName AND p.user.id = :userId AND p.host.id = :hostId")
    Optional<PeerEntity> findByPeerConfNameAndUserIdAndHostId(String peerConfName, Long userId, Long hostId);

    @Query("SELECT p FROM PeerEntity p JOIN p.user u WHERE u.id = :userId")
    List<PeerEntity> findAllWithUserId(Long userId);

    @Query("SELECT p FROM PeerEntity p JOIN p.user u WHERE u.login = :login")
    Iterable<PeerEntity> findAllWithUserLogin(String login);

    @Query("SELECT p FROM PeerEntity p WHERE p.id = :id AND p.user.login = :login")
    Optional<PeerEntity> findByIdAndUserLogin(Long id, String login);

    @Query("SELECT p FROM PeerEntity p WHERE p.host.id = :hostId")
    List<PeerEntity> findAllByHostId(Long hostId);

    @Query("SELECT p FROM PeerEntity p WHERE p.peerIp = :peerIp AND p.host.id = :hostId")
    Optional<PeerEntity> findByPeerIpAndHostId(String peerIp, Long hostId);
}
