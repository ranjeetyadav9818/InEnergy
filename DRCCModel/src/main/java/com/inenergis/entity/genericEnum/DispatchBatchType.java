package com.inenergis.entity.genericEnum;

public enum DispatchBatchType {

    FIVE_MINUTE_DISPATCHABLE(0, "5 minute dispatchable"),
    HOURLY_PRE_DISPATCH(1, "Hourly Pre-Dispatch"),
    COMMITMENT(2, "Commitment (Startup/Shutdown)"),
    AS_AWARDS(3, "AS Awards "),
    OOS(4, "OOS Instructions"),
    PREDISPATCH(5, "Pre-Dispatch Hourly AS Awards");


    private String name;
    private Integer value;

    DispatchBatchType(Integer value, String name) {
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

    public static DispatchBatchType getType(Integer id) {
        if (id == null) {
            return null;
        }
        for (DispatchBatchType dispatchBatchTipe : DispatchBatchType.values()) {
            if (dispatchBatchTipe.getValue() == id) {
                return dispatchBatchTipe;
            }
        }
        return null;

    }


}
