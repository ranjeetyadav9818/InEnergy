package com.inenergis.entity.maintenanceData;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("ANCILLARY_TYPE")
public class AncillaryFee extends MaintenanceData {
}
