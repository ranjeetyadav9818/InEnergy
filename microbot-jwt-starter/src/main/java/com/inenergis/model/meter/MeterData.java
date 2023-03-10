package com.inenergis.model.meter;

import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

import static com.inenergis.entity.genericEnum.RateConsumptionFeeType.CREDIT;
import static com.inenergis.entity.genericEnum.RateConsumptionFeeType.DEBIT;

@Getter
@Setter
@Entity
@Table(name = "COMMODITY_INTERVAL_DATA")
public class MeterData {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "service_point_id")
    private String servicePointId;

    @Column(name = "secondary_sp_id")
    private String secondaryServicePointId;

    @Column(name = "usage_value")
    private BigDecimal value;

    @Column(name = "units")
    private String units;

    @Column(name = "date")
    private String date;

    @Column(name = "usage_time")
    private String time;

    @Column(name = "is_estimate")
    private String isEstimate;

    @Column(name = "daylight_savings")
    private String daylightSavings;

    @Column(name = "direction")
    private String direction;

    public RateConsumptionFeeType getType() {
        return value.compareTo(BigDecimal.ZERO) > 0 ? DEBIT : CREDIT;
    }
}
