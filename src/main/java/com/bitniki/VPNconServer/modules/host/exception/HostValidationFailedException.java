package com.bitniki.VPNconServer.modules.host.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class HostValidationFailedException extends EntityValidationFailedException {
    public HostValidationFailedException(String message) {
        super(message);
    }
}
