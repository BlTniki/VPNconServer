package com.bitniki.VPNconServer.modules.subscription.model;

import com.bitniki.VPNconServer.modules.subscription.entity.UserSubscriptionEntity;
import com.bitniki.VPNconServer.modules.user.model.User;
import lombok.*;

import java.time.LocalDate;

/**
 * Модель {@link UserSubscriptionEntity}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserSubscription {
    private Long id;

    private User user;

    private Subscription subscription;

    /**
     * День сгорания подписки
     */
    private LocalDate expirationDay;

    public static UserSubscription toModel(UserSubscriptionEntity entity) {
        return new UserSubscription(entity);
    }

    public UserSubscription(UserSubscriptionEntity entity) {
        this.setId(entity.getId());
        this.setUser( User.toModel(entity.getUser()) );
        this.setSubscription( Subscription.toModel(entity.getSubscription()) );
        this.setExpirationDay(entity.getExpirationDay());
    }
}
