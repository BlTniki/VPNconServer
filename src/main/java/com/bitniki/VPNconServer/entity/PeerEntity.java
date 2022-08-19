package com.bitniki.VPNconServer.entity;

import javax.persistence.*;

@Entity
@Table(name = "peer")
public class PeerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String peerPrivateKey;
    private String peerPublicKey;
    private String peerConfName;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostEntity host;

    public PeerEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeerPrivateKey() {
        return peerPrivateKey;
    }

    public void setPeerPrivateKey(String peerPrivateKey) {
        this.peerPrivateKey = peerPrivateKey;
    }

    public String getPeerPublicKey() {
        return peerPublicKey;
    }

    public void setPeerPublicKey(String peerPublicKey) {
        this.peerPublicKey = peerPublicKey;
    }

    public String getPeerConfName() {
        return peerConfName;
    }

    public void setPeerConfName(String peerConfName) {
        this.peerConfName = peerConfName;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public HostEntity getHost() {
        return host;
    }

    public void setHost(HostEntity host) {
        this.host = host;
    }
}
