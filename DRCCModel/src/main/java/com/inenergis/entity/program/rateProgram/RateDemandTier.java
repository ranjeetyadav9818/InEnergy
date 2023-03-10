package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.genericEnum.TierBoundType;
import com.inenergis.entity.genericEnum.TierDemandInterval;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("DEMAND")
@Getter
@Setter
public class RateDemandTier extends RateTier {

    @Column(name = "DEMAND_INTERVAL")
    @Enumerated(EnumType.STRING)
    private TierDemandInterval interval;
}