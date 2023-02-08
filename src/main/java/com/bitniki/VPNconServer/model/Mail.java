package com.bitniki.VPNconServer.model;

import com.bitniki.VPNconServer.entity.MailEntity;

@SuppressWarnings("unused")
public class Mail {
    private  Long id;
    private User user;
    private Boolean forTelegram;
    private Boolean isChecked;
    private String payload;

    public static Mail toModel(MailEntity entity) {
        Mail model = new Mail();
        model.setId(entity.getId());
        model.setUser(User.toModel(entity.getUser()));
        model.setForTelegram(entity.getForTelegram());
        model.setChecked(entity.getChecked());
        model.setPayload(entity.getPayload());
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
