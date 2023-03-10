package com.inenergis.entity.trove;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.workflow.ModifiableHourEnd;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@ToString(of = {"serviceAgreementId", "hourEndType", "hourEndDate"})
@EqualsAndHashCode(of = {"serviceAgreementId", "hourEndType", "hourEndDate"}, callSuper = false)
@Data
@Entity
@Table(name = "METER_FORECAST")
public class MeterForecast extends IdentifiableEntity implements Serializable, ModifiableHourEnd {

    public static final String REFERENCCE_LOAD = "ref_load";
    public static final String EVENT_LOAD = "ev_load";

    @Transient
    public String uiName = "Forecasted Capacity (MW)";

    @Transient
    public String type = "forecasted";

    @ManyToOne
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    @JsonManagedReference
    ServiceAgreement serviceAgreement;

    @Column(name = "MEASURE_TYPE")
    public String measureType;

    @Column(name = "MEASURE_DATE")
    public Date measureDate;

    @Column(name = "HOUR_END_1")
    public long hourEnd1;

    @Column(name = "HOUR_END_2")
    public long hourEnd2;

    @Column(name = "HOUR_END_3")
    public long hourEnd3;

    @Column(name = "HOUR_END_4")
    public long hourEnd4;

    @Column(name = "HOUR_END_5")
    public long hourEnd5;

    @Column(name = "HOUR_END_6")
    public long hourEnd6;

    @Column(name = "HOUR_END_7")
    public long hourEnd7;

    @Column(name = "HOUR_END_8")
    public long hourEnd8;

    @Column(name = "HOUR_END_9")
    public long hourEnd9;

    @Column(name = "HOUR_END_10")
    public long hourEnd10;

    @Column(name = "HOUR_END_11")
    public long hourEnd11;

    @Column(name = "HOUR_END_12")
    public long hourEnd12;

    @Column(name = "HOUR_END_13")
    public long hourEnd13;

    @Column(name = "HOUR_END_14")
    public long hourEnd14;

    @Column(name = "HOUR_END_15")
    public long hourEnd15;

    @Column(name = "HOUR_END_16")
    public long hourEnd16;

    @Column(name = "HOUR_END_17")
    public long hourEnd17;

    @Column(name = "HOUR_END_18")
    public long hourEnd18;

    @Column(name = "HOUR_END_19")
    public long hourEnd19;

    @Column(name = "HOUR_END_20")
    public long hourEnd20;

    @Column(name = "HOUR_END_21")
    public long hourEnd21;

    @Column(name = "HOUR_END_22")
    public long hourEnd22;

    @Column(name = "HOUR_END_23")
    public long hourEnd23;

    @Column(name = "HOUR_END_24")
    public long hourEnd24;

    public Long[] getHourEndsAsArray() {

        return new Long[]{
                getHourEnd1(),
                getHourEnd2(),
                getHourEnd3(),
                getHourEnd4(),
                getHourEnd5(),
                getHourEnd6(),
                getHourEnd7(),
                getHourEnd8(),
                getHourEnd9(),
                getHourEnd10(),
                getHourEnd11(),
                getHourEnd12(),
                getHourEnd13(),
                getHourEnd14(),
                getHourEnd15(),
                getHourEnd16(),
                getHourEnd17(),
                getHourEnd18(),
                getHourEnd19(),
                getHourEnd20(),
                getHourEnd21(),
                getHourEnd22(),
                getHourEnd23(),
                getHourEnd24()
        };
    }

    public Object getHourEnd(int hour) {
        switch (hour) {
            case 1:
                return hourEnd1;
            case 2:
                return hourEnd2;
            case 3:
                return hourEnd3;
            case 4:
                return hourEnd4;
            case 5:
                return hourEnd5;
            case 6:
                return hourEnd6;
            case 7:
                return hourEnd7;
            case 8:
                return hourEnd8;
            case 9:
                return hourEnd9;
            case 10:
                return hourEnd10;
            case 11:
                return hourEnd11;
            case 12:
                return hourEnd12;
            case 13:
                return hourEnd13;
            case 14:
                return hourEnd14;
            case 15:
                return hourEnd15;
            case 16:
                return hourEnd16;
            case 17:
                return hourEnd17;
            case 18:
                return hourEnd18;
            case 19:
                return hourEnd19;
            case 20:
                return hourEnd20;
            case 21:
                return hourEnd21;
            case 22:
                return hourEnd22;
            case 23:
                return hourEnd23;
            case 24:
                return hourEnd24;
            default:
                return 0;
        }
    }

    @Override
    public void setHour(int hour, Long  value) {

        switch (hour) {
            case 1:
                hourEnd1 = value;
                break;
            case 2:
                hourEnd2 = value;
                break;
            case 3:
                hourEnd3 = value;
                break;
            case 4:
                hourEnd4 = value;
                break;
            case 5:
                hourEnd5 = value;
                break;
            case 6:
                hourEnd6 = value;
                break;
            case 7:
                hourEnd7 = value;
                break;
            case 8:
                hourEnd8 = value;
                break;
            case 9:
                hourEnd9 = value;
                break;
            case 10:
                hourEnd10 = value;
                break;
            case 11:
                hourEnd11 = value;
                break;
            case 12:
                hourEnd12 = value;
                break;
            case 13:
                hourEnd13 = value;
                break;
            case 14:
                hourEnd14 = value;
                break;
            case 15:
                hourEnd15 = value;
                break;
            case 16:
                hourEnd16 = value;
                break;
            case 17:
                hourEnd17 = value;
                break;
            case 18:
                hourEnd18 = value;
                break;
            case 19:
                hourEnd19 = value;
                break;
            case 20:
                hourEnd20 = value;
                break;
            case 21:
                hourEnd21 = value;
                break;
            case 22:
                hourEnd22 = value;
                break;
            case 23:
                hourEnd23 = value;
                break;
            case 24:
                hourEnd24 = value;
                break;
        }
    }
}
