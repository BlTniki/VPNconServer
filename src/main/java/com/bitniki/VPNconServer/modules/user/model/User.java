package com.bitniki.VPNconServer.modules.user.model;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import lombok.*;

@SuppressWarnings("unused")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    private Long id;
    private String login;
//    private Role role;
    private Long telegramId;
    private String telegramFirstName;

    public static User toModel (UserEntity entity) {
        return new User(entity);
    }

    public User(UserEntity entity) {
        this.setId(entity.getId());
        this.setLogin(entity.getLogin());
//        this.setRole(entity.getRole());
        this.setTelegramId(entity.getTelegramId());
        this.setTelegramFirstName(entity.getTelegramFirstName());
    }
}
