package com.bitniki.VPNconServer.entity;

import com.bitniki.VPNconServer.model.Role;

import javax.persistence.*;

@Entity
@Table(name = "activate_token")
public class ActivateTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @Enumerated(value = EnumType.STRING)
    private Role newRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getNewRole() {
        return newRole;
    }

    public void setNewRole(Role newRole) {
        this.newRole = newRole;
    }
}
