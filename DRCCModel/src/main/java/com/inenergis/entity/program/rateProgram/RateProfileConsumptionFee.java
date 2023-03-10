package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RATE_CONSUMPTION_FEE")
@Getter
@Setter
public class RateProfileConsumptionFee extends RateProfileFee implements Comparable {

    @Column(name = "EVENT")
    @Enumerated(EnumType.STRING)
    private RateEventFee event;

    @Column(name = "RATE_TYPE")
    @Enumerated(EnumType.STRING)
    private RateConsumptionFeeType rateType;

    @Transient
    @Override
    public boolean shouldBeInTheSameGroup(RateProfileFee otherFee) {
        RateProfileConsumptionFee otherAncillaryFee = (RateProfileConsumptionFee) otherFee;
        return super.shouldBeInTheSameGroup(otherFee)
                && (this.getEvent() == null && otherAncillaryFee.getEvent() == null || this.getEvent() != null && otherAncillaryFee.getEvent() != null && this.getEvent().equals(otherAncillaryFee.getEvent()))
                && (this.getRateType() == null && otherAncillaryFee.getRateType() == null || this.getRateType() != null && otherAncillaryFee.getRateType() != null && this.getRateType().equals(otherAncillaryFee.getRateType()));
    }
}