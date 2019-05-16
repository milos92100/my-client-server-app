package com.milos.config;

import com.milos.domain.util.Validator;

public class ServerIp {
    private String value;

    public ServerIp(String value) {
        if (!Validator.validIP(value)) {
            throw new IllegalArgumentException("The given value '" + value + "' is not a valid IP");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

