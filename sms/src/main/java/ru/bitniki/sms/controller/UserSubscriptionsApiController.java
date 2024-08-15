package ru.bitniki.sms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.sms.controller.model.AddSubscriptionToUserRequest;
import ru.bitniki.sms.controller.model.UserSubscriptionResponse;

@RestController
public class UserSubscriptionsApiController implements UserSubscriptionsApi {
    public ResponseEntity<UserSubscriptionResponse> userSubscriptionsGet(Long userId) {
        return new ResponseEntity<UserSubscriptionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<UserSubscriptionResponse> userSubscriptionsPost(AddSubscriptionToUserRequest body) {
        return new ResponseEntity<UserSubscriptionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }
}
