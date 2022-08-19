package com.bitniki.VPNconServer.entity;

import javax.persistence.*;

@Entity
@Table (name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;


    public static void updateEntity(UserEntity oldUser, UserEntity newUser) {
        oldUser.setLogin((newUser.getLogin() != null) ? newUser.getLogin() : oldUser.getLogin());
        oldUser.setPassword((newUser.getPassword() != null) ? newUser.getPassword() : oldUser.getPassword());
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
}
