package com.bitniki.VPNconServer.controller;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.alreadyExistException.UserAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.model.User;
import com.bitniki.VPNconServer.model.UserWithRelations;
import com.bitniki.VPNconServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserWithRelations>> getAllUsers() {
            return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWithRelations> getUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser (@RequestBody UserEntity user)
            throws UserAlreadyExistException {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserEntity user)
            throws UserNotFoundException, UserAlreadyExistException {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id)
            throws UserNotFoundException {
        return ResponseEntity.ok(userService.delete(id));
    }
}
