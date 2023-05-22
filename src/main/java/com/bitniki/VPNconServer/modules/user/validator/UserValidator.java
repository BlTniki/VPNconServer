package com.bitniki.VPNconServer.modules.user.validator;

import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.model.UserFromRequest;
import com.bitniki.VPNconServer.validator.Validator;

import java.util.regex.Pattern;

public class UserValidator extends Validator {
    // accepted login: 1-20 length, alphabet and number chars and -_. chars
    public static final Pattern loginPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_.]{1,20}$");
    // accepted password: contains lower and capital letters and numbers
    public static final Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$");

    private static boolean isLoginNotMatches(String login) {
        return !loginPattern.matcher(login).matches();
    }

    private static boolean isPasswordNotMatches(String password) {
        return !passwordPattern.matcher(password).matches();
    }

    public static UserValidator validateAllFields(UserEntity user) {
        UserValidator userValidator = new UserValidator();

        //if field null – addFail, else do match
        if( user.getLogin() == null || isLoginNotMatches(user.getLogin())) userValidator.addFail("Wrong login");
        if( user.getPassword() == null || isPasswordNotMatches(user.getPassword())) userValidator.addFail("Wrong password");

        return userValidator;
    }

    public static UserValidator validateAllFields(UserFromRequest user) {
        UserValidator userValidator = new UserValidator();

        //if field null – addFail, else do match
        if( user.getLogin() == null || isLoginNotMatches(user.getLogin())) userValidator.addFail("Wrong login");
        if( user.getPassword() == null || isPasswordNotMatches(user.getPassword())) userValidator.addFail("Wrong password");

        return userValidator;
    }

    public static UserValidator validateNonNullFields(UserEntity user) {
        UserValidator userValidator = new UserValidator();

        //if field not null – do match, else – do none
        if( user.getLogin() != null && isLoginNotMatches(user.getLogin())) userValidator.addFail("Wrong login");
        if( user.getPassword() != null && isPasswordNotMatches(user.getPassword())) userValidator.addFail("Wrong password");

        return userValidator;
    }

    public static UserValidator validateNonNullFields(UserFromRequest user) {
        UserValidator userValidator = new UserValidator();

        //if field not null – do match, else – do none
        if( user.getLogin() != null && isLoginNotMatches(user.getLogin())) userValidator.addFail("Wrong login");
        if( user.getPassword() != null && isPasswordNotMatches(user.getPassword())) userValidator.addFail("Wrong password");

        return userValidator;
    }
}
