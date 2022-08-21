package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.HostNotFoundException;
import com.bitniki.VPNconServer.exception.UserNotFoundException;
import com.bitniki.VPNconServer.model.Peer;
import com.bitniki.VPNconServer.model.PeerWithAllRelations;
import com.bitniki.VPNconServer.repository.HostRepo;
import com.bitniki.VPNconServer.repository.PeerRepo;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PeerWithAllRelations create(Long user_id, Long host_id, PeerEntity peerEntity) throws UserNotFoundException, HostNotFoundException {
        Optional<UserEntity> userEntityOptional;
        UserEntity user;
        userEntityOptional = userRepo.findById(user_id);
        if(userEntityOptional.isPresent()) user = userEntityOptional.get();
        else throw new UserNotFoundException("User not found");

        Optional<HostEntity> hostEntityOptional;
        HostEntity host;
        hostEntityOptional = hostRepo.findById(host_id);
        if(hostEntityOptional.isPresent()) host = hostEntityOptional.get();
        else throw new HostNotFoundException("Host not found");

        peerEntity.setUser(user);
        peerEntity.setHost(host);
        return PeerWithAllRelations.toModel(peerRepo.save(peerEntity));
    }
}
