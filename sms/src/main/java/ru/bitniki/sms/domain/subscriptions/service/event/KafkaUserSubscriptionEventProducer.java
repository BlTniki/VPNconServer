package ru.bitniki.sms.domain.subscriptions.service.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscriptionEvent;

public class KafkaUserSubscriptionEventProducer implements UserSubscriptionEventProducer {
    private static final Logger LOGGER = LogManager.getLogger();

    private final String topic;
    private final KafkaTemplate<String, UserSubscriptionEvent> userSubscriptionProducer;

    public KafkaUserSubscriptionEventProducer(
            String topic,
            KafkaTemplate<String, UserSubscriptionEvent> userSubscriptionProducer) {
        this.topic = topic;
        this.userSubscriptionProducer = userSubscriptionProducer;
    }

    private UserSubscriptionEvent createEvent(UserSubscription userSubscription, UserSubscriptionEvent.Type type) {
        return new UserSubscriptionEvent(
                userSubscription.user().telegramId(),
                type,
                new UserSubscriptionEvent.Subscription(userSubscription.subscription().allowedActivePeersCount())
        );
    }

    private void logInit(UserSubscriptionEvent.Type type, UserSubscription userSubscription) {
        LOGGER.debug(
                "Writing `{}` event to Kafka topic `{}` with entity {}",
                type, topic, userSubscription
        );
    }

    private void logResult(UserSubscriptionEvent.Type type, UserSubscription userSubscription, Throwable error) {
        if (error == null) {
            LOGGER.info(
                    "Successfully sent `{}` event to Kafka topic `{}` "
                            + "for userSubscription `{}`",
                    type, topic, userSubscription
            );
        } else {
            LOGGER.error(
                    "Failed to sent `{}` event to Kafka topic `{}` "
                            + "for userSubscription `{}` with error {}",
                    type, topic, userSubscription, error.getMessage()
            );
        }
    }

    @Override
    public void createPaidEvent(UserSubscription userSubscription) {
        logInit(UserSubscriptionEvent.Type.PAID, userSubscription);
        userSubscriptionProducer.send(topic, createEvent(userSubscription, UserSubscriptionEvent.Type.PAID))
            .whenComplete((result, error) -> logResult(UserSubscriptionEvent.Type.PAID, userSubscription, error));
    }

    @Override
    public void createBurnSoonEvent(UserSubscription userSubscription) {
        logInit(UserSubscriptionEvent.Type.BURN_SOON, userSubscription);
        userSubscriptionProducer.send(topic, createEvent(userSubscription, UserSubscriptionEvent.Type.BURN_SOON))
            .whenComplete((result, error) -> logResult(UserSubscriptionEvent.Type.BURN_SOON, userSubscription, error));
    }

    @Override
    public void createBurnedEvent(UserSubscription userSubscription) {
        logInit(UserSubscriptionEvent.Type.BURNED, userSubscription);
        userSubscriptionProducer.send(topic, createEvent(userSubscription, UserSubscriptionEvent.Type.BURNED))
            .whenComplete((result, error) -> logResult(UserSubscriptionEvent.Type.BURNED, userSubscription, error));
    }
}
