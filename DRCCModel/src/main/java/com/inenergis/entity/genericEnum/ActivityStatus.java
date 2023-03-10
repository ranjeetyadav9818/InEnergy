package com.inenergis.entity.genericEnum;

public enum ActivityStatus {

    ACTIVE("Active", true),
    INACTIVE("Inactive", false);
    private String statusName;

    ActivityStatus(String currentStatus, boolean boolStatus) {
        this.statusName = currentStatus;
    }

    static ActivityStatus getByValue(boolean value) {
        return value ? ACTIVE : INACTIVE;
    }

    public String getStatusName() {
        return statusName;
    }

    //todo replace ResourceCaroussel.STATUS
}