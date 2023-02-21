package com.bitniki.VPNconServer.modules.authentication.service;

import com.bitniki.VPNconServer.modules.authentication.model.AuthRequest;
import com.bitniki.VPNconServer.modules.security.JwtTokenProvider;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserValidationFailedException;
import com.bitniki.VPNconServer.modules.user.model.UserWithRelations;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
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
        // set token_expired false and save
        user.setToken(token);
        userRepo.save(user);

        // make response
        Map<Object, Object> response = new HashMap<>();
        response.put("login", request.getLogin());
        response.put("token", token);
        return response;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundException {
        // load user from repo
        UserEntity user = userRepo.findByLogin(request.getUserPrincipal().getName());
        if(user == null) throw new UserNotFoundException("User not found");
        // set null and save
        user.setToken(null);
        userRepo.save(user);
    }

    public UserWithRelations associateTelegramId(UserEntity user) throws UserNotFoundException, UserValidationFailedException {
        //load user
        UserEntity userEntity = userRepo.findByLogin(user.getLogin());
        if(userEntity == null) throw new UserNotFoundException("User not found");
        // set telegram id and save
        if(user.getTelegramId() == null && user.getTelegramUsername() == null)
            throw new UserValidationFailedException("No telegramId or telegramUsername of is given");
        userEntity.setTelegramId(user.getTelegramId());
        userEntity.setTelegramUsername(user.getTelegramUsername());
        return UserWithRelations.toModel(userRepo.save(userEntity));
    }

    public UserWithRelations dissociateTelegramId(UserEntity user) throws UserNotFoundException {
        //load user
        UserEntity userEntity = userRepo.findByLogin(user.getLogin());
        if(userEntity == null) throw new UserNotFoundException("User not found");
        // del telegram id and save
        userEntity.setTelegramId(null);
        userEntity.setTelegramUsername(null);
        return UserWithRelations.toModel(userRepo.save(userEntity));
    }
}
