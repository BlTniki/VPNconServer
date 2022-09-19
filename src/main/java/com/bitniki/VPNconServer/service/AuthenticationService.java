package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.model.AuthRequest;
import com.bitniki.VPNconServer.repository.UserRepo;
import com.bitniki.VPNconServer.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Map<Object, Object> authAndCreateToken(AuthRequest request) throws UserNotFoundException {
        // auth user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        // load user from repo
        UserEntity user = userRepo.findByLogin(request.getLogin());
        if(user == null) throw new UserNotFoundException("User not found");

        // generate token
        String token = jwtTokenProvider.createToken(request.getLogin(), user.getRole().name());

        // make response
        Map<Object, Object> response = new HashMap<>();
        response.put("login", request.getLogin());
        response.put("token", token);
        return response;
    }
}
