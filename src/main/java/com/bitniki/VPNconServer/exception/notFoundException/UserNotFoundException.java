package com.bitniki.VPNconServer.exception.notFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
