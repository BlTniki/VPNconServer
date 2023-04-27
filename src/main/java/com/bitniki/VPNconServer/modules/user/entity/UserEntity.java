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
        //get all fields
        var fields = UserEntity.class.getDeclaredFields();

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
