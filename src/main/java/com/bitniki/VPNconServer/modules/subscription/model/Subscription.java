package com.bitniki.VPNconServer.modules.subscription.model;

import com.bitniki.VPNconServer.modules.role.Role;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;

import lombok.*;

/**
 * Модель {@link SubscriptionEntity}.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Subscription {
    private Long id;

    /**
     * Роль юзера, для которого предназначена подписка
     */
    private Role role;

    /**
     * Цена в рублях
     */
    private Integer priceInRub = 0;

    /**
     * Максимум доступных к созданию пиров для юзера
     */
    private Integer peersAvailable = 0;

    /**
     * Длительность подписки в кол-ве дней
     */
    private Integer durationDays = 0;

    public static Subscription toModel(SubscriptionEntity entity) {
        return new Subscription(entity);
    }

    public Subscription(SubscriptionEntity entity) {
        this.setId(entity.getId());
        this.setRole(entity.getRole());
        this.setPriceInRub(entity.getPriceInRub());
        this.setPeersAvailable(entity.getPeersAvailable());
        this.setDurationDays(entity.getDurationDays());
    }

}
