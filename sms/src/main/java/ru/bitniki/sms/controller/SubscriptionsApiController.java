package ru.bitniki.sms.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.SubscriptionResponse;
import ru.bitniki.sms.domen.subscriptions.service.SubscriptionsService;

@RestController
public class SubscriptionsApiController implements SubscriptionsApi {
    private static final Logger LOGGER = LogManager.getLogger();

    private final SubscriptionsService subscriptionsService;

    @Autowired
    public SubscriptionsApiController(SubscriptionsService subscriptionsService) {
        this.subscriptionsService = subscriptionsService;
    }

    @Override
    public Mono<ResponseEntity<List<SubscriptionResponse>>> subscriptionsGet(String role) {
        return subscriptionsService.getByRole(role)
                .map(ResponseMapper::toSubscriptionResponse)
                .collectList()
                .doOnNext(
                        list -> LOGGER.info("Response successfully with subscriptions with role `{}` `{}` ", role, list)
                )
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<SubscriptionResponse>> subscriptionsIdGet(Long id) {
        return subscriptionsService.getById(id)
                .map(ResponseMapper::toSubscriptionResponse)
                .map(ResponseEntity::ok)
                .doOnNext(response -> LOGGER.info("Response successfully to GET request at /subscriptions/{}", id));
    }
}
