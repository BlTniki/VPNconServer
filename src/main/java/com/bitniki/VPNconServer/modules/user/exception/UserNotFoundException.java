package com.bitniki.VPNconServer.modules.user.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
