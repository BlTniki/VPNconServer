package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;

/*
    Model with user and host relationships
 */
public class PeerWithAllRelations extends Peer{
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
