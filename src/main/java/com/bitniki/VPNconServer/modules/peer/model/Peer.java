package com.bitniki.VPNconServer.modules.peer.model;

import com.bitniki.VPNconServer.modules.host.model.Host;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.user.model.User;
import lombok.*;

/**
 * Модель {@link PeerEntity} без внутренней информации
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Peer {
    private Long id;
    private String peerConfName;
    private String peerIp;
    private Boolean isActivated;
    private User user;
    private Host host;

    public static Peer toModel(PeerEntity entity) {
        return new Peer(entity);
    }

    public Peer(PeerEntity entity) {
        this.setId(entity.getId());
        this.setPeerIp(entity.getPeerIp());
        this.setPeerConfName(entity.getPeerConfName());
        this.setIsActivated(entity.getIsActivated());
    }
}
