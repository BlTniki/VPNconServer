package ru.bitniki.sms.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.sms.controller.model.SubscriptionResponse;

@RestController
public class SubscriptionsApiController implements SubscriptionsApi {
    @Override
    public ResponseEntity<List<SubscriptionResponse>> subscriptionsGet(String role) {
        return new ResponseEntity<List<SubscriptionResponse>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<SubscriptionResponse> subscriptionsIdGet(Long id) {
        return new ResponseEntity<SubscriptionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }
}
