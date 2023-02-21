package com.bitniki.VPNconServer.modules.peer;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.model.User;

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
