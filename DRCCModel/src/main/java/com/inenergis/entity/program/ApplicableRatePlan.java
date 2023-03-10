package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@EqualsAndHashCode(of = "ratePlanProfile", callSuper = false)
@Table(name = "APPLICABLE_RATE_PLAN")
public class ApplicableRatePlan extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_PROFILE_ID")
    private RatePlanProfile ratePlanProfile;

    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_ID")
    private RatePlan ratePlan;

    public ApplicableRatePlan() {
    }

    public ApplicableRatePlan(RatePlan ratePlan) {
        this.ratePlan = ratePlan;
    }
}