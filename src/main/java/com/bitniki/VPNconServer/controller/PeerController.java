package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.exception.alreadyExistException.PeerAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.PeerNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.PeerValidationFailedException;
import com.bitniki.VPNconServer.model.PeerWithAllRelations;
import com.bitniki.VPNconServer.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peers")
public class PeerController {
    @Autowired
    private PeerService peerService;


    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<PeerWithAllRelations>> getAllPeers() {
        return ResponseEntity.ok(peerService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<PeerWithAllRelations> getOnePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.getOne(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> createPeer(@RequestParam Long user_id,
                                                           @RequestParam Long host_id,
                                                           @RequestBody PeerEntity peerEntity)
            throws EntityNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        return ResponseEntity.ok(peerService.create(user_id, host_id, peerEntity));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> updatePeer(@PathVariable Long id, @RequestBody PeerEntity peer)
            throws PeerNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        return ResponseEntity.ok(peerService.update(id, peer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> deletePeer(@PathVariable Long id) throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.delete(id));
    }
}
