package com.bitniki.VPNconServer.exception.notFoundException;

@SuppressWarnings("unused")
public class PaymentNotFoundException extends EntityNotFoundException{
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
