package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.exception.alreadyExistException.HostAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.HostNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.HostValidationFailedException;
import com.bitniki.VPNconServer.model.Host;
import com.bitniki.VPNconServer.model.HostWithRelations;
import com.bitniki.VPNconServer.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hosts")
public class HostController {
    @Autowired
    private HostService hostService;

    @GetMapping
    @PreAuthorize("hasAuthority('host:read')")
    public ResponseEntity<List<HostWithRelations>> getAllHosts() {
        return ResponseEntity.ok(hostService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('host:read')")
    public ResponseEntity<HostWithRelations> getHost (@PathVariable Long id)
            throws HostNotFoundException {
        return ResponseEntity.ok(hostService.getOne(id));
    }
    @PostMapping
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> createHost(@RequestBody HostEntity host)
            throws HostAlreadyExistException, HostValidationFailedException {
        return ResponseEntity.ok(hostService.create(host));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> updateHost (@PathVariable Long id, @RequestBody HostEntity host)
            throws HostNotFoundException, HostAlreadyExistException, HostValidationFailedException {
        return ResponseEntity.ok(hostService.update(id, host));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('host:write')")
    public ResponseEntity<Host> deleteHost (@PathVariable Long id) throws HostNotFoundException {
        return ResponseEntity.ok(hostService.delete(id));
    }
}
