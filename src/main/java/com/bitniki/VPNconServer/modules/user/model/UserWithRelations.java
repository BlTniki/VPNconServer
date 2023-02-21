package com.bitniki.VPNconServer.modules.user.model;

import com.bitniki.VPNconServer.modules.peer.PeerEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.peer.Peer;
import com.bitniki.VPNconServer.modules.peer.PeerWithHostRelations;

import java.util.List;
import java.util.stream.Collectors;

/*
    Model with peers relationships
*/
public class UserWithRelations extends User{
    private List<Peer> peers;

    public static UserWithRelations toModel (UserEntity entity) {
        return new UserWithRelations(entity);
    }

    public UserWithRelations() {
    }

    public UserWithRelations(UserEntity entity) {
        super(entity);
        this.setPeers(entity.getPeers());
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void setPeers(List<PeerEntity> peerEntities) {
        this.peers = peerEntities.stream().map(PeerWithHostRelations::toModel).collect(Collectors.toList());
    }
}
