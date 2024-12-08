package ru.bitniki.sms.domain.subscriptions.dto;

public record UserSubscriptionEvent(
    Long userId,
    Type type,
    UserSubscriptionEvent.Subscription subscription
) {
    enum Type {
        PAID,
        BURN_SOON,
        BURNED
    }

    record Subscription(
        int allowedActivePeersCount
    ) {}
}
