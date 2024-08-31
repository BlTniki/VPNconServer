package ru.bitniki.sms.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.AddUserRequest;
import ru.bitniki.sms.controller.model.UserResponse;
import ru.bitniki.sms.domain.users.service.UsersService;

@RestController
public class UsersApiController implements UsersApi {
    private static final Logger LOGGER = LogManager.getLogger();

    private final UsersService usersService;

    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Mono<ResponseEntity<UserResponse>> usersIdGet(Long id) {
        return usersService.getById(id)
                .map(ResponseMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .doOnNext(response -> LOGGER.info("Response successfully to GET request at /users/{}", id));
    }

    @Override
    public Mono<ResponseEntity<UserResponse>> usersPost(AddUserRequest body) {
        return usersService.createUser(body.getTelegramId(), body.getUsername())
                .map(ResponseMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .doOnNext(
                    response -> LOGGER.info("Response successfully to POST request at /users with body {}", body)
                );
    }
}
