package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.PeerEntity;

/*
    Model with Host relationships
*/
public class PeerWithHostRelations extends Peer{
    private Host host;

    public static PeerWithHostRelations toModel(PeerEntity entity) {
        PeerWithHostRelations model = new PeerWithHostRelations();
        model.setId(entity.getId());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(entity.getPeerPrivateKey());
        model.setPeerPublicKey(entity.getPeerPublicKey());
        model.setPeerConfName(entity.getPeerConfName());
        model.setHost(Host.toModel(entity.getHost()));

        return model;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
