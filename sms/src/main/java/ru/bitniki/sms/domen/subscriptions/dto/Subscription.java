package ru.bitniki.sms.domen.subscriptions.dto;

import java.math.BigDecimal;
import java.time.Period;

public record Subscription(
    Long id,
    String role,
    BigDecimal priceInRubles,
    Integer allowedActivePeersCount,
    Period period
) {}
