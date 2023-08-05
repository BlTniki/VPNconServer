package com.bitniki.VPNconServer.modules.subscription.exception;

import com.bitniki.VPNconServer.exception.EntityAlreadyExistException;

public class SubscriptionAlreadyExistException extends EntityAlreadyExistException {
    public SubscriptionAlreadyExistException(String message) {
        super(message);
    }
}
