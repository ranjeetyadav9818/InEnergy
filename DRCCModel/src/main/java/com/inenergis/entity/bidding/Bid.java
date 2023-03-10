package com.inenergis.entity.bidding;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.BidAction;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "BID")
@ToString(exclude = {"segments", "registrationSubmissionStatus"})
public class Bid extends IdentifiableEntity {

    @Column(name = "TRADE_DATE")
    private Date tradeDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BidStatus status;

    @Column(name = "DETAILED_STATUS")
    private String detailedStatus;

    @OneToMany(mappedBy = "bid", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Segment> segments;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    private IsoResource isoResource;

    @ManyToOne
    @JoinColumn(name = "REGISTRATION_ID")
    private RegistrationSubmissionStatus registration;

    @Column(name = "SCHEDULE_MODIFIED")
    private boolean scheduleModified;

    @Column(name = "DEFAULT_SCHEDULE")
    private boolean defaultSchedule = true;

    @Column(name = "SUBMITTED_TIME")
    private Date submittedTime;

    @Column(name = "SUBMITTED_BY")
    private String submittedBy;

    @Column(name = "CANCEL_REASON")
    private String cancelReason;

    @Column(name = "LAST_RESET")
    private Date lastReset;

    @Transient
    BidProperties properties;

    public Bid() {

    }

    public Bid(IsoResource isoResource) {
        boolean autoBidLowRisk = isoResource.getIsoProduct().isAutoBidLowRisk();
        this.isoResource = isoResource;
        this.registration = isoResource.getActiveRegistration();
        if (this.registration == null) {
            throw new RuntimeException("A bid cannot be created ni a resource without active registration");
        }
        properties = new BidProperties(registration);
    }


    @PostLoad
    public void onLoad() {
        properties = new BidProperties(registration, tradeDate);
    }

    public Bid(IsoResource isoResource, Date tradeDate) {
        this(isoResource);
        this.tradeDate = tradeDate;
        if (properties != null) {
            properties.reCalculateResourceAdequacyPotential(tradeDate);
        } else {
            properties = new BidProperties(registration, tradeDate);
        }
        segments = new ArrayList<>();
    }

    @Transient
    public Long getTotalBid() {
        long totalBid = 0L;
        for (Segment segment : segments) {
            for (Long capacity : segment.getCapacitiesAsList()) {
                if (capacity != null) {
                    totalBid += capacity;
                }
            }
        }
        return totalBid;

    }

    public Collection<BidAction> getActions() {
        switch (status) {
            case ACCEPTED:
                return Arrays.asList(BidAction.CANCEL);
            case AUTO_BID:
                return Arrays.asList(BidAction.BID, null, BidAction.NO_BID);
            case NO_BID:
                return Arrays.asList(BidAction.BID);
            case ACTION_REQUIRED:
                return Arrays.asList(BidAction.BID, null, BidAction.NO_BID);
            case EXCEPTIONS:
                return Arrays.asList(BidAction.BID, null, BidAction.CANCEL);
            default:
                return Arrays.asList();
        }
    }

    public LocalDate getTradeLocalDate() {
        if (tradeDate == null) {
            return null;
        }
        return tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public boolean isBiddableOnDayOfWeek(DayOfWeek dayOfWeek) {
        IsoProduct isoProduct = isoResource.getIsoProduct();
        switch (dayOfWeek) {
            case MONDAY:
                return isoProduct.isBidOnMonday();
            case TUESDAY:
                return isoProduct.isBidOnTuesday();
            case WEDNESDAY:
                return isoProduct.isBidOnWednesday();
            case THURSDAY:
                return isoProduct.isBidOnThursday();
            case FRIDAY:
                return isoProduct.isBidOnFriday();
            case SATURDAY:
                return isoProduct.isBidOnSaturday();
            case SUNDAY:
                return isoProduct.isBidOnSunday();
            default:
                return true;
        }
    }
}




