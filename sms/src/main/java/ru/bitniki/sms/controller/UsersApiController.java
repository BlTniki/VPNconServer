package ru.bitniki.sms.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.sms.controller.model.AddUserRequest;
import ru.bitniki.sms.controller.model.UserResponse;

@RestController
public class UsersApiController implements UsersApi {
    @Override
    public ResponseEntity<UserResponse> usersIdGet(Long id) {
        return new ResponseEntity<UserResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<UserResponse> usersPost(AddUserRequest body) {
        return new ResponseEntity<UserResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
