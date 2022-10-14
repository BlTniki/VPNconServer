package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.HostEntity;

public class Host {
    private Long id;
    private String ipadress;
    private String serverPublicKey;
    private String dns;


    public static Host toModel (HostEntity entity) {
        return new Host(entity);
    }

    public Host() {
    }

    public Host(HostEntity entity) {
        this.setId(entity.getId());
        this.setIpadress(entity.getIpadress());
        this.setServerPublicKey(entity.getServerPublicKey());
        this.setDns(entity.getDns());
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

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }
}
