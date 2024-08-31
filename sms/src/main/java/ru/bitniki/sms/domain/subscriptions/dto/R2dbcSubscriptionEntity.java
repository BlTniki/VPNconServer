package ru.bitniki.sms.domain.subscriptions.dto;

import io.r2dbc.postgresql.codec.Interval;
import java.math.BigDecimal;
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
@Table(name = "subscriptions")
public class R2dbcSubscriptionEntity {
    @Id
    private Long id;
    private String role;
    private BigDecimal priceInRubles;
    private Integer allowedActivePeersCount;
    private Interval period;
}
