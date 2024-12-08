package ru.bitniki.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import ru.bitniki.sms.configuration.KafkaConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(KafkaConfiguration.class)
@EnableR2dbcRepositories
@EnableWebFlux
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsApplication.class, args);
    }
}
