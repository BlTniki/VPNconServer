package ru.bitniki.sms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.AddSubscriptionToUserRequest;
import ru.bitniki.sms.controller.model.UserSubscriptionResponse;
import ru.bitniki.sms.domain.subscriptions.service.UsersSubscriptionsService;

@RestController
public class UserSubscriptionsApiController implements UserSubscriptionsApi {
    private static final Logger LOGGER = LogManager.getLogger();

    private final UsersSubscriptionsService usersSubscriptionsService;

    @Autowired
    public UserSubscriptionsApiController(UsersSubscriptionsService usersSubscriptionsService) {
        this.usersSubscriptionsService = usersSubscriptionsService;
    }

    public Mono<ResponseEntity<UserSubscriptionResponse>> userSubscriptionsGet(Long userId) {
        return usersSubscriptionsService.getByUserId(userId)
                .map(ResponseMapper::toUserSubscriptionResponse)
                .map(ResponseEntity::ok)
                .doOnNext(response -> LOGGER.info(
                        "Response successfully to GET request at /user_subscriptions with query `userId={}`", userId
                ));
    }

    public Mono<ResponseEntity<UserSubscriptionResponse>> userSubscriptionsPost(AddSubscriptionToUserRequest body) {
        return usersSubscriptionsService
                .addUserSubscription(body.getUserId(), body.getSubscriptionId(), body.getTimes())
                .map(ResponseMapper::toUserSubscriptionResponse)
                .map(ResponseEntity::ok)
                .doOnNext(response -> LOGGER.info(
                        "Response successfully to POST request at /user_subscriptions with body `{}`", body
                ));
    }

    @Override
    public Mono<ResponseEntity<UserSubscriptionResponse>> userSubscriptionsDelete(Long userId) {
        return usersSubscriptionsService.removeUserSubscription(userId)
                .map(ResponseMapper::toUserSubscriptionResponse)
                .map(ResponseEntity::ok)
                .doOnNext(response -> LOGGER.info(
                        "Response successfully to DELETE request at /user_subscriptions with query `userId={}`", userId
                ));
    }
}
