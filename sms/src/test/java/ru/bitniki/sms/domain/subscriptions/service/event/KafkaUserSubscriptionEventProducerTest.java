package ru.bitniki.sms.domain.subscriptions.service.event;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import ru.bitniki.sms.domain.subscriptions.dto.Subscription;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscriptionEvent;
import ru.bitniki.sms.domain.users.dto.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class KafkaUserSubscriptionEventProducerTest {
    @TestConfiguration
    public static class Config {
        @Bean
        public KafkaUserSubscriptionEventProducer userSubscriptionEventProducer(KafkaTemplate<String, UserSubscriptionEvent> userSubscriptionEventKafkaTemplate) {
            return new KafkaUserSubscriptionEventProducer("test_topic", userSubscriptionEventKafkaTemplate);
        }
    }

    @MockBean
    KafkaTemplate<String, UserSubscriptionEvent> userSubscriptionEventKafkaTemplate;
    @Autowired
    KafkaUserSubscriptionEventProducer userSubscriptionEventProducer;

    @Test
    void createPaidEvent() {
        Result result = getResult(UserSubscriptionEvent.Type.PAID);

        // Call the method
        userSubscriptionEventProducer.createPaidEvent(result.userSubscription());

        // Verify interactions
        verify(userSubscriptionEventKafkaTemplate).send("test_topic", "key", result.event());
    }



    @Test
    void createBurnSoonEvent() {
        Result result = getResult(UserSubscriptionEvent.Type.BURN_SOON);

        // Call the method
        userSubscriptionEventProducer.createBurnSoonEvent(result.userSubscription());

        // Verify interactions
        verify(userSubscriptionEventKafkaTemplate).send("test_topic", "key", result.event());
    }

    @Test
    void createBurnedEvent() {
        Result result = getResult(UserSubscriptionEvent.Type.BURNED);

        // Call the method
        userSubscriptionEventProducer.createBurnedEvent(result.userSubscription());

        // Verify interactions
        verify(userSubscriptionEventKafkaTemplate).send("test_topic", "key", result.event());
    }

    private @NotNull Result getResult(UserSubscriptionEvent.Type type) {
        UserSubscription userSubscription = new UserSubscription(
                1L,
                new User(1L, "lol", "kek"),
                new Subscription(1L, "kek", BigDecimal.ONE, 1, Period.ofMonths(1)),
                LocalDate.now().plusMonths(1)
        );
        UserSubscriptionEvent event = new UserSubscriptionEvent(
                userSubscription.user().telegramId(),
                type,
                new UserSubscriptionEvent.Subscription(userSubscription.subscription().allowedActivePeersCount())
        );
        // Simulate a successful send
        CompletableFuture<SendResult<String, UserSubscriptionEvent>> future = CompletableFuture.completedFuture(new SendResult<>(null, null));
        when(userSubscriptionEventKafkaTemplate.send(any(), any(), any())).thenReturn(future);
        return new Result(userSubscription, event);
    }

    private record Result(UserSubscription userSubscription, UserSubscriptionEvent event) {
    }
}