package com.inenergis.entity.genericEnum;

public enum WorkPlanType {
    ENROL("Enroll" ,ProgramType.DEMAND_RESPONSE),
    UNENR("Unenroll" ,ProgramType.DEMAND_RESPONSE),
    RATE_PLAN_ENROLLMENT("Rate Plan Enrollment", ProgramType.RATE),
    RATE_PLAN_UNENROLLMENT("Rate Plan Unenrollment", ProgramType.RATE);

    private String name;
    private ProgramType programType;

    public ProgramType getProgramType() {
        return programType;
    }

    WorkPlanType(String name, ProgramType programType) {
        this.name = name;
        this.programType = programType;
    }

    public String getName() {
        return name;
    }
}