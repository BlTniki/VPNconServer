package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;

/*
    Model with User relationships
*/
public class PeerWithUserRelations extends Peer {
    private User user;

    public static PeerWithUserRelations toModel(PeerEntity entity) {
        return new PeerWithUserRelations(entity);
    }

    public PeerWithUserRelations() {
    }

    public PeerWithUserRelations(PeerEntity entity) {
        super(entity);
        this.setUser(entity.getUser());
    }

    public User getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = User.toModel(user);
    }
}
