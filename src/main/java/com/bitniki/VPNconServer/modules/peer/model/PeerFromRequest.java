package com.bitniki.VPNconServer.modules.peer.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeerFromRequest {
    private String peerConfName;
    private String peerIp;
    private Long userId;
    private Long hostId;
}
