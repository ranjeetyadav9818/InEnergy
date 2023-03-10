package com.inenergis.entity.maintenanceData;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("PARTY_TYPE")
public class PartyType extends MaintenanceData {
}
