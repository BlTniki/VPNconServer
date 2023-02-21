package com.bitniki.VPNconServer.modules.activateToken.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class ActivateTokenNotFoundException extends EntityNotFoundException {
    public ActivateTokenNotFoundException(String message) {
        super(message);
    }
}
