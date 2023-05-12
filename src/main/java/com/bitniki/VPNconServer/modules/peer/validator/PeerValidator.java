package com.bitniki.VPNconServer.modules.peer.validator;

import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.model.PeerFromRequest;
import com.bitniki.VPNconServer.validator.Validator;

import java.util.regex.Pattern;

public class PeerValidator extends Validator {
    private final Pattern peerIpPattern = Pattern.compile("^10\\.8\\.0\\.(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    private final Pattern peerConfNamePattern = Pattern.compile("^[A-Za-z0-9]+$");

    public static PeerValidator validateAllFields(PeerEntity peer) {
        PeerValidator peerValidator = new PeerValidator();

        //peerIp can be null but if not -- validate
        if(peer.getPeerIp() != null && !peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches()) {
            peerValidator.addFail("Wrong peer ip");
        }
        //if field null – addFail, else do match
        if(peer.getPeerConfName() == null || !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches()) {
            peerValidator.addFail("Wrong peer conf name");
        }
        //check that host is not null
        if (peer.getHost() == null) {
            peerValidator.addFail("Wrong host");
        }

        return peerValidator;
    }

    public static PeerValidator validateAllFields(PeerFromRequest peer) {
        PeerValidator peerValidator = new PeerValidator();

        //peerIp can be null but if not -- validate
        if(peer.getPeerIp() != null) {
            if (!peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches()) {
                peerValidator.addFail("Wrong peer ip");
            } else {
                int lastOctet = Integer.parseInt(peer.getPeerIp().substring(peer.getPeerIp().lastIndexOf(".") + 1));
                if (lastOctet < 2 || lastOctet > 254) {
                    peerValidator.addFail("Wrong peer ip");
                }
            }
        }
        //if field null – addFail, else do match
        if(peer.getPeerConfName() == null || !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches()) {
            peerValidator.addFail("Wrong peer conf name");
        }
        //check that host_id is not null
        if (peer.getHostId() == null) {
            peerValidator.addFail("Wrong host id");
        }

        return peerValidator;
    }

    public static PeerValidator validateNonNullFields(PeerEntity peer) {
        PeerValidator peerValidator = new PeerValidator();

        //if field null – addFail, else do match
        if(peer.getPeerIp() != null && !peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches())
            peerValidator.addFail("Wrong peer ip");
        if(peer.getPeerConfName() != null && !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches())
            peerValidator.addFail("Wrong peer conf name");

        return peerValidator;
    }

    public static PeerValidator validateNonNullFields(PeerFromRequest peer) {
        PeerValidator peerValidator = new PeerValidator();

        //if field null – addFail, else do match
        if(peer.getPeerIp() != null && !peerValidator.peerIpPattern.matcher(peer.getPeerIp()).matches())
            peerValidator.addFail("Wrong peer ip");
        if(peer.getPeerConfName() != null && !peerValidator.peerConfNamePattern.matcher(peer.getPeerConfName()).matches())
            peerValidator.addFail("Wrong peer conf name");

        return peerValidator;
    }
}
