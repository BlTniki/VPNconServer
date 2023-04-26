package com.bitniki.VPNconServer.modules.user.controller;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserAlreadyExistException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.User;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('any')" +
                    "&& hasAuthority('user:read')")
    public ResponseEntity<List<User>> getAllUsers() {
            return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.getOne(id));
    }

    @GetMapping("/tg/{telegramId}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable Long telegramId)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.getOneByTelegramId(telegramId));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:read')")
    public ResponseEntity<User> getMineUser(Principal principal)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.getOne(principal));
    }

    @PostMapping
    public ResponseEntity<User> createUser (@RequestBody UserEntity user)
            throws UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserEntity user)
            throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @PutMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> updateMineUser(Principal principal, @RequestBody UserEntity user)
            throws UserNotFoundException, UserAlreadyExistException, UserValidationFailedException {
        return ResponseEntity.ok(userService.update(principal, user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('any')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> deleteUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.delete(id));
    }

    @DeleteMapping("/mine")
    @PreAuthorize("hasAuthority('personal')" +
            "&& hasAuthority('user:write')")
    public ResponseEntity<User> deleteMineUser(Principal principal)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.delete(principal));
    }


}
