package ru.bitniki.sms.domain.subscriptions.service;

import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domain.subscriptions.service.event.UserSubscriptionEventProducer;

public class UserSubscriptionExpirationChecker {
    private static final Logger LOGGER = LogManager.getLogger();

    private final UsersSubscriptionsService usersSubscriptionsService;
    private final UserSubscriptionEventProducer producer;

    public UserSubscriptionExpirationChecker(
            UsersSubscriptionsService usersSubscriptionsService,
            UserSubscriptionEventProducer producer
    ) {
        this.usersSubscriptionsService = usersSubscriptionsService;
        this.producer = producer;
    }

    private Flux<UserSubscription> checkExpired(LocalDate referenceDate) {
        LOGGER.info("Check expired user subscriptions for day `{}`", referenceDate);
        return usersSubscriptionsService.getExpired(referenceDate)
                .doOnNext(dto -> LOGGER.info("user `{}` has a expired subscription `{}`", dto.user().telegramId(), dto))
                .flatMap(dto -> usersSubscriptionsService.removeUserSubscription(dto.user().telegramId()))
                .doOnNext(producer::createBurnedEvent);
    }

    private Flux<UserSubscription> checkExpireSoon(LocalDate referenceDate) {
        LOGGER.info("Check expire soon user subscriptions for day `{}`", referenceDate);
        return usersSubscriptionsService.getByExpirationDate(referenceDate.plusDays(1))
                .doOnNext(
                        dto -> LOGGER.info(
                                "user `{}` has a expire soon subscription `{}`", dto.user().telegramId(), dto
                        )
                )
                .doOnNext(producer::createBurnSoonEvent);
    }

    @Scheduled(cron = "#{@'app-ru.bitniki.sms.configuration.AppConfiguration'.expirationChecker.cron}")
    public void checkExpiration() {
        final LocalDate today = LocalDate.now();
        LOGGER.debug("Start expiration checking...");
        checkExpired(today).thenMany(checkExpireSoon(today)).subscribe();
        LOGGER.debug("Expiration checking finished");
    }
}
