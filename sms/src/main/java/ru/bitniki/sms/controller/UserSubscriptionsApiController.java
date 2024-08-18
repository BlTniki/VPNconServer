package ru.bitniki.sms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.AddSubscriptionToUserRequest;
import ru.bitniki.sms.controller.model.UserSubscriptionResponse;

@RestController
public class UserSubscriptionsApiController implements UserSubscriptionsApi {
    public Mono<ResponseEntity<UserSubscriptionResponse>> userSubscriptionsGet(Long userId) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }

    public Mono<ResponseEntity<UserSubscriptionResponse>> userSubscriptionsPost(AddSubscriptionToUserRequest body) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }
}
