package com.bitniki.VPNconServer.modules.subscription.validator;

import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.user.role.Role;
import com.bitniki.VPNconServer.validator.Validator;

import java.util.Arrays;

public class SubscriptionValidator extends Validator {

    public static SubscriptionValidator validateAllFields(SubscriptionEntity entity) {
        SubscriptionValidator validator = new SubscriptionValidator();

        //validate
        if(entity.getRole() == null
            || !Arrays.asList(Role.values()).contains(entity.getRole())) {
            validator.addFail("Wrong role");
        }
        if(entity.getPriceInRub() == null) {
            validator.addFail("Wrong priceInRub");
        }
        if(entity.getPeersAvailable() == null) {
            validator.addFail("Wrong peersAvailable");
        }
        if(entity.getDays() == null) {
            validator.addFail("Wrong days");
        }

        return validator;
    }
}
