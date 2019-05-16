package com.milos.config;

public class ServerPort {
    private int value;

    public ServerPort(int value) throws IllegalArgumentException {
        if (value < 1) {
            throw new IllegalArgumentException("Server port value must be greater than zero");
        }
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
