package com.bitniki.VPNconServer.modules.peer.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerAlreadyExistException;
import com.bitniki.VPNconServer.modules.peer.exception.PeerNotFoundException;
import com.bitniki.VPNconServer.modules.peer.model.Peer;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/peers")
public class PeerController {
    @Autowired
    private PeerService peerService;


    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<Peer>> getAllPeers() {
        return ResponseEntity.ok(
                StreamSupport.stream(peerService.getAll(), false)
                        .map(Peer::toModel)
                        .toList()
        );
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<Peer>> getAllMinePeers(Principal principal) {
        return ResponseEntity.ok(
                StreamSupport.stream(peerService.getAllByLogin(principal.getName()), false)
                        .map(Peer::toModel)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<Peer> getOnePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.getOneById(id))
        );
    }

    @GetMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<Peer> getOneMinePeer(@PathVariable Long id, Principal principal)
            throws EntityNotFoundException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.getOneByLoginAndId(principal.getName(), id))
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> createPeer(@RequestBody PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.create(model))
        );
    }

    @PostMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> createMinePeer(Principal principal, @RequestBody PeerFromRequest model)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.createByLogin( principal.getName(),model))
        );
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> deletePeer(@PathVariable Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.delete(id))
        );
    }

    @DeleteMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Peer> deleteMinePeer(Principal principal, @PathVariable Long id)
            throws EntityNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(
                Peer.toModel(peerService.deleteByLogin(principal.getName(), id))
        );
    }

    @GetMapping("/conf/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getDownloadToken(@PathVariable Long id) throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeerById(id));
    }

    @GetMapping("/conf/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getMineDownloadToken(Principal principal,@PathVariable Long id) throws EntityNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeerByLoginAndId(principal.getName(), id));
    }

    @PostMapping("/activate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> activatePeer(@PathVariable Long id)
            throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.activatePeerOnHostById(id));
    }

    @PostMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> deactivatePeer(@PathVariable Long id)
            throws PeerNotFoundException, PeerConnectHandlerException {
        return ResponseEntity.ok(peerService.deactivatePeerOnHostById(id));
    }
}
