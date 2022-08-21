package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.HostEntity;

public class Host {
    private Long id;
    private String ipadress;
    private String serverPublicKey;

    public static Host toModel (HostEntity entity) {
        Host model = new Host();
        model.setId(entity.getId());
        model.setIpadress(entity.getIpadress());
        model.setServerPublicKey(entity.getServerPublicKey());

        return model;
    }

    public Host() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public String getServerPublicKey() {
        return serverPublicKey;
    }

    public void setServerPublicKey(String serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }
}
