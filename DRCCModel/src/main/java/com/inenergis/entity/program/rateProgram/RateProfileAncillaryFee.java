package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.genericEnum.GasRateAncillaryFrequency;
import com.inenergis.entity.genericEnum.RateAncillaryFrequency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RATE_ANCILLARY_FEE")
@Getter
@Setter
public class RateProfileAncillaryFee extends RateProfileFee {

    @Column(name = "FREQUENCY")
    @Enumerated(EnumType.STRING)
    private RateAncillaryFrequency frequency;

    @Column(name = "GAS_FREQUENCY")
    @Enumerated(EnumType.STRING)
    private GasRateAncillaryFrequency gasFrequency;



    @Transient
    @Override
    public boolean shouldBeInTheSameGroup(RateProfileFee otherFee) {
        RateProfileAncillaryFee otherAncillaryFee = (RateProfileAncillaryFee) otherFee;
        return super.shouldBeInTheSameGroup(otherFee)
                && (this.getFrequency() == null && otherAncillaryFee.getFrequency() == null
                            || this.getFrequency() != null && otherAncillaryFee.getFrequency() != null && this.getFrequency().equals(otherAncillaryFee.getFrequency()));
    }
}