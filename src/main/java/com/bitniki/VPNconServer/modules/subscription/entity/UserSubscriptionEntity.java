package com.bitniki.VPNconServer.modules.subscription.entity;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность, описывающая связь между юзером и подпиской
 */
@Entity
@Table(name = "user_subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserSubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id", nullable = false)
    private SubscriptionEntity subscription;

    /**
     * День сгорания подписки
     */
    @Column(nullable = false)
    private LocalDate expirationDay;

}
