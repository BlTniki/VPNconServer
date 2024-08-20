package ru.bitniki.sms.domen.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "users")
public class R2dbcUserEntity {
    @Id
    private Long id;
    private Long telegramId;
    private String username;
    private String role;
}

