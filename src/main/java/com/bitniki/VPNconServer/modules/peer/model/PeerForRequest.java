package com.bitniki.VPNconServer.modules.peer.model;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeerForRequest {
    private String peerId;
    private String peerIp;
    private String peerPublicKey;
    private String peerPrivateKey;

    public static PeerForRequest toModel(PeerEntity entity) {
        PeerForRequest model = new PeerForRequest();
        model.setPeerId(entity.getUser().getId(), entity.getPeerConfName());
        model.setPeerIp(entity.getPeerIp());
        model.setPeerPrivateKey(null);
        model.setPeerPublicKey(null);

        return model;
    }

    public void fillEntityFromModel(PeerEntity entity) {
        entity.setPeerPrivateKey(this.getPeerPrivateKey());
        entity.setPeerPublicKey(this.getPeerPublicKey());
    }

    public void setPeerId(Long user_id, String confName) {
        this.peerId = confName + "_" + user_id;
    }
}
