package com.inenergis.entity.genericEnum;

public enum AwardInstruction {
    ADS_AWARD("ADS Award"),
    EP_PREFERRED_SCHEDULE("EP Preferred Schedule");

    private String name;

    AwardInstruction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}