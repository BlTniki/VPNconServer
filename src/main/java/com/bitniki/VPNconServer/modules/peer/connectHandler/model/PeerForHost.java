package com.bitniki.VPNconServer.modules.peer.connectHandler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Эта модель предназначена для запросов до хоста.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PeerForHost {
    private String peerId;
    private String peerIp;
    private String peerPublicKey;
    private String peerPrivateKey;
}
