package com.bitniki.VPNconServer.modules.peer.connectHandler;

import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.model.PeerFromHost;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import org.springframework.stereotype.Service;

/**
 * Это класс обёртка {@link PeerConnectHandler} чтобы можно было замокать общение с хостом в тестах.
 */
@Service
public class PeerConnectHandlerService {
    public PeerFromHost createPeerOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        return peerConnectHandler.createPeerOnHost();
    }

    public void deletePeerOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        //deleting peer on host
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.deletePeerOnHost();
    }

    public String getDownloadConfToken(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        return peerConnectHandler.getDownloadConfToken();
    }

    public void activateOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.activateOnHost();
    }

    public void deactivateOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.deactivateOnHost();
    }
}
