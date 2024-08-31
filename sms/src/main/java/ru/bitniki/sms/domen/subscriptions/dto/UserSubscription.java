package ru.bitniki.sms.domen.subscriptions.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import ru.bitniki.sms.domen.users.dto.User;

public record UserSubscription(
    @NotNull Long id,
    @NotNull User user,
    @NotNull Subscription subscription,
    @NotNull LocalDate expirationDate
) {}
