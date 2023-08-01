package com.bitniki.VPNconServer.modules.subscription.model;

import com.bitniki.VPNconServer.modules.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель из реквеста для создания/обновления сущности.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionFromRequest {
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
}
