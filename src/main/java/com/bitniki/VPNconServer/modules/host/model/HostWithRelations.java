package com.bitniki.VPNconServer.modules.host.model;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.model.Peer;
import com.bitniki.VPNconServer.modules.peer.model.PeerWithUserRelations;

import java.util.List;
import java.util.stream.Collectors;

/*
    Model with peers relationships
*/
public class HostWithRelations extends Host {
    private List<Peer> peers;

    public static HostWithRelations toModel (HostEntity entity) {
        return new HostWithRelations(entity);
    }

    public HostWithRelations() {
    }

    public HostWithRelations(HostEntity entity) {
        super(entity);
        this.setPeers(entity.getPeers());
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void setPeers(List<PeerEntity> peers) {
        this.peers = peers.stream().map(PeerWithUserRelations::toModel).collect(Collectors.toList());
    }
}
