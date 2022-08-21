package com.bitniki.VPNconServer.model;
import com.bitniki.VPNconServer.entity.UserEntity;

public class UserPeer {
    private Long id;
    private String login;


    public static UserPeer toModel (UserEntity entity) {
        UserPeer model = new UserPeer();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());


        return model;
    }

    public UserPeer() {
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
}
