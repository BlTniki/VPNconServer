package com.bitniki.VPNconServer.modules.host.exception;

import com.bitniki.VPNconServer.exception.EntityAlreadyExistException;

public class HostAlreadyExistException extends EntityAlreadyExistException {
    public HostAlreadyExistException(String message) {
        super(message);
    }
}
