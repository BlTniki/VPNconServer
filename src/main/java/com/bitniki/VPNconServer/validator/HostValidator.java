package com.bitniki.VPNconServer.validator;

import com.bitniki.VPNconServer.entity.HostEntity;

import java.util.regex.Pattern;

public class HostValidator extends Validator {
    private final Pattern ipaddressPattern = Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]):((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$");

    public static HostValidator validateAllFields(HostEntity host) {
        HostValidator hostValidator = new HostValidator();

        //if field null – addFail, else do match
        if(host.getIpadress() == null || !hostValidator.ipaddressPattern.matcher(host.getIpadress()).matches()) hostValidator.addFail("Wrong ipaddress");
        if(host.getServerPassword() == null) hostValidator.addFail("Wrong server password");
        if(host.getServerPublicKey() == null) hostValidator.addFail("Wrong server public key");

        return hostValidator;
    }

    public static HostValidator validateNonNullFields(HostEntity host) {
        HostValidator hostValidator = new HostValidator();
        //if field not null – do match, else – do none
        if(host.getIpadress() != null && !hostValidator.ipaddressPattern.matcher(host.getIpadress()).matches()) hostValidator.addFail("Wrong ipaddress");

        return hostValidator;
    }
}
