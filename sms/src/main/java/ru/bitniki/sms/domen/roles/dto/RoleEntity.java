package ru.bitniki.sms.domen.roles.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@Table(name = "roles")
public class RoleEntity {
    @Id
    private String role;
}
