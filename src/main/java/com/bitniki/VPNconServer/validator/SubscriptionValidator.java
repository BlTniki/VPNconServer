package com.bitniki.VPNconServer.validator;

import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.model.Role;

import java.util.Arrays;

public class SubscriptionValidator extends Validator {

    public static SubscriptionValidator validateAllFields(SubscriptionEntity entity) {
        SubscriptionValidator validator = new SubscriptionValidator();

        //validate
        if(entity.getRole() == null
            || Arrays.asList(Role.values()).contains(entity.getRole())) {
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
