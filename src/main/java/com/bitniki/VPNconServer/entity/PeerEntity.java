package com.bitniki.VPNconServer.entity;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "peer")
public class PeerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String peerIp;
    @Column(nullable = false)
    private String peerPrivateKey;
    @Column(nullable = false)
    private String peerPublicKey;
    @Column(nullable = false)
    private String peerConfName;
    @Column(nullable = false)
    private Boolean isActivated = true;
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

    public String getPeerIp() {
        return peerIp;
    }

    public void setPeerIp(String peerIp) {
        this.peerIp = peerIp;
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

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
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
