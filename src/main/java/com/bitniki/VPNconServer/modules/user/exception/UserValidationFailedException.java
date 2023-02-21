package com.bitniki.VPNconServer.modules.user.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class UserValidationFailedException extends EntityValidationFailedException {
    public UserValidationFailedException(String message) {
        super(message);
    }
}
