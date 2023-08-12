package com.bitniki.VPNconServer.modules.user.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Token {
    /**
     * Логин юзера, для которого предназначен токен.
     */
    private String login;

    /**
     * Токен юзера.
     */
    private String token;
}
