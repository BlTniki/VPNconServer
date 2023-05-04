package com.bitniki.VPNconServer.modules.host.validator;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.validator.Validator;

import java.util.regex.Pattern;

public class HostValidator extends Validator {
    private final Pattern ipaddressPattern = Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]):((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$");
    private final Pattern hostInternalNetworkPrefixPattern = Pattern.compile("^(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.0$");

    public static HostValidator validateAllFields(HostEntity host) {
        HostValidator hostValidator = new HostValidator();

        //if field null – addFail, else do match
        if(host.getIpaddress() == null || !hostValidator.ipaddressPattern.matcher(host.getIpaddress()).matches()) {
            hostValidator.addFail("Wrong ipaddress");
        }
        if(host.getHostPassword() == null) {
            hostValidator.addFail("Wrong host password");
        }
        if(host.getHostInternalNetworkPrefix() == null ||
            !hostValidator.hostInternalNetworkPrefixPattern
                    .matcher(host.getHostInternalNetworkPrefix()).matches()
        ) {
            hostValidator.addFail("Wrong host internal network prefix");
        }
        if(host.getHostPublicKey() == null) {
            hostValidator.addFail("Wrong host public key");
        }

        return hostValidator;
    }

    public static HostValidator validateNonNullFields(HostEntity host) {
        HostValidator hostValidator = new HostValidator();
        //if field not null – do match, else – do none
        if(host.getIpaddress() != null && !hostValidator.ipaddressPattern.matcher(host.getIpaddress()).matches()) {
            hostValidator.addFail("Wrong ipaddress");
        }
        if(host.getHostInternalNetworkPrefix() != null &&
                !hostValidator.hostInternalNetworkPrefixPattern
                        .matcher(host.getHostInternalNetworkPrefix()).matches()
        ) {
            hostValidator.addFail("Wrong host internal network prefix");
        }

        return hostValidator;
    }
}
