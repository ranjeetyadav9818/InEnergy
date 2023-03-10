package com.inenergis.entity.genericEnum;


public enum InstructionType {

    DOT(0,"DOT (5 minute/Hourly/OOS)"),
    MIN_CONSTRAINT(1,"Min Constraint (OOS) "),
    MAX_CONSTRAINT(2,"Max Constraint (OOS) "),
    FIXED_CONSTRAINT(3,"Fixed Constraint (OOS)"),
    START_UP(4,"Start up (Commitment)"),
    SHUT_DOWN(5,"Shut down (Commitment) "),
    CAPACITY_AWARD(6,"Capacity Award (AS Award)"),
    MSG_TRANSITION(7,"MSG Transition Instruction");

    private String name;
    private Integer value;

    InstructionType(Integer value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static InstructionType getType(Integer id) {
        if (id == null) {
            return null;
        }
        for (InstructionType instructionType : InstructionType.values()) {
            if (instructionType.getValue() == id) {
                return instructionType;
            }
        }
        return null;

    }
}

