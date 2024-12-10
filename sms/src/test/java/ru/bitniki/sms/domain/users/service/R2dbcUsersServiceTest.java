package ru.bitniki.sms.domain.users.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.bitniki.sms.IntegrationTest;
import ru.bitniki.sms.domain.exception.EntityAlreadyExistException;
import ru.bitniki.sms.domain.exception.EntityNotFoundException;
import ru.bitniki.sms.domain.users.dto.User;
import ru.bitniki.sms.utils.rollback.TrxStepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class R2dbcUsersServiceTest extends IntegrationTest {
    private final User newUser = new User(123L, "foo", "ACTIVATED_USER");
    @Autowired
    TrxStepVerifier stepVerifier;
    @Autowired
    R2dbcUsersService usersService;

    @Test
    @DisplayName("Check that user creates successfully")
    public void createUser_success() {
        stepVerifier.create(usersService.createUser(123L, "foo"))
                .assertNext(
                        user -> assertThat(user).isEqualTo(newUser)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Check throwing error if telegram id not unique")
    public void createUser_UserAlreadyExist() {
        usersService.createUser(123L, "foo")
                .flatMap(user -> usersService.createUser(user.telegramId(), user.username()))
                .as(stepVerifier::create)
                .expectErrorSatisfies(error -> assertThat(error).isInstanceOf(EntityAlreadyExistException.class))
                .verify();
    }

    @Test
    @DisplayName("Check successful user finding")
    public void getById_success() {
        usersService.createUser(123L, "foo")
                .then(usersService.getById(123L))
                .as(stepVerifier::create)
                .assertNext(
                        user -> assertThat(user).isEqualTo(newUser)
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Check throwing error if telegram id not unique")
    public void getById_UserNotFound() {
        stepVerifier.create(usersService.getById(123L))
                .expectErrorSatisfies(error -> assertThat(error).isInstanceOf(EntityNotFoundException.class))
                .verify();
    }
}