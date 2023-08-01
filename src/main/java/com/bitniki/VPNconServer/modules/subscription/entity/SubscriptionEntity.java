package com.bitniki.VPNconServer.modules.subscription.entity;

import com.bitniki.VPNconServer.modules.role.Role;
import lombok.*;

import javax.persistence.*;

/**
 * Сущность, описывающая подписку
 */
@Entity
@Table(name = "subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Роль юзера, для которого предназначена подписка
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    /**
     * Цена в рублях
     */
    @Column(nullable = false)
    private Integer priceInRub = 0;

    /**
     * Максимум доступных к созданию пиров для юзера
     */
    @Column(nullable = false)
    private Integer peersAvailable = 0;

    /**
     * Длительность подписки в кол-ве дней
     */
    @Column(nullable = false)
    private Integer durationDays = 0;

    public void setRole(Role role) {
        this.role = role;
    }
    public void setRole(String role) {
        this.role = Role.valueOf(role);
    }

}
