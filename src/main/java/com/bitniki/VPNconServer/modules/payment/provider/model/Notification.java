package com.bitniki.VPNconServer.modules.payment.provider.model;

import java.math.BigDecimal;

/**
 * Базовый класс для уведомления от платёжного провайдера.
 */
public abstract class Notification {
    public abstract BigDecimal getAmount();
    public abstract String getUuid();

    @Override
    public String toString() {
        return "Notification{\namount=%s\nuuid=%s\n}".formatted(getAmount().toString(), getUuid());
    }
}
