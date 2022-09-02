package com.bitniki.VPNconServer.validator;

import com.bitniki.VPNconServer.entity.UserEntity;

import java.util.regex.Pattern;

public class UserValidator extends Validator{
    private final Pattern loginPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_.]{1,20}$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$");

    public static UserValidator validateAllFields(UserEntity user) {
        UserValidator userValidator = new UserValidator();

        //if field null – addFail, else do match
        if(user.getLogin() == null || !userValidator.loginPattern.matcher(user.getLogin()).matches()) userValidator.addFail("Wrong login");
        if(user.getPassword() == null || !userValidator.passwordPattern.matcher(user.getPassword()).matches()) userValidator.addFail("Wrong password");

        return userValidator;
    }

    public static UserValidator validateNonNullFields(UserEntity user) {
        UserValidator userValidator = new UserValidator();

        //if field not null – do match, else – do none
        if(user.getLogin() != null && !userValidator.loginPattern.matcher(user.getLogin()).matches()) userValidator.addFail("Wrong login");
        if(user.getPassword() != null && !userValidator.passwordPattern.matcher(user.getPassword()).matches()) userValidator.addFail("Wrong password");

        return userValidator;
    }
}
