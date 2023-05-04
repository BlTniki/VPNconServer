package com.bitniki.VPNconServer.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class FlywayConfig {


    @Bean(initMethod = "migrate")
    public Flyway flyway(
            @Value("${spring.flyway.url}") String url,
            @Value("${spring.flyway.user}") String user,
            @Value("${spring.flyway.password}") String password,
            @Value("${spring.flyway.locations}") String locations
    ) {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(url, user, password)
                .locations(locations)
        );
    }
}
