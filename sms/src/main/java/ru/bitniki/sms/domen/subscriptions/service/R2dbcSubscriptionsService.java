package ru.bitniki.sms.domen.subscriptions.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.domen.exception.EntityNotFoundException;
import ru.bitniki.sms.domen.subscriptions.dao.R2dbcSubscriptionsDao;
import ru.bitniki.sms.domen.subscriptions.dto.R2dbcSubscriptionEntity;
import ru.bitniki.sms.domen.users.dto.Subscription;

@Service
@Transactional
public class R2dbcSubscriptionsService implements SubscriptionsService {
    private static final Logger LOGGER = LogManager.getLogger();

    private final R2dbcSubscriptionsDao subscriptionsDao;

    @Autowired
    public R2dbcSubscriptionsService(R2dbcSubscriptionsDao subscriptionsDao) {
        this.subscriptionsDao = subscriptionsDao;
    }

    private Subscription toSubscription(R2dbcSubscriptionEntity entity) {
        return new Subscription(
                entity.getId(),
                entity.getRole(),
                entity.getPriceInRubles(),
                entity.getAllowedActivePeersCount(),
                entity.getPeriod().getPeriod()
        );
    }

    @Override
    public Mono<Subscription> getById(long id) {
        LOGGER.debug("Getting subscription with id `{}`", id);
        return subscriptionsDao.findById(id)
                .map(this::toSubscription)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException("Subscription with id `%d` not found".formatted(id)))
                )
                .doOnNext(entity -> LOGGER.debug("Found subscription `{}`", entity));
    }

    @Override
    public Flux<Subscription> getByRole(String role) {
        LOGGER.debug("Getting subscriptions with role `{}`", role);
        return subscriptionsDao.findByRole(role)
                .map(this::toSubscription);
    }
}
