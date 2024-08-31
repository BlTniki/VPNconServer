package ru.bitniki.sms.domen.subscriptions.service;

import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.exception.BadRequestException;
import ru.bitniki.sms.domen.exception.EntityNotFoundException;
import ru.bitniki.sms.domen.exception.UnexpectedStateException;
import ru.bitniki.sms.domen.subscriptions.dao.R2dbcUsersSubscriptionsDao;
import ru.bitniki.sms.domen.subscriptions.dto.R2dbcUserSubscriptionEntity;
import ru.bitniki.sms.domen.subscriptions.dto.Subscription;
import ru.bitniki.sms.domen.subscriptions.dto.UserSubscription;
import ru.bitniki.sms.domen.users.dto.User;
import ru.bitniki.sms.domen.users.service.UsersService;

@Service
@Transactional
public class R2dbcUsersSubscriptionsService implements UsersSubscriptionsService {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String USER_SUBSCRIPTION_NOT_FOUND_MSG = "User subscription with userId `%d` not found";
    public static final String UNEXPECTED_STATE_MSG =
            "While successfully find user subscription catches a error on retrieving other entities";

    private final UsersService usersService;
    private final SubscriptionsService subscriptionsService;
    private final R2dbcUsersSubscriptionsDao usersSubscriptionsDao;

    @Autowired
    public R2dbcUsersSubscriptionsService(
            UsersService usersService,
            SubscriptionsService subscriptionsService,
            R2dbcUsersSubscriptionsDao usersSubscriptionsDao
    ) {
        this.usersService = usersService;
        this.subscriptionsService = subscriptionsService;
        this.usersSubscriptionsDao = usersSubscriptionsDao;
    }

    @Override
    public Mono<UserSubscription> getByUserId(long userId) {
        LOGGER.debug("Getting user subscription with userId `{}`", userId);
        return usersSubscriptionsDao.findByUserId(userId)
                .flatMap(userSubscription ->
                    Mono.zip(
                        usersService.getById(userSubscription.getUserId()),
                        subscriptionsService.getById(userSubscription.getSubscriptionId())
                    )
                    .map(tuple -> new UserSubscription(
                        userSubscription.getId(),
                        tuple.getT1(),
                        tuple.getT2(),
                        userSubscription.getExpirationDate()
                    ))
                )
                .onErrorMap(e -> {
                    if (e instanceof BadRequestException) {
                        return new UnexpectedStateException(
                                UNEXPECTED_STATE_MSG, e
                        );
                    }
                    return e;
                })
                .switchIfEmpty(
                    Mono.error(
                        new EntityNotFoundException(USER_SUBSCRIPTION_NOT_FOUND_MSG.formatted(userId))
                    )
                )
                .doOnNext(dto -> LOGGER.debug("Found user subscription `{}`", dto));
    }

    private Mono<UserSubscription> renewSubscription(
            R2dbcUserSubscriptionEntity userSubscriptionEntity,
            long subscriptionId,
            int times
    ) {
        if (userSubscriptionEntity.getSubscriptionId() != subscriptionId) {
            throw new BadRequestException(
                "Can't renew existing subscription with new subscription (existing sub id: %d, new sub id: %d"
                        .formatted(userSubscriptionEntity.getSubscriptionId(), subscriptionId)
            );
        }

        return Mono.zip(usersService.getById(userSubscriptionEntity.getUserId()),
            subscriptionsService.getById(userSubscriptionEntity.getSubscriptionId()))
                .flatMap(tuple -> {
                    var user = tuple.getT1();
                    var subscription = tuple.getT2();
                    LocalDate newExpirationDate = userSubscriptionEntity.getExpirationDate()
                            .plus(subscription.period().multipliedBy(times));
                    userSubscriptionEntity.setExpirationDate(newExpirationDate);
                    return usersSubscriptionsDao.save(userSubscriptionEntity)
                            .map(entity -> new UserSubscription(
                                    userSubscriptionEntity.getId(),
                                    user,
                                    subscription,
                                    newExpirationDate
                            ));
                })
                .doOnNext(dto -> LOGGER.info("renew user subscription {}", dto));
    }

    private Mono<UserSubscription> createNewSubscription(User user, Subscription subscription, int times) {
        return usersSubscriptionsDao.save(
                new R2dbcUserSubscriptionEntity(
                        null,
                        user.telegramId(),
                        subscription.id(),
                        LocalDate.now().plus(subscription.period().multipliedBy(times))
                ))
                .map(entity -> new UserSubscription(entity.getId(), user, subscription, entity.getExpirationDate()))
                .doOnNext(dto -> LOGGER.info("Created new user subscription {}", dto));
    }

    @Override
    public Mono<UserSubscription> addUserSubscription(long userId, long subscriptionId, int times) {
        LOGGER.debug(
            "Adding `{}` times the subscription with id `{}` to user with id `{}`", times, subscriptionId, userId
        );
        if (times < 1) {
            throw new BadRequestException("Can't add subscription to user 0 times");
        }
        return usersSubscriptionsDao.findByUserId(userId)
                .flatMap(entity -> renewSubscription(entity, subscriptionId, times))
                .switchIfEmpty(
                    Mono.zip(usersService.getById(userId),
                    subscriptionsService.getById(subscriptionId))
                        .flatMap(tuple -> createNewSubscription(tuple.getT1(), tuple.getT2(), times))
                );
    }

    @Override
    public Mono<UserSubscription> removeUserSubscription(long userId) {
        LOGGER.debug("Removing subscription from user with id `{}`", userId);
        return usersSubscriptionsDao.findByUserId(userId)
                .flatMap(entity -> usersSubscriptionsDao.delete(entity).thenReturn(entity))
                .flatMap(userSubscription ->
                    Mono.zip(
                        usersService.getById(userSubscription.getUserId()),
                        subscriptionsService.getById(userSubscription.getSubscriptionId())
                    )
                    .map(tuple -> new UserSubscription(
                        userSubscription.getId(),
                        tuple.getT1(),
                        tuple.getT2(),
                        userSubscription.getExpirationDate()
                    ))
                )
                .onErrorMap(e -> {
                    if (e instanceof BadRequestException) {
                        return new UnexpectedStateException(
                                UNEXPECTED_STATE_MSG, e
                        );
                    }
                    return e;
                })
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException(USER_SUBSCRIPTION_NOT_FOUND_MSG.formatted(userId))
                ))
                .doOnNext(dto -> LOGGER.info("Successfully removed user subscription `{}`", dto));
    }
}
