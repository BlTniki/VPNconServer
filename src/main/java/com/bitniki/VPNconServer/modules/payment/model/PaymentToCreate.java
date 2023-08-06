package com.bitniki.VPNconServer.modules.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Тело реквеста на создание платежа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentToCreate {
    /**
     * Id юзера, к которому следует создать добавить подписку после оплаты.
     */
    private Long userId;

    /**
     * Id подписки, которую следует добавить юзеру после оплаты.
     */
    private Long subscriptionId;
}
