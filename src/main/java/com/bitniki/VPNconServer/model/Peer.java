package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;

public class Peer {
    private Long id;
    private String peerIp;
    private String peerPrivateKey;
    private String peerPublicKey;
    private String peerConfName;
    private Boolean isActivated;

    public static Peer toModel(PeerEntity entity) {
        return new Peer(entity);
    }

    public Peer() {
    }

    public Peer(PeerEntity entity) {
        this.setId(entity.getId());
        this.setPeerIp(entity.getPeerIp());
        this.setPeerPrivateKey(entity.getPeerPrivateKey());
        this.setPeerPublicKey(entity.getPeerPublicKey());
        this.setPeerConfName(entity.getPeerConfName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeerIp() {
        return peerIp;
    }

    public void setPeerIp(String peerIp) {
        this.peerIp = peerIp;
    }

    public String getPeerPrivateKey() {
        return peerPrivateKey;
    }

    public void setPeerPrivateKey(String peerPrivateKey) {
        this.peerPrivateKey = peerPrivateKey;
    }

    public String getPeerPublicKey() {
        return peerPublicKey;
    }

    public void setPeerPublicKey(String peerPublicKey) {
        this.peerPublicKey = peerPublicKey;
    }

    public String getPeerConfName() {
        return peerConfName;
    }

    public void setPeerConfName(String peerConfName) {
        this.peerConfName = peerConfName;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }
}
