package ru.bitniki.sms.domen.users.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.exception.EntityAlreadyExistException;
import ru.bitniki.sms.domen.exception.EntityNotFoundException;
import ru.bitniki.sms.domen.users.dao.R2dbcUsersDao;
import ru.bitniki.sms.domen.users.dto.R2dbcUserEntity;
import ru.bitniki.sms.domen.users.dto.User;

@Service
@Transactional
public class R2dbcUsersService implements UsersService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DEFAULT_USER_ROLE = "ACTIVATED_USER";

    private final R2dbcUsersDao usersDao;

    @Autowired
    public R2dbcUsersService(R2dbcUsersDao usersDao) {
        this.usersDao = usersDao;
    }

    private User toUser(R2dbcUserEntity entity) {
        return new User(entity.getTelegramId(), entity.getUsername(), entity.getRole());
    }

    @Override
    public Mono<User> getById(long id) {
        LOGGER.debug("Getting user with telegram id `{}`", id);
        return usersDao.findByTelegramId(id)
                .map(this::toUser)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("User with telegram id `%d` not found".formatted(id)))
                )
                .doOnNext(entity -> LOGGER.debug("Found user `{}`", entity));
    }

    @Override
    public Mono<User> createUser(long id, String username) {
        LOGGER.debug("Creating user with telegram id `{}` and username `{}`", id, username);
        return usersDao.findByTelegramId(id)
                .flatMap(entity -> Mono.error(
                        new EntityAlreadyExistException("User with telegram id `%d` already exists".formatted(id))
                ))
                .then(usersDao.save(new R2dbcUserEntity(null, id, username, DEFAULT_USER_ROLE)))
                .doOnNext(entity -> LOGGER.info("Created user `{}`", entity))
                .doOnError(
                    error -> LOGGER.debug("Failed to create user with telegram id `{}` due to uniqueness violation", id)
                )
                .map(this::toUser);
    }
}
