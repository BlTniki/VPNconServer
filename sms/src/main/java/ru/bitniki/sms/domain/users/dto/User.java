package ru.bitniki.sms.domain.users.dto;

import jakarta.validation.constraints.NotNull;

public record User(
    @NotNull Long telegramId,
    @NotNull String username,
    @NotNull String role
) {}
