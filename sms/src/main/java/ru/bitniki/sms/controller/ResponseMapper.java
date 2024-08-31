package ru.bitniki.sms.controller;

import ru.bitniki.sms.controller.model.SubscriptionResponse;
import ru.bitniki.sms.controller.model.UserResponse;
import ru.bitniki.sms.controller.model.UserSubscriptionResponse;
import ru.bitniki.sms.domen.subscriptions.dto.Subscription;
import ru.bitniki.sms.domen.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domen.users.dto.User;

/**
 * Utils class for mapping domain models to response models.
 */
public final class ResponseMapper {

    private ResponseMapper() {
    }

    public static UserResponse toUserResponse(User dto) {
        return UserResponse.builder()
                .telegramId(dto.telegramId())
                .username(dto.username())
                .role(UserResponse.RoleEnum.valueOf(dto.role()))
                .build();
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.id())
                .role(SubscriptionResponse.RoleEnum.fromValue(subscription.role()))
                .priceInRubles(subscription.priceInRubles())
                .allowedActivePeersCount(subscription.allowedActivePeersCount())
                .period(subscription.period())
                .build();
    }

    public static UserSubscriptionResponse toUserSubscriptionResponse(UserSubscription dto) {
        return UserSubscriptionResponse.builder()
                .id(dto.id())
                .user(toUserResponse(dto.user()))
                .subscription(toSubscriptionResponse(dto.subscription()))
                .expirationDate(dto.expirationDate())
                .build();
    }
}
