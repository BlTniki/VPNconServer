package com.bitniki.VPNconServer.validator;

import java.util.ArrayList;
import java.util.List;

/*
* uses for validations
* the succeeding class should create static validate method
* which fill fails list and calling code should check hasFails()
* */

public class Validator {
    private final List<String> fails;

    protected void addFail(String message) {
        fails.add(message);
    }

    public Boolean hasFails() {
        return fails.size() != 0;
    }

    @Override
    public String toString() {
        return String.join("\n", fails);
    }

    public Validator() {
        this.fails = new ArrayList<>();
    }
}
