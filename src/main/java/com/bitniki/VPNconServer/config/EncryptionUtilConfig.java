package com.bitniki.VPNconServer.config;

import com.bitniki.VPNconServer.modules.payment.EncryptionUtil.AESEncryptionUtil;
import com.bitniki.VPNconServer.modules.payment.EncryptionUtil.EncryptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionUtilConfig {
    @Value("${payment.uuidEncryption.secretKey}")
    private String SECRET_KEY;

    @Bean
    public EncryptionUtil encryptionUtilBean() {
        return new AESEncryptionUtil(SECRET_KEY);
    }
}
