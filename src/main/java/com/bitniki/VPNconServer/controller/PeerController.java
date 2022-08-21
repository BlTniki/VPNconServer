package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.PeerEntity;
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }
}
