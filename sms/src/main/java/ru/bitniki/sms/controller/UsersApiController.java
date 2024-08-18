package ru.bitniki.sms.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bitniki.sms.controller.model.AddUserRequest;
import ru.bitniki.sms.controller.model.UserResponse;

@RestController
public class UsersApiController implements UsersApi {
    @Override
    public Mono<ResponseEntity<UserResponse>> usersIdGet(Long id) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public Mono<ResponseEntity<UserResponse>> usersPost(AddUserRequest body) {
        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED));
    }

}
