package com.bitniki.VPNconServer.entity;

import com.bitniki.VPNconServer.exception.HostAlreadyExistException;

import javax.persistence.*;

@Entity
@Table (name = "Host")
public class HostEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String ipadress;
    private String serverPublicKey;

    /*
     this static method update existing host fields if some of new host field is not null
    */
    public static HostEntity updateHost(HostEntity oldHost, HostEntity newHost) {
        oldHost.setIpadress((newHost.getIpadress() != null) ? newHost.getIpadress() : oldHost.getIpadress());
        oldHost.setServerPublicKey((newHost.getServerPublicKey() != null) ? newHost.getServerPublicKey() : oldHost.getServerPublicKey());

        return oldHost;
    }

    public HostEntity() {
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
