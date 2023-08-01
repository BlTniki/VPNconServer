package com.bitniki.VPNconServer.modules.subscription.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class SubscriptionValidationFailedException extends EntityValidationFailedException {
    public SubscriptionValidationFailedException(String message) {
        super(message);
    }
}
