package com.inenergis.entity.meterData;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Antonio on 13/06/2017.
 */
@Data
@Entity
@Table(name="COMMODITY_INTERVAL_DATA")
public class LatestMeterData {
    @Column(name="service_point_id")
    @Id
    private String servicePointId;

    @Column(name="usage_time")
    private String time;

    @Column(name = "date")
    private String date;
}
