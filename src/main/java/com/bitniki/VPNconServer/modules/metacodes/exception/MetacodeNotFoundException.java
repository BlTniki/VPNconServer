package com.bitniki.VPNconServer.modules.metacodes.exception;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class MetacodeNotFoundException extends EntityNotFoundException {

    public MetacodeNotFoundException(String message) {
        super(message);
    }
}
