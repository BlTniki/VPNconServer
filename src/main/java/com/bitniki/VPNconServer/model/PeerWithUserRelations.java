package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;

/*
    Model with User relationships
*/
public class PeerWithUserRelations extends Peer {
    private User user;

    public static PeerWithUserRelations toModel(PeerEntity entity) {
        PeerWithUserRelations model = new PeerWithUserRelations();
        model.setId(entity.getId());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(entity.getPeerPrivateKey());
        model.setPeerPublicKey(entity.getPeerPublicKey());
        model.setPeerConfName(entity.getPeerConfName());
        model.setUser(User.toModel(entity.getUser()));

        return model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
