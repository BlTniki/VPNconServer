package com.bitniki.VPNconServer.modules.user.model;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.role.Role;
import lombok.*;

/**
 * Модель {@link UserEntity} без внутренней информации
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    private Long id;

    private String login;

    /**
     * Роль юзера в сервисе. Определяет доступ к функционалу.
     */
    private Role role;

    /**
     * Id пользователя в Телеграм
     */
    private Long telegramId;

    /**
     * Ник пользователя в Телеграм
     */
    private String telegramNickname;

    /**
     * Статический метод создания модели из сущности.
     * @param entity сущность, на основе которой следует создать модель.
     * @return новую модель.
     */
    public static User toModel (UserEntity entity) {
        return new User(entity);
    }

    public User(UserEntity entity) {
        this.setId(entity.getId());
        this.setLogin(entity.getLogin());
        this.setRole(entity.getRole());
        this.setTelegramId(entity.getTelegramId());
        this.setTelegramNickname(entity.getTelegramNickname());
    }
}
