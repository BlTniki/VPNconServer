package com.bitniki.VPNconServer.security;

import com.bitniki.VPNconServer.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;
    private final String token;

    public static UserDetails fromUserEntity(UserEntity user) {
        return new SecurityUser(user.getLogin(), user.getPassword(), user.getRole().getAuthorities(), user.getToken());
    }

    public SecurityUser(String username, String password, Set<SimpleGrantedAuthority> authorities, String token) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }
}
