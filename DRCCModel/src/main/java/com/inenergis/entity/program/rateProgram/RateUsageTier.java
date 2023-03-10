package com.inenergis.entity.program.rateProgram;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("USAGE")
@Getter
@Setter
public class RateUsageTier extends RateTier {

}