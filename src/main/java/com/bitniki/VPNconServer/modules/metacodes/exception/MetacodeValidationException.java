package com.bitniki.VPNconServer.modules.metacodes.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class MetacodeValidationException extends EntityValidationFailedException {
    public MetacodeValidationException(String message) {
        super(message);
    }
}
