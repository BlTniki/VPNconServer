package com.bitniki.VPNconServer.modules.subscription.model;

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
public class UserSubscriptionFromRequest {

    /**
     * Id юзера
     */
    private Long userId;

    /**
     * Id подписки
     */
    private Long subscriptionId;
}
