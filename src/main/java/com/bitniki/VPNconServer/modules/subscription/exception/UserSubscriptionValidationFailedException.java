package com.bitniki.VPNconServer.modules.subscription.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class UserSubscriptionValidationFailedException extends EntityValidationFailedException {
    public UserSubscriptionValidationFailedException(String message) {
        super(message);
    }
}
