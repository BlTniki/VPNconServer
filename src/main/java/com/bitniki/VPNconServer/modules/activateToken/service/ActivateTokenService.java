package com.bitniki.VPNconServer.modules.activateToken.service;

import com.bitniki.VPNconServer.modules.activateToken.entity.ActivateTokenEntity;
import com.bitniki.VPNconServer.modules.activateToken.exception.ActivateTokenNotFoundException;
import com.bitniki.VPNconServer.modules.activateToken.repository.ActivateTokenRepo;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.RoleNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.user.role.Role;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("unused")
@Service
public class ActivateTokenService {
    @Autowired
    private ActivateTokenRepo activateTokenRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String genRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    public ActivateTokenEntity generate(String newRole) throws RoleNotFoundException {
        ActivateTokenEntity entity = new ActivateTokenEntity();
        entity.setToken(genRandomString());

        entity.setNewRole((Arrays.stream(Role.values())
                .filter(role -> role.name().equals(newRole))
                .findAny().orElseThrow(() ->
                        new RoleNotFoundException("That role does not exist"))));
        return activateTokenRepo.save(entity);
    }

    private String changeRole(UserEntity user, String token) throws ActivateTokenNotFoundException {
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

    public String changeRole(Long userId, String token) throws EntityNotFoundException {
        // load user
        UserEntity user = userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        return changeRole(user, token);
    }

    public String changeMineRole(Principal principal, String token) throws EntityNotFoundException {
        // load user
        UserEntity user = userRepo.findByLogin(principal.getName());
        if(user == null) throw new UserNotFoundException("User not found");

        return  changeRole(user, token);
    }
}
