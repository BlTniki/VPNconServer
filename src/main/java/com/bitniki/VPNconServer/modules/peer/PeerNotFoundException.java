package com.bitniki.VPNconServer.modules.peer;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;

public class PeerNotFoundException extends EntityNotFoundException {
    public PeerNotFoundException(String message) {
        super(message);
    }
}
