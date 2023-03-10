package com.inenergis.entity.program.rateProgram;

import com.inenergis.entity.genericEnum.TierDemandInterval;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("VOLTAGE")
@Getter
@Setter
public class RateVoltageTier extends RateTier {

}