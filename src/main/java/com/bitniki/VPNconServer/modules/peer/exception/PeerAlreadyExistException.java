package com.bitniki.VPNconServer.modules.peer.exception;

import com.bitniki.VPNconServer.exception.EntityAlreadyExistException;

public class PeerAlreadyExistException extends EntityAlreadyExistException {
    public PeerAlreadyExistException(String message) {
        super(message);
    }
}
