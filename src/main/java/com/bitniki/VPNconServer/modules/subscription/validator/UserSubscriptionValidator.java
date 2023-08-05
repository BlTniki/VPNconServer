package com.bitniki.VPNconServer.modules.subscription.validator;

import com.bitniki.VPNconServer.modules.subscription.model.UserSubscriptionFromRequest;
import com.bitniki.VPNconServer.validator.Validator;

public class UserSubscriptionValidator extends Validator {

    public static UserSubscriptionValidator validateAllFields(UserSubscriptionFromRequest model) {
        UserSubscriptionValidator validator = new UserSubscriptionValidator();

        //validate
        if(model.getUserId() == null) {
            validator.addFail("Wrong userId");
        }
        if(model.getSubscriptionId() == null) {
            validator.addFail("Wrong subscriptionId");
        }

        return validator;
    }
}
