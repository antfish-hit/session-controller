package com.sony.session;

public enum ActionType {
    START("Start"),STOP("Stop");

    private String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
