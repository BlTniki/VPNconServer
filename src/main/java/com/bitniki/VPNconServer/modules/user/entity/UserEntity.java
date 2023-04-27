package com.bitniki.VPNconServer.modules.user.entity;

import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Field;

/**
 * Сущность юзера.
 */
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
    /**
     * JWT токен, используемый для авторизации.
     */
    private String token;

    /**
     * Id пользователя в Телеграм
     */
    private Long telegramId;

    /**
     * Имя пользователя в Телеграм
     */
    private String telegramFirstName;

    /**
     * Ник пользователя в Телеграм
     */
    private String telegramNickname;

    /**
     * Обновляет посредством рефлексии поля этого объекта UserEntity ненулевыми полями данного объекта UserEntity.
     * @param newUser объект UserEntity, ненулевые поля которого будут использоваться для обновления полей этого объекта.
     * @return обновленный объект UserEntity.
     * @throws RuntimeException если произошла ошибка при доступе к полям через рефлексию.
     */
    public UserEntity updateWith(UserEntity newUser) {
        //get all fields
        Field[] fields = UserEntity.class.getDeclaredFields();

        //replace in this entity all fields that not null in new entity
        for (Field field: fields) {
            try {
                if (field.get(newUser) != null) {
                    field.set(this, field.get(newUser));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
}
