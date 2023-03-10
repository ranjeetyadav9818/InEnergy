package com.inenergis.entity.meterData;

import com.inenergis.entity.genericEnum.CommodityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * Created by egamas on 01/12/2017.
 */
@Entity
@Getter
@Setter
@Table(name = "PEAK_DEMAND_INTERVAL_DATA_TEST")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeakDemandMeterData implements Serializable {

    protected static final String TAB = "\t";
    @Transient
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    @Transient
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000000");
    @Transient
    private String uuid = UUID.randomUUID().toString();

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "meter_reading_id")
    private String meterReadingId;

    @Column(name = "service_point_id")
    public String servicePointId;

    @Column(name = "secondary_sp_id")
    public String secondarySpId;

    @Column(name = "usage_value")
    public BigDecimal value;

    @Column(name = "date")
    public String date;

    @Column(name = "usage_time")
    public String time;

    @Column(name = "units")
    public String units;

    @Column(name = "is_estimate")
    public String isEstimate;

    @Column(name = "daylight_savings")
    public String daylightSavings;

    @Column(name = "commodity_type")
    @Enumerated(EnumType.STRING)
    public CommodityType commodityType;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(TAB);
        joiner.add(StringUtils.defaultString(meterReadingId))
                .add(StringUtils.defaultString(servicePointId))
                .add(StringUtils.defaultString(secondarySpId))
                .add(StringUtils.defaultString(decimalFormat.format(value)))
                .add(StringUtils.defaultString(date))
                .add(StringUtils.defaultString(time))
                .add(StringUtils.defaultString(units))
                .add(StringUtils.defaultString(isEstimate))
                .add(StringUtils.defaultString(daylightSavings));
        if (commodityType != null) {
            joiner.add(commodityType.name());
        }
        return joiner.toString();
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.parse(time, formatter);
    }
}
