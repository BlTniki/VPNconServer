package com.bitniki.VPNconServer.validator;

import com.bitniki.VPNconServer.entity.PeerEntity;

import java.util.regex.Pattern;

public class PeerValidator extends Validator{
    private final Pattern peerIpPattern = Pattern.compile("^10\\.8\\.0\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    private final Pattern peerConfNamePattern = Pattern.compile("^[A-Za-z0-9]+$");

    public static PeerValidator validateAllFields(PeerEntity peer) {
        PeerValidator peerValidator = new PeerValidator();

        //if field null – addFail, else do match
        if(peer.getPeerIp() == null || !peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches()) peerValidator.addFail("Wrong peer ip");
        if(peer.getPeerConfName() == null || !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches()) peerValidator.addFail("Wrong peer conf name");

        return peerValidator;
    }

    public static PeerValidator validateNonNullFields(PeerEntity peer) {
        PeerValidator peerValidator = new PeerValidator();

        //if field null – addFail, else do match
        if(peer.getPeerIp() != null && !peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches()) peerValidator.addFail("Wrong peer ip");
        if(peer.getPeerConfName() != null && !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches()) peerValidator.addFail("Wrong peer conf name");

        return peerValidator;
    }
}
