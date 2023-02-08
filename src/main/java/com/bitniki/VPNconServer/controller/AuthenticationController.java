package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.UserValidationFailedException;
import com.bitniki.VPNconServer.model.AuthRequest;
import com.bitniki.VPNconServer.model.UserWithRelations;
import com.bitniki.VPNconServer.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> auth(@RequestBody AuthRequest request) throws UserNotFoundException {
        return ResponseEntity.ok(authenticationService.authAndCreateToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException {
        authenticationService.logout(request, response);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/tg")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<UserWithRelations> associateTelegramIdWithUser(@RequestBody UserEntity user)
            throws UserNotFoundException, UserValidationFailedException {

        return ResponseEntity.ok(authenticationService.associateTelegramId(user));
    }

    @DeleteMapping("/tg")
    @PreAuthorize("hasAuthority('any')")
    public ResponseEntity<UserWithRelations> dissociateTelegramIdWithUser(@RequestBody UserEntity user)
            throws UserNotFoundException {

        return ResponseEntity.ok(authenticationService.dissociateTelegramId(user));
    }
}
