package com.bitniki.VPNconServer.modules.user.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class RoleNotFoundException extends EntityNotFoundException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
