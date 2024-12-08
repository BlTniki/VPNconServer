package ru.bitniki.sms.domain.subscriptions.service.event;

import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;

public class KafkaUserSubscriptionEventProducer implements UserSubscriptionEventProducer {
    @Override
    public void createPaidEvent(UserSubscription userSubscription) {
    }

    @Override
    public void createBurnSoonEvent(UserSubscription userSubscription) {

    }

    @Override
    public void createBurnedEvent(UserSubscription userSubscription) {

    }
}
