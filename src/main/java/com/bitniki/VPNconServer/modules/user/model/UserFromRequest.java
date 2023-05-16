package com.bitniki.VPNconServer.modules.user.model;

import lombok.*;

/**
 * Model for request body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFromRequest {

    /**
     * Логин юзера. Уникальный. Должен быть минимум в 3 символа длиной,
     * начинаться с латинского символа и состоять из: латинского алфавита и, опционально, знаков "_" и "."
     */
    private String login;

    /**
     * Пароль юзера. Должен быть минимум в 6 символа длиной и содержать:
     * Строчную и прописную латинский символ,
     * Цифры
     */
    private String password;

    /**
     * Id юзера в Telegram.
     */
    private Long telegramId;

    /**
     * Nickname юзера в Telegram.
     */
    private String telegramNickname;
}
