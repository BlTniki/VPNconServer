package ru.bitniki.sms.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.SubscriptionResponse;

@RestController
public class SubscriptionsApiController implements SubscriptionsApi {
    @Override
    public Mono<ResponseEntity<List<SubscriptionResponse>>> subscriptionsGet(String role) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public Mono<ResponseEntity<SubscriptionResponse>> subscriptionsIdGet(Long id) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }
}
