package com.bitniki.VPNconServer.modules.payment.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class PaymentNotFoundException extends EntityNotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
