package com.inenergis.entity.workflow;

import com.inenergis.entity.program.RatePlanEnrollment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue("RATE")
public class RatePlanInstance extends PlanInstance {
    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_ENROLLMENT_ID")
    private RatePlanEnrollment ratePlanEnrollment;

}
