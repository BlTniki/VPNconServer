package ru.bitniki.sms.domen.subscriptions.dto;

import jakarta.validation.constraints.NotNull;
import ru.bitniki.sms.domen.users.dto.User;

public record UserSubscription(
    @NotNull Long id,
    @NotNull User user,
    @NotNull Subscription subscription
) {}
