package com.bitniki.VPNconServer.modules.mail.entity;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table (name = "mail")
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @ManyToOne
    private UserEntity user;
    @Column(nullable = false)
    private Boolean forTelegram = false;
    @Column(nullable = false)
    private Boolean isChecked = false;
    @Column(nullable = false)
    private String payload;

    public MailEntity() {
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
