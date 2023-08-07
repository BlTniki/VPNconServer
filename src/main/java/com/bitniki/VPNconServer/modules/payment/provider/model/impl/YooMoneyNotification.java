package com.bitniki.VPNconServer.modules.payment.provider.model.impl;

import com.bitniki.VPNconServer.modules.payment.provider.model.Notification;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public class YooMoneyNotification extends Notification {
    private final String notification_type;
    private final String operation_id;
    private final BigDecimal amount;
    private final BigDecimal withdraw_amount;
    private final String currency;
    private final LocalDateTime datetime;
    private final String sender;
    private final Boolean codepro;
    private final String label;
    private final String sha1_hash;

    @Override
    public BigDecimal getAmount() {
        return withdraw_amount;
    }

    @Override
    public String getUuid() {
        return label;
    }

    public String getSha1_hash() {
        return sha1_hash;
    }

    public String toStringForHash(String notification_secret) {
        return notification_type + "&" +
                operation_id + "&" +
                amount + "&" +
                currency + "&" +
                datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")) + "&" +
                sender + "&" +
                codepro + "&" +
                notification_secret + "&" +
                label;
    }
}
