package com.bitniki.VPNconServer.modules.payment.entity;

import com.bitniki.VPNconServer.modules.payment.status.PaymentStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentEntity {
    /**
     * Идентификатор платежа, генерируется на основе полей userId, subscriptionId, toPay и timeStamp.
     */
    @Id
    private String uuid;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long subscriptionId;

    /**
     * Сумма к оплате
     */
    @Column(nullable = false, columnDefinition = "VARCHAR")
    private BigDecimal toPay;
    @Column(nullable = false)
    private LocalDateTime timeStamp;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;
}
