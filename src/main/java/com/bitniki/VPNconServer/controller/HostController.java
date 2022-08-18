package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.HostAlreadyExistException;
import com.bitniki.VPNconServer.exception.HostNotFoundException;
import com.bitniki.VPNconServer.model.Host;
import com.bitniki.VPNconServer.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hosts")
public class HostController {
    @Autowired
    private HostService hostService;

    @SuppressWarnings("rawtypes")
    @GetMapping
    public ResponseEntity getAllHosts() {
        try {
            return ResponseEntity.ok(hostService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/{id}")
    public ResponseEntity getHost (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(hostService.getOne(id));
        } catch (HostNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }
    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity createHost(@RequestBody HostEntity host) {
        try {
            return ResponseEntity.ok(hostService.create(host));
        } catch (HostAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("error");
        }
    }
}
