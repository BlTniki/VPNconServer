package com.bitniki.VPNconServer.modules.payment.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class PaymentValidationFailedException extends EntityValidationFailedException {
    public PaymentValidationFailedException(String message) {
        super(message);
    }
}
