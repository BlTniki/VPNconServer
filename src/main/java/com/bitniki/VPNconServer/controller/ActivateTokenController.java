package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.ActivateTokenEntity;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.RoleNotFoundException;
import com.bitniki.VPNconServer.service.ActivateTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/activate_token")
public class ActivateTokenController {
    @Autowired
    private ActivateTokenService activateTokenService;

    @PostMapping("/gen/{newRole}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('gen_token')")
    public ResponseEntity<ActivateTokenEntity> generateToken(@PathVariable String newRole) throws RoleNotFoundException {
        return ResponseEntity.ok(activateTokenService.generate(newRole));
    }

    @GetMapping("/{token}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changeUserRole(Principal principal, @PathVariable String token) throws EntityNotFoundException {
        return ResponseEntity.ok(activateTokenService.changeRole(principal, token));
    }
}
