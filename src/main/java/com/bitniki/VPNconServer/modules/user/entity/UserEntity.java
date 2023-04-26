package com.bitniki.VPNconServer.modules.user.entity;

import lombok.*;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table (name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
//    @Enumerated(value = EnumType.STRING)
//    private Role role;
    private String token;
    private Long telegramId;
    private String telegramFirstName;


    public UserEntity updateWith(UserEntity newUser) {
        // update field if not null
        this.setLogin((newUser.getLogin() != null) ? newUser.getLogin() : this.getLogin());
        this.setPassword((newUser.getPassword() != null) ? newUser.getPassword() : this.getPassword());
        this.setToken((newUser.getToken() != null) ? newUser.getToken() : this.getToken());
        this.setTelegramId((newUser.getTelegramId() != null)
                ? newUser.getTelegramId() : this.getTelegramId());
        this.setTelegramFirstName((newUser.getTelegramFirstName() != null)
                ? newUser.getTelegramFirstName() : this.getTelegramFirstName());
        return this;
    }
}
