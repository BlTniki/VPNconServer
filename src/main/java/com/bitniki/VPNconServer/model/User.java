package com.bitniki.VPNconServer.model;
import com.bitniki.VPNconServer.entity.UserEntity;

public class User {
    private Long id;
    private String login;
    private Role role;


    public static User toModel (UserEntity entity) {
        User model = new User();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());


        return model;
    }

    public User() {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
