package com.bitniki.VPNconServer.modules.mail.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class MailValidationFailedException extends EntityValidationFailedException {
    public MailValidationFailedException(String message) {
        super(message);
    }
}
