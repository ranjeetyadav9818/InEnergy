package com.inenergis.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "METER_DATA_USAGE")
public class MeterDataUsage extends IdentifiableEntity{

    @Column(name = "SERVICE_POINT_ID")
    private String servicePointId;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "CONSUMPTION_WATTS")
    private Long consumptionWatts;

    @Column(name = "PRICE")
    private BigDecimal price;
}
