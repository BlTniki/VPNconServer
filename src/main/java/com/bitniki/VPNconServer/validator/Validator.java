package com.bitniki.VPNconServer.validator;

import java.util.List;

public class Validator {
    private List<String> fails;

    private void addFail(String message) {
        fails.add(message);
    }

    public Boolean hasFails() {
        return fails.size() != 0;
    }

    @Override
    public String toString() {
        return String.join("\n", fails);
    }
}
