package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.genericEnum.AwardInstruction;
import com.inenergis.entity.genericEnum.BidSubmissionIsoInterval;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTime;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTimeHours;
import com.inenergis.entity.genericEnum.DispatchType;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.MarketType;
import com.inenergis.entity.genericEnum.MeterIntervalType;
import com.inenergis.entity.genericEnum.MinutesOrHoursOrDays;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@ToString(exclude = "profile")
@Entity
@Table(name = "MI_ISO_PROFILE_PRODUCT")
public class IsoProduct extends IdentifiableEntity {

    @ManyToOne
    @JoinColumn(name = "MI_ISO_PROFILE_ID")
    private IsoProfile profile;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "MARKET_TYPE")
    @Enumerated(EnumType.STRING)
    private MarketType marketType;

    @Column(name = "RESOURCE_MIN_CAPACITY")
    private Long resourceMinCapacity;

    @Column(name = "RESOURCE_MIN_CAPACITY_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit resourceMinCapacityUnit;

    @Column(name = "TELEMETRY_MIN_THRESHOLD")
    private Long telemetryMinThreshold;

    @Column(name = "TELEMETRY_MIN_THRESHOLD_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit telemetryMinThresholdUnit;

    @Column(name = "METER_INTERVAL_TYPE")
    @Enumerated(EnumType.STRING)
    private MeterIntervalType meterIntervalType;

    @Column(name = "DISPATCH_TYPE")
    @Enumerated(EnumType.STRING)
    private DispatchType dispatchType;

    @Column(name = "AWARD_INSTRUCTION")
    @Enumerated(EnumType.STRING)
    private AwardInstruction awardInstruction;

    @Column(name = "MAX_RESOURCE_SIZE_VALUE")
    private BigDecimal maxResourceSizeValue;

    @Column(name = "MAX_RESOURCE_SIZE_UNIT")
    @Enumerated(EnumType.STRING)
    private ElectricalUnit maxResourceSizeUnit;

    @Column(name = "CURTAILMENT_RESPONSE_TIME_VALUE")
    private Integer curtailmentResponseTimeValue;

    @Column(name = "CURTAILMENT_RESPONSE_TIME_UNIT")
    @Enumerated(EnumType.STRING)
    private MinutesOrHoursOrDays curtailmentResponseTimeUnit;

    @Column(name = "BID_ON_MONDAY")
    private boolean bidOnMonday;
    @Column(name = "BID_ON_TUESDAY")
    private boolean bidOnTuesday;
    @Column(name = "BID_ON_WEDNESDAY")
    private boolean bidOnWednesday;
    @Column(name = "BID_ON_THURSDAY")
    private boolean bidOnThursday;
    @Column(name = "BID_ON_FRIDAY")
    private boolean bidOnFriday;
    @Column(name = "BID_ON_SATURDAY")
    private boolean bidOnSaturday;
    @Column(name = "BID_ON_SUNDAY")
    private boolean bidOnSunday;

    @Transient
    private List<String> selectedDays = new ArrayList<>();

    @PostLoad
    public void onLoad() {
        if (bidOnMonday) {
            selectedDays.add("Mon");
        }
        if (bidOnTuesday) {
            selectedDays.add("Tue");
        }
        if (bidOnWednesday) {
            selectedDays.add("Wed");
        }
        if (bidOnThursday) {
            selectedDays.add("Thu");
        }
        if (bidOnFriday) {
            selectedDays.add("Fri");
        }
        if (bidOnSaturday) {
            selectedDays.add("Sat");
        }
        if (bidOnSunday) {
            selectedDays.add("Sun");
        }
    }

    public void onCreate() {
        bidOnMonday = selectedDays.contains("Mon");
        bidOnTuesday = selectedDays.contains("Tue");
        bidOnWednesday = selectedDays.contains("Wed");
        bidOnThursday = selectedDays.contains("Thu");
        bidOnFriday = selectedDays.contains("Fri");
        bidOnSaturday = selectedDays.contains("Sat");
        bidOnSunday = selectedDays.contains("Sun");
    }

    @Column(name = "BID_SUBMISSION_SC_HOUR")
    private Integer bidSubmissionScHour;
    @Column(name = "BID_SUBMISSION_SC_MINUTE")
    private Integer bidSubmissionScMinute;
    @Column(name = "BID_SUBMISSION_SC_ON")
    @Enumerated(EnumType.STRING)
    private BidSubmissionTradeTime bidSubmissionScOn;

    @Column(name = "BID_SUBMISSION_ISO_HOUR")
    private Integer bidSubmissionIsoHour;
    @Column(name = "BID_SUBMISSION_ISO_MINUTE")
    private Integer bidSubmissionIsoMinute;
    @Column(name = "BID_SUBMISSION_ISO_ON")
    @Enumerated(EnumType.STRING)
    private BidSubmissionTradeTimeHours bidSubmissionIsoOn;

    @Column(name = "AUTO_BID_LOW_RISK")
    private boolean autoBidLowRisk;

    @Column(name = "AUTO_DISPATCH")
    private boolean autoDispatch = true;

    @Column(name = "AUTO_BID_LOW_RISK_HOUR")
    private Integer autoBidLowRiskHour;
    @Column(name = "AUTO_BID_LOW_RISK_MINUTE")
    private Integer autoBidLowRiskMinute;
    @Column(name = "AUTO_BID_LOW_RISK_ON")
    @Enumerated(EnumType.STRING)
    private BidSubmissionTradeTime autoBidLowRiskOn;

    public void setBidSubmissionSc(LocalTime hour) {
        if (hour != null) {
            bidSubmissionScHour = hour.getHour();
            bidSubmissionScMinute = hour.getMinute();
        } else {
            bidSubmissionScHour = null;
            bidSubmissionScMinute = null;
        }
    }

    public LocalTime getBidSubmissionSc() {
        if (bidSubmissionScHour == null || bidSubmissionScMinute == null) {
            return null;
        }
        return LocalTime.of(bidSubmissionScHour, bidSubmissionScMinute);
    }

    public void setBidSubmissionIso(LocalTime hour) {
        if (hour != null) {
            if (hour.equals(BidSubmissionIsoInterval.HourBid_75.getHour())) {
                bidSubmissionIsoHour = 0;
                bidSubmissionIsoMinute = BidSubmissionIsoInterval.HourBid_75.getMinutes();
            } else {
                bidSubmissionIsoHour = hour.getHour();
                bidSubmissionIsoMinute = hour.getMinute();
            }
        } else {
            bidSubmissionIsoHour = null;
            bidSubmissionIsoMinute = null;
        }
    }

    public LocalTime getBidSubmissionIso() {
        if (bidSubmissionIsoHour == null && bidSubmissionIsoMinute == null) {
            return null;
        } else if (bidSubmissionIsoHour == null || bidSubmissionIsoMinute > 59) {
            return BidSubmissionIsoInterval.HourBid_75.getHour();
        } else {
            return LocalTime.of(bidSubmissionIsoHour, bidSubmissionIsoMinute);
        }
    }

    public void setAutoBidLowRiskLT(LocalTime hour) {
        if (hour != null) {
            autoBidLowRiskHour = hour.getHour();
            autoBidLowRiskMinute = hour.getMinute();
        } else {
            autoBidLowRiskHour = null;
            autoBidLowRiskMinute = null;
        }
    }

    public LocalTime getAutoBidLowRiskLT() {
        if (autoBidLowRiskHour == null) {
            autoBidLowRiskHour = 0;
        }
        if (autoBidLowRiskMinute != null) {
            return LocalTime.of(autoBidLowRiskHour, autoBidLowRiskMinute);
        } else {
            return null;
        }
    }
}