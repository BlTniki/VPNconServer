package com.bitniki.VPNconServer.modules.peer.connectHandler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Эта модель предназначена для ответов от хоста.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeerFromHost {
    private String peerId;
    private String peerIp;
    private String peerPrivateKey;
    private String peerPublicKey;
}
