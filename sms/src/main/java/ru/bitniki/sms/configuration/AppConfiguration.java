package ru.bitniki.sms.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscriptionEvent;
import ru.bitniki.sms.domain.subscriptions.service.UserSubscriptionExpirationChecker;
import ru.bitniki.sms.domain.subscriptions.service.UsersSubscriptionsService;
import ru.bitniki.sms.domain.subscriptions.service.event.KafkaUserSubscriptionEventProducer;
import ru.bitniki.sms.domain.subscriptions.service.event.UserSubscriptionEventProducer;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableScheduling
@Profile("prod")
public record AppConfiguration(
    Kafka kafka,
    ExpirationChecker expirationChecker
) {
    private static final Logger LOGGER = LogManager.getLogger();

    @Bean
    @ConditionalOnProperty(prefix = "kafka.producer", name = "enable", havingValue = "true")
    public UserSubscriptionEventProducer userSubscriptionEventProducer(
            KafkaTemplate<String, UserSubscriptionEvent> template
    ) {
        LOGGER.info("Kafka event producer is on. Using KafkaUserSubscriptionEventProducer...");
        return new KafkaUserSubscriptionEventProducer(kafka.topic, template);
    }

    /**
     * Этот бин существует на случай, если работа с kafka выключена в проекте и нужно забить чем-то зависимости
     */
    @Bean
    @ConditionalOnProperty(prefix = "kafka.producer", name = "enable", havingValue = "false", matchIfMissing = true)
    public UserSubscriptionEventProducer userSubscriptionEventProducerStab(
    ) {
        LOGGER.info("Kafka event producer is off. Using UserSubscriptionEventProducer Stab...");
        return new UserSubscriptionEventProducer() {
            private static final Logger LOGGER = LogManager.getLogger();

            @Override
            public void createPaidEvent(UserSubscription userSubscription) {
                LOGGER.debug(
                        "UserSubscriptionEventProducer stab was called by method `createPaidEvent` with entity {}",
                        userSubscription
                );
            }

            @Override
            public void createBurnSoonEvent(UserSubscription userSubscription) {
                LOGGER.debug(
                        "UserSubscriptionEventProducer stab was called by method `createBurnSoonEvent` with entity {}",
                        userSubscription
                );
            }

            @Override
            public void createBurnedEvent(UserSubscription userSubscription) {
                LOGGER.debug(
                        "UserSubscriptionEventProducer stab was called by method `createBurnedEvent` with entity {}",
                        userSubscription
                );
            }
        };
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "expiration-checker.enable", havingValue = "true")
    public UserSubscriptionExpirationChecker userSubscriptionExpirationChecker(
            UsersSubscriptionsService service,
            UserSubscriptionEventProducer producer
    ) {
        LOGGER.info("Scheduled expiration checker at `{}`", expirationChecker.cron);
        return new UserSubscriptionExpirationChecker(service, producer);
    }

    record Kafka(
        String topic
    ) {}

    record ExpirationChecker(
        String enable,
        String cron
    ) {}
}
