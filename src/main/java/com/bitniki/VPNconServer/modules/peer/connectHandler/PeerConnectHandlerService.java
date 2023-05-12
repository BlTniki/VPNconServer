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

    /**
     * Создать пира на хосте.
     * @param peer сущность {@link PeerEntity}.
     * @return Ответ хоста в виде {@link PeerFromHost} с заполненными private и public ключами.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public PeerFromHost createPeerOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        return peerConnectHandler.createPeerOnHost();
    }

    /**
     * Удаляет пира на хосте.
     * @param peer сущность {@link PeerEntity}.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void deletePeerOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        //deleting peer on host
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.deletePeerOnHost();
    }

    /**
     * Создаёт токен для скачивания конфига для туннеля с хоста.
     * @param peer сущность {@link PeerEntity}.
     * @return Токен в виде url-safe строки.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public String getDownloadConfToken(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        return peerConnectHandler.getDownloadConfToken();
    }

    /**
     * Активирует пира на хосте.
     * @param peer сущность {@link PeerEntity}.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void activateOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.activateOnHost();
    }

    /**
     * Деактивирует пира на хосте.
     * @param peer сущность {@link PeerEntity}.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void deactivateOnHost(PeerEntity peer) throws PeerConnectHandlerException {
        PeerConnectHandler peerConnectHandler = new PeerConnectHandler(peer);
        peerConnectHandler.deactivateOnHost();
    }
}
