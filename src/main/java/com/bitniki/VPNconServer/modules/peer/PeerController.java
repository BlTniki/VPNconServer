package com.bitniki.VPNconServer.modules.peer;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.exception.EntityValidationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@SuppressWarnings("unused")
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

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<List<PeerWithAllRelations>> getAllMinePeers(Principal principal) throws UserNotFoundException {
        return ResponseEntity.ok(peerService.getAll(principal));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<PeerWithAllRelations> getOnePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.getOne(id));
    }

    @GetMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<PeerWithAllRelations> getOneMinePeer(@PathVariable Long id, Principal principal)
            throws EntityNotFoundException {
        return ResponseEntity.ok(peerService.getOne(id, principal));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> createPeer(@RequestParam Long user_id,
                                                           @RequestParam Long host_id,
                                                           @RequestBody PeerEntity peerEntity)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException {
        return ResponseEntity.ok(peerService.create(user_id, host_id, peerEntity));
    }

    @PostMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> createMinePeer( Principal principal,
                                                                @RequestParam Long host_id,
                                                                @RequestBody PeerEntity peerEntity)
            throws EntityNotFoundException, PeerAlreadyExistException, EntityValidationFailedException {
        return ResponseEntity.ok(peerService.create(principal, host_id, peerEntity));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> updatePeer(@PathVariable Long id, @RequestBody PeerEntity peer)
            throws PeerNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        return ResponseEntity.ok(peerService.update(id, peer));
    }

    @PutMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> updateMinePeer(Principal principal,
                                                               @PathVariable Long id,
                                                               @RequestBody PeerEntity peer)
            throws EntityNotFoundException, PeerAlreadyExistException, PeerValidationFailedException {
        return ResponseEntity.ok(peerService.update(principal, id, peer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> deletePeer(@PathVariable Long id) throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.delete(id));
    }

    @DeleteMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<PeerWithAllRelations> deleteMinePeer(Principal principal, @PathVariable Long id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(peerService.delete(principal, id));
    }

    @GetMapping("/conf/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getDownloadToken(@PathVariable Long id) throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeer(id));
    }

    @GetMapping("/conf/mine/{id}")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('peer:read')")
    public ResponseEntity<String> getMineDownloadToken(Principal principal,@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(peerService.getDownloadTokenForPeer(principal, id));
    }

    @PostMapping("/activate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> activatePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.activatePeerOnHost(id));
    }

    @PostMapping("/deactivate/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('peer:write')")
    public ResponseEntity<Boolean> deactivatePeer(@PathVariable Long id)
            throws PeerNotFoundException {
        return ResponseEntity.ok(peerService.deactivatePeerOnHost(id));
    }
}
