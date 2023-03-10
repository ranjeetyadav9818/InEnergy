package com.inenergis.entity.genericEnum;

import lombok.Getter;

/**
 * Created by egamas on 18/10/2017.
 */
public enum EnrollmentChannel {
    PORTAL("Portal"),
    DRCC("DRCC");

    @Getter
    private String label;

    EnrollmentChannel(String name) {
        this.label = name;
    }


}
