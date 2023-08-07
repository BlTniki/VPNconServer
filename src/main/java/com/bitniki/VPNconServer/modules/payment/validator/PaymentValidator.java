package com.bitniki.VPNconServer.modules.payment.validator;

import com.bitniki.VPNconServer.modules.payment.model.PaymentToCreate;
import com.bitniki.VPNconServer.validator.Validator;

public class PaymentValidator extends Validator {
    public static PaymentValidator validateAllFields(PaymentToCreate model) {
        PaymentValidator validator = new PaymentValidator();

        if(model.getUserId() == null) {
            validator.addFail("Wrong userId");
        }
        if(model.getSubscriptionId() == null) {
            validator.addFail("Wrong subscriptionId");
        }

        return validator;
    }
}
