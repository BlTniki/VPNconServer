package ru.bitniki.sms.domain.subscriptions.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domain.subscriptions.dto.Subscription;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domain.subscriptions.service.event.UserSubscriptionEventProducer;
import ru.bitniki.sms.domain.users.dto.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserSubscriptionExpirationCheckerTest {
    @TestConfiguration
    public static class Config {
        @Bean
        public UserSubscriptionExpirationChecker userSubscriptionExpirationChecker(
            UsersSubscriptionsService service,
            UserSubscriptionEventProducer producer
        ) {
            return new UserSubscriptionExpirationChecker(service, producer);
        }
    }

    @MockBean
    UsersSubscriptionsService usersSubscriptionsService;
    @MockBean
    UserSubscriptionEventProducer producer;
    @Autowired
    UserSubscriptionExpirationChecker userSubscriptionExpirationChecker;

    @Test
    void checkExpiration_burned() {
        var dtoBurned = new UserSubscription(
            1L,
            new User(1L, "kek", "cheburek"),
            new Subscription(1L, "cheburek", BigDecimal.ONE, 1, Period.ofMonths(1)),
            LocalDate.now()
        );
        var dtoBurnSoon = new UserSubscription(
                2L,
                new User(1L, "kek", "cheburek"),
                new Subscription(1L, "cheburek", BigDecimal.ONE, 1, Period.ofMonths(1)),
                LocalDate.now()
        );

        when(usersSubscriptionsService.getExpired(any()))
                .thenReturn(Flux.just(dtoBurned));
        when(usersSubscriptionsService.removeUserSubscription(anyLong()))
                .thenReturn(Mono.just(dtoBurned));
        when(usersSubscriptionsService.getByExpirationDate(any()))
                .thenReturn(Flux.just(dtoBurnSoon));

        userSubscriptionExpirationChecker.checkExpiration();

        verify(producer).createBurnedEvent(dtoBurned);
        verify(producer).createBurnSoonEvent(dtoBurnSoon);
    }
}