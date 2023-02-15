package com.bitniki.VPNconServer.config;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class QiwiPaymentsConfig {
    @Value("${qiwi.secretKey}")
    private String SECRET_KEY;

    @Bean
    public BillPaymentClient billPaymentClientBean() {
        return new BillPaymentClient(SECRET_KEY);
    }
}
