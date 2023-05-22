package com.bitniki.VPNconServer.modules.host.validator;

import com.bitniki.VPNconServer.modules.host.model.HostFromRequest;
import com.bitniki.VPNconServer.validator.Validator;

import java.util.regex.Pattern;

public class HostValidator extends Validator {
    public static final Pattern ipaddressPattern = Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    public static final Pattern hostInternalNetworkPrefixPattern = Pattern.compile("^(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.0$");

    public static HostValidator validateAllFields(HostFromRequest host) {
        HostValidator hostValidator = new HostValidator();

        //if field null – addFail, else do match
        if(host.getIpaddress() == null || !HostValidator.ipaddressPattern.matcher(host.getIpaddress()).matches()) {
            hostValidator.addFail("Wrong ipaddress");
        }
        if (host.getPort() == null || !(host.getPort() >= 0 && host.getPort() <= 65_535)) {
            hostValidator.addFail("Wrong port");
        }
        if(host.getHostInternalNetworkPrefix() == null ||
            !HostValidator.hostInternalNetworkPrefixPattern
                    .matcher(host.getHostInternalNetworkPrefix()).matches()
        ) {
            hostValidator.addFail("Wrong host internal network prefix");
        }
        if(host.getHostPassword() == null) {
            hostValidator.addFail("Wrong host password");
        }
        if(host.getHostPublicKey() == null) {
            hostValidator.addFail("Wrong host public key");
        }

        return hostValidator;
    }

    public static HostValidator validateNonNullFields(HostFromRequest host) {
        HostValidator hostValidator = new HostValidator();
        //if field not null – do match, else – do none
        if(host.getIpaddress() != null && !HostValidator.ipaddressPattern.matcher(host.getIpaddress()).matches()) {
            hostValidator.addFail("Wrong ipaddress");
        }
        if (host.getPort() != null && !(host.getPort() >= 0 && host.getPort() <= 65_535)) {
            hostValidator.addFail("Wrong port");
        }
        if(host.getHostInternalNetworkPrefix() != null &&
                !HostValidator.hostInternalNetworkPrefixPattern
                        .matcher(host.getHostInternalNetworkPrefix()).matches()
        ) {
            hostValidator.addFail("Wrong host internal network prefix");
        }

        return hostValidator;
    }
}
