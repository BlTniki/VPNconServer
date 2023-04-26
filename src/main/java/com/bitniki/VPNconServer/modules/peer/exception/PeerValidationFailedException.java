package com.bitniki.VPNconServer.modules.peer.exception;

import com.bitniki.VPNconServer.exception.EntityValidationFailedException;

public class PeerValidationFailedException extends EntityValidationFailedException {
    public PeerValidationFailedException(String message) {
        super(message);
    }
}
