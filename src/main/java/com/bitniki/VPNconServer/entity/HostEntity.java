package com.bitniki.VPNconServer.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "host")
public class HostEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ipadress;
    private String serverPassword;
    private String serverPublicKey;
    private String dns;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "host", orphanRemoval = true)
    private List<PeerEntity> peerEntities;

    /*
     this static method update existing host fields if some of new host field is not null
    */
    public static HostEntity updateHost(HostEntity oldHost, HostEntity newHost) {
        oldHost.setName( (newHost.getName() != null) ? newHost.getName() : oldHost.getName() );
        oldHost.setIpadress((newHost.getIpadress() != null) ? newHost.getIpadress() : oldHost.getIpadress());
        oldHost.setServerPublicKey((newHost.getServerPublicKey() != null) ? newHost.getServerPublicKey() : oldHost.getServerPublicKey());
        oldHost.setDns( (newHost.getDns() != null) ? newHost.getDns() : oldHost.getDns() );

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public String getServerPublicKey() {
        return serverPublicKey;
    }

    public void setServerPublicKey(String serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    public List<PeerEntity> getPeers() {
        return peerEntities;
    }

    public void setPeers(List<PeerEntity> peerEntities) {
        this.peerEntities = peerEntities;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public List<PeerEntity> getPeerEntities() {
        return peerEntities;
    }

    public void setPeerEntities(List<PeerEntity> peerEntities) {
        this.peerEntities = peerEntities;
    }
}
