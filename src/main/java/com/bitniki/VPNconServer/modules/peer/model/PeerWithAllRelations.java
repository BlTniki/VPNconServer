package com.bitniki.VPNconServer.modules.peer.model;

import com.bitniki.VPNconServer.modules.host.model.Host;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.model.User;

/*
    Model with user and host relationships
 */
public class PeerWithAllRelations extends Peer {
    private User user;
    private Host host;

    public static PeerWithAllRelations toModel(PeerEntity entity) {
        return new PeerWithAllRelations(entity);
    }

    public PeerWithAllRelations() {
    }

    public PeerWithAllRelations(PeerEntity entity) {
        super(entity);
        this.setUser(entity.getUser());
        this.setHost(entity.getHost());
    }

    public User getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = User.toModel(user);
    }

    public Host getHost() {
        return host;
    }

    public void setHost(HostEntity host) {
        this.host = Host.toModel(host);
    }
}
