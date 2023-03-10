package com.inenergis.entity.genericEnum;

public enum RetailDispatchScheduleType {

    AUTO_DISPATCH_SCHEDULED("Auto Dispatch Scheduled"),
    MANUAL_DISPATCH_SCHEDULED("Manual Dispatch Scheduled"),
    DECLINE_DISPATCH("Decline dispatch");

    private String name;

    RetailDispatchScheduleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
