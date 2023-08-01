package com.bitniki.VPNconServer.modules.subscription.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class SubscriptionNotFoundException extends EntityNotFoundException {
    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
