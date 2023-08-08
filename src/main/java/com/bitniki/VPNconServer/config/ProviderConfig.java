package com.bitniki.VPNconServer.config;

import com.bitniki.VPNconServer.modules.payment.provider.provider.Provider;
import com.bitniki.VPNconServer.modules.payment.provider.provider.impl.YooMoneyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {
    @Value("${yoomoney.secretKey}")
    private String YOOMONEY_SECRET_KEY;
    @Value("${yoomoney.account}")
    private String YOOMONEY_ACCOUNT;
    @Value("${yoomoney.successUrl}")
    private String SUCCESS_URL;

    @Bean
    public Provider providerBean() {
        return new YooMoneyProvider(YOOMONEY_SECRET_KEY, YOOMONEY_ACCOUNT, SUCCESS_URL);
    }
}
