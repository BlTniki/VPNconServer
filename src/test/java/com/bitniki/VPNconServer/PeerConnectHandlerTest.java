package com.bitniki.VPNconServer;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.connectHandler.PeerConnectHandler;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.junit.jupiter.api.Test;

public class PeerConnectHandlerTest {

    @Test
    public void testTest() {
        PeerEntity peer = PeerEntity.builder()
                .id(1L)
                .peerConfName("test")
                .peerIp("10.8.0.123")
                .user(
                        UserEntity.builder()
                                .id(1L)
                                .build()
                )
                .host(
                        HostEntity.builder()
                                .id(1L)
                                .name("test")
                                .ipaddress("127.0.0.1")
                                .port(5000)
                                .hostInternalNetworkPrefix("127.0.0.1")
                                .hostPassword("5543678")
                                .hostPublicKey("lolkek")
                                .build()
                )
                .build();

        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.createPeerOnHost();
    }
}
