package com.bitniki.VPNconServer.modules.security.service;

import com.bitniki.VPNconServer.modules.security.model.SecurityUser;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис детализации пользователя, используемый для аутентификации и авторизации пользователей.
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    /**
     * Загружает детали пользователя по его имени пользователя.
     * @param username Имя пользователя.
     * @return Детали пользователя.
     * @throws UsernameNotFoundException Если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return SecurityUser.fromUserEntity(user);
    }
}
