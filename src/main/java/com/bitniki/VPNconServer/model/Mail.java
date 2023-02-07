package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.MailEntity;
import com.bitniki.VPNconServer.entity.UserEntity;

@SuppressWarnings("unused")
public class Mail {
    private  Long id;
    private UserEntity user;
    private Boolean forTelegram;
    private Boolean isChecked;

    public static Mail toModel(MailEntity entity) {
        Mail model = new Mail();
        model.setId(entity.getId());
        model.setUser(entity.getUser());
        model.setForTelegram(entity.getForTelegram());
        model.setChecked(entity.getChecked());
        return model;
    }

    public Mail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Boolean getForTelegram() {
        return forTelegram;
    }

    public void setForTelegram(Boolean forTelegram) {
        this.forTelegram = forTelegram;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
