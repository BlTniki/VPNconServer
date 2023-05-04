package com.bitniki.VPNconServer.modules.peer.entity;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "peers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PeerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String peerConfName;
    @Column(nullable = false)
    private String peerIp;
    @Column(nullable = false)
    private String peerPrivateKey;
    @Column(nullable = false)
    private String peerPublicKey;
    @Column(nullable = false)
    private Boolean isActivated = true;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private HostEntity host;
}
