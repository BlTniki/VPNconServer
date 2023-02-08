package com.bitniki.VPNconServer.entity;

import com.bitniki.VPNconServer.role.Role;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table (name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String token;
    private Long telegramId;
    private String telegramUsername;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PeerEntity> peerEntities;
    private LocalDate subscriptionExpirationDay;
    @ManyToOne
    private SubscriptionEntity subscription;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<MailEntity> mails;



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

    public List<MailEntity> getMails() {
        return mails;
    }

    public void setMails(List<MailEntity> mails) {
        this.mails = mails;
    }
}
