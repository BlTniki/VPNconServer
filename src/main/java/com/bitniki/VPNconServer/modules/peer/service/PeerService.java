package com.bitniki.VPNconServer.modules.peer.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;

public interface PeerService {
    Spliterator<PeerEntity> getAll();
    Spliterator<PeerEntity> getAllByLogin(@NotNull String login);
    PeerEntity getOneById(@NotNull Long id) throws PeerNotFoundException;
    PeerEntity getOneByLoginAndId(@NotNull String login, @NotNull Long id) throws EntityNotFoundException;
    PeerEntity create(@NotNull PeerFromRequest model)
            throws EntityValidationFailedException, PeerAlreadyExistException, EntityNotFoundException, PeerConnectHandlerException;
    PeerEntity createByLogin(@NotNull String login, @NotNull PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException;
    PeerEntity delete(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;
    PeerEntity delete(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException;
    String getDownloadTokenForPeer(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;
    String getDownloadTokenForPeer(@NotNull String login, @NotNull Long id) throws EntityNotFoundException, PeerConnectHandlerException;
    Boolean activatePeerOnHost(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;
    Boolean deactivatePeerOnHost(@NotNull Long id) throws PeerNotFoundException, PeerConnectHandlerException;
}
