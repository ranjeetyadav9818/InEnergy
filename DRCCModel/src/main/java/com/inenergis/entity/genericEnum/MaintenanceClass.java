package com.inenergis.entity.genericEnum;

import com.inenergis.entity.maintenanceData.FeeType;
import com.inenergis.entity.maintenanceData.PartyType;
import com.inenergis.entity.maintenanceData.PowerSource;

public enum MaintenanceClass {
    POWER_SOURCE("Power source", PowerSource.class),
    PARTY_TYPE("Party type", PartyType.class),
    FEE_TYPE("Fee type", FeeType.class);

    private String name;
    private Class clazz;

    MaintenanceClass(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }
}