package ru.bitniki.sms.domen.subscriptions.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@AllArgsConstructor
@ToString
@Table(name = "users_subscriptions")
public class R2dbcUserSubscriptionEntity {
    @Id
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private LocalDate expirationDate;
}
