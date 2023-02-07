package com.bitniki.VPNconServer.model;
import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.role.Role;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class User {
    private Long id;
    private String login;
    private Role role;
    private Long telegramId;
    private String telegramUsername;
    private LocalDate subscriptionExpirationDay;
    private SubscriptionEntity subscription;

    public static User toModel (UserEntity entity) {
        return new User(entity);
    }

    public User() {
    }

    public User(UserEntity entity) {
        this.setId(entity.getId());
        this.setLogin(entity.getLogin());
        this.setRole(entity.getRole());
        this.setTelegramId(entity.getTelegramId());
        this.setTelegramUsername(entity.getTelegramUsername());
        this.setSubscriptionExpirationDay(entity.getSubscriptionExpirationDay());
        this.setSubscription(entity.getSubscription());
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

    public LocalDate getSubscriptionExpirationDay() {
        return subscriptionExpirationDay;
    }

    public void setSubscriptionExpirationDay(LocalDate subscriptionExpirationDay) {
        this.subscriptionExpirationDay = subscriptionExpirationDay;
    }

    public SubscriptionEntity getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionEntity subscription) {
        this.subscription = subscription;
    }
}
