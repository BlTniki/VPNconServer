package ru.bitniki.sms.domain.subscriptions.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.bitniki.sms.IntegrationTest;
import ru.bitniki.sms.domain.exception.EntityNotFoundException;
import ru.bitniki.sms.domain.subscriptions.dto.Subscription;
import ru.bitniki.sms.utils.rollback.TrxStepVerifier;

import java.math.BigDecimal;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class R2dbcSubscriptionsServiceTest extends IntegrationTest {
    @Autowired
    TrxStepVerifier stepVerifier;
    @Autowired
    R2dbcSubscriptionsService subscriptionsService;

    @Test
    @DisplayName("Check that we can find a subscription by id")
    void getById_success() {
        var expectedSubscription = new Subscription(1L, "ADMIN", BigDecimal.valueOf(1L), 1000, Period.ofYears(10));

        stepVerifier.create(subscriptionsService.getById(1L))
                .assertNext(subscription -> assertThat(subscription).isEqualTo(expectedSubscription))
                .verifyComplete();
    }

    @Test
    @DisplayName("Check that we get error if subscription is not exists")
    void getById_SubscriptionNotFound() {

        stepVerifier.create(subscriptionsService.getById(0L))
                .expectErrorSatisfies(error -> assertThat(error).isInstanceOf(EntityNotFoundException.class))
                .verify();
    }

    @Test
    @DisplayName("Check that we can find a subscriptions by role")
    void getByRole_success() {
        stepVerifier.create(subscriptionsService.getByRole("ACTIVATED_USER"))
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Check that we get empty stream if role is not exists")
    void getByRole_emptyList() {
        stepVerifier.create(subscriptionsService.getByRole("foo"))
                .expectNextCount(0)
                .verifyComplete();
    }
}