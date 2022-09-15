package com.bitniki.VPNconServer.security;

import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByLogin(username);
        if(user == null) throw new UsernameNotFoundException("User not found");

        return SecurityUser.fromUserEntity(user);
    }
}
