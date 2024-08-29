package ru.bitniki.sms.domen.subscriptions.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.exception.BadRequestException;
import ru.bitniki.sms.domen.exception.EntityNotFoundException;
import ru.bitniki.sms.domen.exception.UnexpectedStateException;
import ru.bitniki.sms.domen.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domen.users.service.UsersService;

@Service
@Transactional
public class R2dbcUsersSubscriptionsService implements UsersSubscriptionsService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final UsersService usersService;
    private final SubscriptionsService subscriptionsService;
    private final UsersSubscriptionsService usersSubscriptionsService;

    public R2dbcUsersSubscriptionsService(
            UsersService usersService,
            SubscriptionsService subscriptionsService,
            UsersSubscriptionsService usersSubscriptionsService
    ) {
        this.usersService = usersService;
        this.subscriptionsService = subscriptionsService;
        this.usersSubscriptionsService = usersSubscriptionsService;
    }

    @Override
    public Mono<UserSubscription> getById(long id) {
        return usersSubscriptionsService.getById(id)
                .flatMap(userSubscription ->
                    Mono.zip(
                        usersService.getById(userSubscription.user().telegramId()),
                        subscriptionsService.getById(userSubscription.subscription().id())
                    )
                    .map(tuple -> new UserSubscription(
                        userSubscription.id(),
                        tuple.getT1(),
                        tuple.getT2()
                    ))
                )
                .switchIfEmpty(
                    Mono.error(new EntityNotFoundException("User subscription with id `%d` not found".formatted(id)))
                )
                .onErrorMap(e -> {
                    if (e instanceof BadRequestException) {
                        return new UnexpectedStateException(
                            "While successfully find user subscription catches a error on retrieving other entities", e
                        );
                    }
                    return e;
                })
                .doOnNext(dto -> LOGGER.debug("Found user subscription `{}`", dto));
    }

    @Override
    public Mono<UserSubscription> createUserSubscription(long userId, long subscriptionId, int times) {
        return null;
    }
}
