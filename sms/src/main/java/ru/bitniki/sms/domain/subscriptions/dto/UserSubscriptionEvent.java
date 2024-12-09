package ru.bitniki.sms.domain.subscriptions.dto;

public record UserSubscriptionEvent(
    Long userId,
    Type type,
    UserSubscriptionEvent.Subscription subscription
) {
    public enum Type {
        PAID,
        BURN_SOON,
        BURNED
    }

    public record Subscription(
        int allowedActivePeersCount
    ) {}
}
