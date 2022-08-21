package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;

public class Peer {
    private Long id;
    private String peerIp;
    private String peerPrivateKey;
    private String peerPublicKey;
    private String peerConfName;

    public static Peer toModel(PeerEntity entity) {
        Peer model = new Peer();
        model.setId(entity.getId());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(entity.getPeerPrivateKey());
        model.setPeerPublicKey(entity.getPeerPublicKey());
        model.setPeerConfName(entity.getPeerConfName());

        return model;
    }

    public Peer() {
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

}
