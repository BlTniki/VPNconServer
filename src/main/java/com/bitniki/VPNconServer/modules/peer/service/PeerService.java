package com.bitniki.VPNconServer.modules.peer.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;

import com.bitniki.VPNconServer.modules.peer.exception.PeerValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import org.springframework.stereotype.Service;

import java.util.Spliterator;

@Service
public interface PeerService {
    Spliterator<PeerEntity> getAll();
    Spliterator<PeerEntity> getAllByLogin(String login);
    PeerEntity getOne(Long id) throws PeerNotFoundException;
    PeerEntity getOne(String login, Long id) throws EntityNotFoundException;
    PeerEntity create(PeerFromRequest model)
            throws EntityValidationFailedException, PeerAlreadyExistException, EntityNotFoundException;
    PeerEntity create(String login, PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException;
    PeerEntity update(Long id, PeerFromRequest newPeerModel)
            throws PeerNotFoundException, PeerAlreadyExistException, PeerValidationFailedException;
    PeerEntity update(String login, Long id, PeerFromRequest newPeerModel)
            throws EntityNotFoundException, PeerAlreadyExistException, PeerValidationFailedException;
    PeerEntity delete(Long id) throws PeerNotFoundException;
    PeerEntity delete(String login, Long id) throws EntityNotFoundException;
    String getDownloadTokenForPeer(Long id) throws PeerNotFoundException;
    String getDownloadTokenForPeer(String login, Long id) throws EntityNotFoundException;
    Boolean activatePeerOnHost(Long id) throws PeerNotFoundException;
    Boolean deactivatePeerOnHost(Long id) throws PeerNotFoundException;
}
