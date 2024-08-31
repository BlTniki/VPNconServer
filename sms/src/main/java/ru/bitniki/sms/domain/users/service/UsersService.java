package ru.bitniki.sms.domain.users.service;

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domain.users.dto.User;

public interface UsersService {
    /**
     * Return user by given id.
     * @param id user id
     * @return {@link User} or
     * {@link ru.bitniki.sms.domain.exception.EntityNotFoundException} if a user with this id not exists
     */
     Mono<User> getById(long id);

    /**
     * Create new user with given id and username.
     * @param id new user's id
     * @param username user's
     * @return created {@link User} or
     * {@link ru.bitniki.sms.domain.exception.EntityAlreadyExistException} if a user with given id already exists
     */
     Mono<User> createUser(long id, @NotNull String username);
}
