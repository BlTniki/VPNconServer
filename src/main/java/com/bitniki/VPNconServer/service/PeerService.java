package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.connectHandler.PeerConnectHandler;
import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.HostNotFoundException;
import com.bitniki.VPNconServer.exception.alreadyExistException.PeerAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.PeerNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.model.PeerWithAllRelations;
import com.bitniki.VPNconServer.repository.HostRepo;
import com.bitniki.VPNconServer.repository.PeerRepo;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeerService {
    @Autowired
    private PeerRepo peerRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private HostRepo hostRepo;

    public List<PeerWithAllRelations> getAll() {
        List<PeerEntity> peerEntities = new ArrayList<>();
        peerRepo.findAll().forEach(peerEntities::add);
        return peerEntities.stream().map(PeerWithAllRelations::toModel).collect(Collectors.toList());
    }

    public PeerWithAllRelations getOne(Long id) throws PeerNotFoundException {
        Optional<PeerEntity> peerEntityOptional;
        peerEntityOptional = peerRepo.findById(id);
        if(peerEntityOptional.isPresent()) return PeerWithAllRelations.toModel(peerEntityOptional.get());
        else throw new PeerNotFoundException("Peer not found");
    }

    public PeerWithAllRelations create(Long user_id, Long host_id, PeerEntity peerEntity) throws UserNotFoundException, HostNotFoundException, PeerAlreadyExistException {
        //find user entity
        Optional<UserEntity> userEntityOptional;
        UserEntity user;
        userEntityOptional = userRepo.findById(user_id);
        if(userEntityOptional.isPresent()) user = userEntityOptional.get();
        else throw new UserNotFoundException("User not found");

        //find host entity
        Optional<HostEntity> hostEntityOptional;
        HostEntity host;
        hostEntityOptional = hostRepo.findById(host_id);
        if(hostEntityOptional.isPresent()) host = hostEntityOptional.get();
        else throw new HostNotFoundException("Host not found");

        //check the uniqueness of the confName for a specific host and user
        if(peerRepo.findByUserAndHostAndPeerConfName(user, host, peerEntity.getPeerConfName()) != null) {
            throw new PeerAlreadyExistException("Peer already exist");
        }
        peerEntity.setUser(user);
        peerEntity.setHost(host);
        //create peer on host and complete peer entity
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peerEntity);
        peerConnectHandler.createPeerOnHostAndFillEntity();

        return PeerWithAllRelations.toModel(peerRepo.save(peerEntity));
    }

    public PeerWithAllRelations update(Long id, PeerEntity newPeer)
            throws PeerNotFoundException, PeerAlreadyExistException {
        //find old peer
        Optional<PeerEntity> peerEntityOptional;
        peerEntityOptional = peerRepo.findById(id);
        PeerEntity oldPeer;
        if(peerEntityOptional.isPresent()) oldPeer = peerEntityOptional.get();
        else throw new PeerNotFoundException("Peer not found");

        //check the uniqueness of the confName for a specific host and user
        //this is placeholder because we cant properly update confName on host
        if(newPeer.getPeerConfName() != null && peerRepo.findByUserAndHostAndPeerConfName(
                oldPeer.getUser(), oldPeer.getHost(), newPeer.getPeerConfName()) != null) {
            throw new PeerAlreadyExistException("Peer already exist");
        }
        //if we have new peer ip – update
        if(newPeer.getPeerIp() != null) {
            oldPeer.setPeerIp(newPeer.getPeerIp());
        }

        //update peer on host
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(oldPeer);
        peerConnectHandler.updatePeerOnHost();

        return PeerWithAllRelations.toModel(peerRepo.save(oldPeer));
    }

    public PeerWithAllRelations delete(Long id) throws PeerNotFoundException {
        Optional<PeerEntity> peerEntityOptional;
        peerEntityOptional = peerRepo.findById(id);
        PeerEntity peer;
        if(peerEntityOptional.isPresent()) peer = peerEntityOptional.get();
        else throw new PeerNotFoundException("Peer not found");

        //deleting peer on host
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.deletePeerOnHost();
        peerRepo.delete(peer);
        return PeerWithAllRelations.toModel(peer);

    }
}
