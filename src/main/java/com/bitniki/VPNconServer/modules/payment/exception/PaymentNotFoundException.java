package com.bitniki.VPNconServer.modules.payment.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

@SuppressWarnings("unused")
public class PaymentNotFoundException extends EntityNotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
