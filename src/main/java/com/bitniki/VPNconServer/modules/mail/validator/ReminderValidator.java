package com.bitniki.VPNconServer.modules.mail.validator;

import com.bitniki.VPNconServer.modules.mail.model.ReminderToCreate;
import com.bitniki.VPNconServer.validator.Validator;

public class ReminderValidator extends Validator {
    public static ReminderValidator validateAllFields(ReminderToCreate model) {
        ReminderValidator validator = new ReminderValidator();

        if (model.getReminderType() == null) {
            validator.addFail("Wrong reminderType");
        }
        if (model.getUserId() == null) {
            validator.addFail("Wrong userId");
        }
        if (model.getPayload() == null) {
            validator.addFail("Wrong payload");
        }

        return validator;
    }
}
