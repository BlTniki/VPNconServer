package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.exception.alreadyExistException.EntityAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/peers")
public class PeerController {
    @Autowired
    private PeerService peerService;

    @SuppressWarnings("rawtypes")
    @GetMapping
    public ResponseEntity getAllPeers() {
        try {
            return ResponseEntity.ok(peerService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity createPeer(@RequestParam Long user_id, @RequestParam Long host_id, @RequestBody PeerEntity peerEntity) {
        try {
            return ResponseEntity.ok(peerService.create(user_id, host_id, peerEntity));
        } catch (EntityNotFoundException | EntityAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }
}
