package ru.bitniki.sms.domain.subscriptions.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.bitniki.sms.IntegrationTest;
import ru.bitniki.sms.domain.exception.BadRequestException;
import ru.bitniki.sms.domain.exception.EntityNotFoundException;
import ru.bitniki.sms.domain.users.service.UsersService;
import ru.bitniki.sms.utils.rollback.TrxStepVerifier;

import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class R2dbcUsersSubscriptionsServiceTest extends IntegrationTest {
    @Autowired
    TrxStepVerifier stepVerifier;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersSubscriptionsService usersSubscriptionsService;



    @Test
    @DisplayName("Check that we can find user subscription by user id")
    void getByUserId_success() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .then(usersSubscriptionsService.getByUserId(123L))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.user().telegramId()).isEqualTo(123L);
                    assertThat(dto.subscription().id()).isEqualTo(3L);
                    assertThat(dto.expirationDate()).isEqualTo(LocalDate.now().plus(Period.ofMonths(1)));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Check that we throw error on getting if user subscription not exists")
    void getByUserId_not_found() {
        usersSubscriptionsService.getByUserId(123L)
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(EntityNotFoundException.class)
                                .hasMessageContaining("User subscription")
                )
                .verify();
    }

    @Test
    @DisplayName("Create: Check that we can create user subscription if all fine")
    void addUserSubscription_create_success() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.user().telegramId()).isEqualTo(123L);
                    assertThat(dto.subscription().id()).isEqualTo(3L);
                    assertThat(dto.expirationDate()).isEqualTo(LocalDate.now().plus(Period.ofMonths(1)));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Create: Check that we can add correct amount of times")
    void addUserSubscription_create_success_times() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 3))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.user().telegramId()).isEqualTo(123L);
                    assertThat(dto.subscription().id()).isEqualTo(3L);
                    assertThat(dto.expirationDate()).isEqualTo(LocalDate.now().plus(Period.ofMonths(3)));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Create: Check that we throw error on 0 times")
    void addUserSubscription_zero_times() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 0))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(BadRequestException.class)
                                .hasMessageContaining("Can't add subscription to user 0 times")
                )
                .verify();
    }

    @Test
    @DisplayName("Create: Check that we throw error on non existing user")
    void addUserSubscription_user_not_exists() {
        stepVerifier.create(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(EntityNotFoundException.class)
                                .hasMessageContaining("User")
                )
                .verify();
    }

    @Test
    @DisplayName("Create: Check that we throw error on non existing subscription")
    void addUserSubscription_subscription_not_exists() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 123L, 1))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(EntityNotFoundException.class)
                                .hasMessageContaining("Subscription")
                )
                .verify();
    }

    @Test
    @DisplayName("Renew: Check that we can add user subscription if all fine")
    void addUserSubscription_renew_success() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.user().telegramId()).isEqualTo(123L);
                    assertThat(dto.subscription().id()).isEqualTo(3L);
                    assertThat(dto.expirationDate()).isEqualTo(LocalDate.now().plus(Period.ofMonths(1)).plus(Period.ofMonths(1)));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Renew: Check that we can add correct amount of times")
    void addUserSubscription_renew_success_times() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 3))
                .as(stepVerifier::create)
                .assertNext(dto -> {
                    assertThat(dto.user().telegramId()).isEqualTo(123L);
                    assertThat(dto.subscription().id()).isEqualTo(3L);
                    assertThat(dto.expirationDate()).isEqualTo(LocalDate.now().plus(Period.ofMonths(1)).plus(Period.ofMonths(3)));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Renew: Check that we throw error on 0 times")
    void addUserSubscription_renew_zero_times() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 0))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(BadRequestException.class)
                                .hasMessageContaining("Can't add subscription to user 0 times")
                )
                .verify();
    }

    @Test
    @DisplayName("Check that we can remove subscription from user")
    void removeUserSubscription_success() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.addUserSubscription(123L, 3L, 1))
                .then(usersSubscriptionsService.removeUserSubscription(123L))
//                .delaySubscription(Duration.ofSeconds(5))
                .then(usersSubscriptionsService.getByUserId(123L))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(EntityNotFoundException.class)
                                .hasMessageContaining("User subscription")
                )
                .verify();
    }

    @Test
    @DisplayName("Check that we throws error on removing user subscription that not exists")
    void removeUserSubscription_not_found() {
        usersService.createUser(123L, "wololo")
                .then(usersSubscriptionsService.removeUserSubscription(123L))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error ->
                        assertThat(error)
                                .isInstanceOf(EntityNotFoundException.class)
                                .hasMessageContaining("User subscription")
                )
                .verify();
    }
}