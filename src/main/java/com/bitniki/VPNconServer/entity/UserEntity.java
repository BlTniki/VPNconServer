package com.bitniki.VPNconServer.entity;

import com.bitniki.VPNconServer.model.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String token;
    private Long telegramId;
    private String telegramUsername;
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.DISABLED_USER;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true)
    private List<PeerEntity> peerEntities;



    public static UserEntity updateEntity(UserEntity oldUser, UserEntity newUser) {
        // update field if not null
        oldUser.setLogin((newUser.getLogin() != null) ? newUser.getLogin() : oldUser.getLogin());
        oldUser.setPassword((newUser.getPassword() != null) ? newUser.getPassword() : oldUser.getPassword());
        oldUser.setToken((newUser.getToken() != null) ? newUser.getToken() : oldUser.getToken());
        oldUser.setTelegramId((newUser.getTelegramId() != null)
                ? newUser.getTelegramId() : oldUser.getTelegramId());
        oldUser.setTelegramUsername((newUser.getTelegramUsername() != null)
                ? newUser.getTelegramUsername() : oldUser.getTelegramUsername());
        return oldUser;
    }

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PeerEntity> getPeers() {
        return peerEntities;
    }

    public void setPeers(List<PeerEntity> peerEntities) {
        this.peerEntities = peerEntities;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }
}
