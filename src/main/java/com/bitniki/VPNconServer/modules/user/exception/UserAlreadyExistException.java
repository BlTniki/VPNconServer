package com.bitniki.VPNconServer.modules.user.exception;

import com.bitniki.VPNconServer.exception.EntityAlreadyExistException;

public class UserAlreadyExistException extends EntityAlreadyExistException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
