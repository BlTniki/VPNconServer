package com.bitniki.VPNconServer.modules.mail.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class ReminderValidationFailedException extends EntityValidationFailedException {
    public ReminderValidationFailedException(String message) {
        super(message);
    }
}
