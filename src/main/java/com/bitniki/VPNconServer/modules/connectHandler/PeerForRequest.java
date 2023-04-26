package com.bitniki.VPNconServer.modules.connectHandler;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;


public class PeerForRequest {
    private String peerId;
    private String peerIp;
    private String peerPublicKey;
    private String peerPrivateKey;

    public static PeerForRequest toModel(PeerEntity entity) {
        PeerForRequest model = new PeerForRequest();
        model.setPeerId(entity.getUser().getId(), entity.getPeerConfName());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(null);
        model.setPeerPublicKey(null);

        return model;
    }

    public void fillEntityFromModel(PeerEntity entity) {
        entity.setPeerPrivateKey(this.getPeerPrivateKey());
        entity.setPeerPublicKey(this.getPeerPublicKey());
    }

    public PeerForRequest() {
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(Long user_id, String confName) {
        this.peerId = confName + "_" + user_id;
    }

    public String getPeerIp() {
        return peerIp;
    }

    public void setPeerIp(String peerIp) {
        this.peerIp = peerIp;//Integer.parseInt(peerIp.substring(peerIp.lastIndexOf(".")+1));
    }

    public String getPeerPublicKey() {
        return peerPublicKey;
    }

    public void setPeerPublicKey(String peerPublicKey) {
        this.peerPublicKey = peerPublicKey;
    }

    public String getPeerPrivateKey() {
        return peerPrivateKey;
    }

    public void setPeerPrivateKey(String peerPrivateKey) {
        this.peerPrivateKey = peerPrivateKey;
    }
}
