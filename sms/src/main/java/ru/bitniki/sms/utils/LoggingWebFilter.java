package ru.bitniki.sms.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class LoggingWebFilter implements WebFilter {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public @NotNull Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Логирование запроса
        LOGGER.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    // Логирование ответа
                    LOGGER.info("Response Status: {}", exchange.getResponse().getStatusCode());
                });
    }
}

