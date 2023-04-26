package com.bitniki.VPNconServer.modules.user.entity;

import com.bitniki.VPNconServer.modules.mail.entity.MailEntity;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table (name = "user")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
