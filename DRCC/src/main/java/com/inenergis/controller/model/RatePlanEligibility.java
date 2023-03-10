package com.inenergis.controller.model;

import com.inenergis.entity.program.RatePlan;
import lombok.Data;

@Data
public class RatePlanEligibility {

    private RatePlan ratePlan;
    private boolean eligible;
    private boolean enrollable;
    private boolean unenrollable;
    private String ineligibleFields;
}
