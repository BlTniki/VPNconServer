package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.HostAlreadyExistException;
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

    @GetMapping
    public ResponseEntity<String> getHost() {
        try {
            return ResponseEntity.ok().body("all good");
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
