package com.inenergis.entity.program;

public enum CapUnit {
    kW,MW,Customer;

    public String getLabel(){
        return this.name();
    }
}
