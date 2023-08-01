package com.bitniki.VPNconServer.modules.subscription.validator;

import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.model.SubscriptionFromRequest;
import com.bitniki.VPNconServer.validator.Validator;

public class SubscriptionValidator extends Validator {

    @SuppressWarnings("unused")
    public static SubscriptionValidator validateAllFields(SubscriptionEntity entity) {
        SubscriptionValidator validator = new SubscriptionValidator();

        //validate
        if(entity.getRole() == null) {
            validator.addFail("Wrong role");
        }
        if(entity.getPriceInRub() == null) {
            validator.addFail("Wrong priceInRub");
        }
        if(entity.getPeersAvailable() == null) {
            validator.addFail("Wrong peersAvailable");
        }
        if(entity.getDurationDays() == null) {
            validator.addFail("Wrong duration days");
        }

        return validator;
    }

    public static SubscriptionValidator validateAllFields(SubscriptionFromRequest model) {
        SubscriptionValidator validator = new SubscriptionValidator();

        //validate
        if(model.getRole() == null) {
            validator.addFail("Wrong role");
        }
        if(model.getPriceInRub() == null) {
            validator.addFail("Wrong priceInRub");
        }
        if(model.getPeersAvailable() == null) {
            validator.addFail("Wrong peersAvailable");
        }
        if(model.getDurationDays() == null) {
            validator.addFail("Wrong duration days");
        }

        return validator;
    }
}
