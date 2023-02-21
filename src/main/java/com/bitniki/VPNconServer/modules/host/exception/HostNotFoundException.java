package com.bitniki.VPNconServer.modules.host.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class HostNotFoundException extends EntityNotFoundException {
    public HostNotFoundException(String message) {
        super(message);
    }
}
