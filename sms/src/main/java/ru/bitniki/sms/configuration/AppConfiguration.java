package ru.bitniki.sms.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscriptionEvent;
import ru.bitniki.sms.domain.subscriptions.service.event.KafkaUserSubscriptionEventProducer;
import ru.bitniki.sms.domain.subscriptions.service.event.UserSubscriptionEventProducer;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record AppConfiguration(
    Kafka kafka
) {
    private static final Logger LOGGER = LogManager.getLogger();

    @Bean
    @ConditionalOnProperty(prefix = "kafka", name = "enable", havingValue = "true")
    public UserSubscriptionEventProducer userSubscriptionEventProducer(
            KafkaTemplate<String, UserSubscriptionEvent> template
    ) {
        return new KafkaUserSubscriptionEventProducer(kafka.topic, template);
    }

    /**
     * Этот бин существует на случай, если работа с kafka выключена в проекте и нужно забить чем-то зависимости
     */
    @Bean
    @ConditionalOnProperty(prefix = "kafka", name = "enable", havingValue = "false")
    public UserSubscriptionEventProducer userSubscriptionEventProducerStab(
    ) {
        return new KafkaUserSubscriptionEventProducer(kafka.topic, template);
    }

    record Kafka(
        String topic
    ) {}
}
