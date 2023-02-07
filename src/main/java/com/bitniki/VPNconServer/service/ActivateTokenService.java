package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.ActivateTokenEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.ActivateTokenNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.RoleNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.role.Role;
import com.bitniki.VPNconServer.repository.ActivateTokenRepo;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.Random;

@Service
public class ActivateTokenService {
    @Autowired
    private ActivateTokenRepo activateTokenRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String genRandomString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    public ActivateTokenEntity generate(String newRole) throws RoleNotFoundException {
        ActivateTokenEntity entity = new ActivateTokenEntity();
        entity.setToken(passwordEncoder
                .encode(genRandomString(10))
                .replaceAll("/", "_"));

        entity.setNewRole((Arrays.stream(Role.values())
                .filter(role -> role.name().equals(newRole))
                .findAny().orElseThrow(() ->
                        new RoleNotFoundException("That role does not exist"))));
        return activateTokenRepo.save(entity);
    }

    public String changeRole(Principal principal, String token) throws EntityNotFoundException {
        // load user
        UserEntity user = userRepo.findByLogin(principal.getName());
        if(user == null) throw new UserNotFoundException("User not found");

        // load activate token
        ActivateTokenEntity activateToken = activateTokenRepo.findByToken(token).orElseThrow(
                () -> new ActivateTokenNotFoundException("Activate token not found")
        );

        // set new role
        user.setRole(activateToken.getNewRole());
        userRepo.save(user);
        activateTokenRepo.delete(activateToken);
        return "Success";
    }
}
