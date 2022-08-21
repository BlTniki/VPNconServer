package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;

/*
    Model with user and host relationships
 */
public class PeerWithAllRelations extends Peer{
    private User user;
    private Host host;

    public static PeerWithAllRelations toModel(PeerEntity entity) {
        PeerWithAllRelations model = new PeerWithAllRelations();
        model.setId(entity.getId());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(entity.getPeerPrivateKey());
        model.setPeerPublicKey(entity.getPeerPublicKey());
        model.setPeerConfName(entity.getPeerConfName());
        model.setUser(User.toModel(entity.getUser()));
        model.setHost(Host.toModel(entity.getHost()));

        return model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
