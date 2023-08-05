package com.bitniki.VPNconServer.modules.subscription.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class UserSubscriptionNotFoundException extends EntityNotFoundException {
    public UserSubscriptionNotFoundException(String message) {
        super(message);
    }
}
