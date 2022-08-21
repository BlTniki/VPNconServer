package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Host {
    private Long id;
    private String ipadress;
    private String serverPublicKey;
    private List<Peer> peers;

    public static Host toModel (HostEntity entity) {
        Host model = new Host();
        model.setId(entity.getId());
        model.setIpadress(entity.getIpadress());
        model.setServerPublicKey(entity.getServerPublicKey());
        model.setPeers(entity.getPeers());

        return model;
    }

    public Host() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public String getServerPublicKey() {
        return serverPublicKey;
    }

    public void setServerPublicKey(String serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void setPeers(List<PeerEntity> peers) {
        this.peers = peers.stream().map(Peer::toModel).collect(Collectors.toList());
    }
}
