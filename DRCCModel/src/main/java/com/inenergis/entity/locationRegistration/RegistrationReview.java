package com.inenergis.entity.locationRegistration;

public enum RegistrationReview {
    YES("Yes"),
    NO("No"),
    AUTO("Auto");

    private String name;

    RegistrationReview(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}