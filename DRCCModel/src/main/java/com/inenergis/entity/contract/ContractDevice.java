package com.inenergis.entity.contract;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.MinutesOrHoursOrDays;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.program.HourEnd;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "CONTRACT_DEVICE")
@EqualsAndHashCode(of = {"device","energyContract"})
@NoArgsConstructor
public class ContractDevice extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "DEVICE_ID")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_ID")
    private EnergyContract energyContract;

    @Column(name = "ESTIMATED_CAPACITY")
    private Long estimatedCapacity;

    @Column(name = "ESTIMATED_CAPACITY_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit estimatedCapacityUnit;

    @Column(name = "RAMP_RATE")
    private Long rampRate;

    @Column(name = "RAMP_RATE_UNIT")
    @Enumerated(EnumType.STRING)
    private MinutesOrHoursOrDays rampRateUnit;

    @Column(name = "HOUR_END_FROM")
    @Enumerated(EnumType.STRING)
    private HourEnd availableHoursFrom;

    @Column(name = "HOUR_END_TO")
    @Enumerated(EnumType.STRING)
    private HourEnd availableHoursTo;


    public ContractDevice(Device device, EnergyContract energyContract) {
        super();
        this.device = device;
        this.energyContract = energyContract;
    }
}
