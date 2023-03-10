package com.inenergis.entity.genericEnum;

public enum EnrolmentStatus {


    CANCELLED("Cancelled"),
    UNENROLLED("Unenrolled"),
    REINSTATE("Reinstate"),
    ENROLLED("Enrolled"),
    IN_PROGRESS("In Progress"),
    UNKNOWN("Unknown");

    private String name;

    EnrolmentStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EnrolmentStatus getByName(String nameQueried) {
        for (EnrolmentStatus enrolmentStatus : values()) {
            if(enrolmentStatus.getName().equalsIgnoreCase(nameQueried)){
                return enrolmentStatus;
            }
        }
        return null;
    }
}