package com.bitniki.VPNconServer.modules.peer;

import com.bitniki.VPNconServer.modules.host.model.Host;
import com.bitniki.VPNconServer.modules.host.entity.HostEntity;

/*
    Model with Host relationships
*/
public class PeerWithHostRelations extends Peer {
    private Host host;

    public static PeerWithHostRelations toModel(PeerEntity entity) {
        return new PeerWithHostRelations(entity);
    }

    public PeerWithHostRelations() {
    }

    public PeerWithHostRelations(PeerEntity entity) {
        super(entity);
        this.setHost(entity.getHost());
    }


    public Host getHost() {
        return host;
    }

    public void setHost(HostEntity host) {
        this.host = Host.toModel(host);
    }
}
