package com.inenergis.entity.genericEnum;


public enum DispatchBatchStatus {

    NEW(0, "New"),
    ACTIVE(1, "Active"),
    TIME_OUT(2, "Time Out (hourly only)."),
    CLOSED(3, "Closed"),
    EMERGENCY_CANCELLED(4, "Emergency Cancelled"),
    OPERATOR_RESPONSE_PERIOD(5, "Operator Response Period (hourly only)"),
    DEFERRED(6, "Deferred.");


    private String name;
    private Integer value;

    DispatchBatchStatus(Integer value, String name) {
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

    public static DispatchBatchStatus getType(Integer id) {
        if (id == null) {
            return null;
        }
        for (DispatchBatchStatus dispatchBatchStatus : DispatchBatchStatus.values()) {
            if (dispatchBatchStatus.getValue() == id) {
                return dispatchBatchStatus;
            }
        }
        return null;

    }
}
