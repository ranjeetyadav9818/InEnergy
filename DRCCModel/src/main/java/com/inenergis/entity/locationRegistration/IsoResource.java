package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.Instruction;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.ProgramEventDuration;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.inenergis.util.EnergyUtil.convertToWatts;

@Entity
@Getter
@Setter
@Table(name = "ISO_RESOURCE")
public class IsoResource extends IdentifiableEntity {
    protected static final Logger log = LoggerFactory.getLogger(IsoResource.class);

    @Column(name = "SUBLAP")
    private String isoSublap;

    @Column(name = "LSE")
    private String isoLse;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REGISTRATION_REVIEW")
    @Enumerated(EnumType.STRING)
    private RegistrationReview registrationReview;

    @Column(name = "ISO_RESOURCE_ID")
    private String isoResourceId; //TODO DELETE COLUMN

    @OneToMany(mappedBy = "isoResource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RegistrationSubmissionStatus> registrations;

    @OneToMany(mappedBy = "isoResource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PmaxPmin> pmaxPminList;

    @ManyToOne
    @JoinColumn(name = "ISO_PRODUCT_ID")
    private IsoProduct isoProduct;

    @OneToMany(mappedBy = "isoResource", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Instruction> instructions;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Award> awards;

    @OneToMany(mappedBy = "isoResource", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImpactedResource> impactedResources;

    @OneToMany(mappedBy = "isoResource", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IsoOutage> outages;

    public RegistrationSubmissionStatus getActiveRegistration() {
        for (RegistrationSubmissionStatus status : getRegistrations()) {
            if (status.isActive()) {
                return status;
            }
        }
        return null;
    }

    public RegistrationSubmissionStatus getLastRegistrationInISO() {
        for (RegistrationSubmissionStatus registration : getRegistrations()) {
            if (registration.isActiveInISO()) {
                return registration;
            }
        }
        return null;
    }

    public PmaxPmin getActivePmaxPmin() {
        Date now = new Date();
        for (PmaxPmin pmaxPmin : pmaxPminList) {
            if (now.after(pmaxPmin.getEffectiveStartDate()) && (pmaxPmin.getEffectiveEndDate() == null || now.before(pmaxPmin.getEffectiveEndDate()))) {
                return pmaxPmin;
            }
        }
        return null;
    }

    @Transient
    private List<LocationSubmissionStatus> newLocations = new ArrayList<LocationSubmissionStatus>();

    @Transient
    public long getCalculatedCapacity() {
        //TODO cache this in memory
        int result = 0;
        RegistrationSubmissionStatus activeRegistration = getActiveRegistration();
        if (activeRegistration != null) {
            for (LocationSubmissionStatus location : activeRegistration.getLocations()) {
                result += location.getCalculatedCapacity();
            }
        }
        return result;
    }

    @Transient
    public boolean isLocationAssignable(LocationSubmissionStatus location) {

        ProgramProfile locationActiveProgramProfile = location.getProgram().getActiveProfile();
        IsoProduct isoProduct = locationActiveProgramProfile.getWholesaleIsoProduct();
        long currentResourceCapacity = this.getCurrentCapacity() + this.getCapacityFromNewLocations();

        long maxCapacityInWatts = 0;
        if (isoProduct.getDispatchType() != null && isoProduct.getMaxResourceSizeValue() != null) {
            maxCapacityInWatts = convertToWatts(isoProduct.getMaxResourceSizeValue().longValue(), isoProduct.getMaxResourceSizeUnit());
        }
        if ((currentResourceCapacity + location.getCalculatedCapacity()) > maxCapacityInWatts) {
            return false;
        }

        maxCapacityInWatts = 0;
        if (isoProduct.getTelemetryMinThreshold() != null) {
            maxCapacityInWatts = convertToWatts(isoProduct.getTelemetryMinThreshold(), isoProduct.getTelemetryMinThresholdUnit());
        }
        if ((currentResourceCapacity + location.getCalculatedCapacity()) > maxCapacityInWatts) {
            return false;
        }

        long resourceCurtailmentTimeInMinutes = calculateResourceCurtailmentTime(isoProduct);
        long locationCurtailmentTimeInMinutes = calculateLocationCurtailmentTime(locationActiveProgramProfile);
        log.info("resCurtailment {}, locCurtailment {}", resourceCurtailmentTimeInMinutes, locationCurtailmentTimeInMinutes);
        if (resourceCurtailmentTimeInMinutes != locationCurtailmentTimeInMinutes) {
            return false;
        }

        if (locationActiveProgramProfile.getDefaultBidHoursFrom() != null && locationActiveProgramProfile.getDefaultBidHoursTo() != null) {
            long minCapacityInWatts = convertToWatts(isoProduct.getResourceMinCapacity(), isoProduct.getResourceMinCapacityUnit());
            for (HourEnd hourEnd : HourEnd.values()) {
                if (hourEnd.getHourNumber() >= locationActiveProgramProfile.getDefaultBidHoursFrom().getHourNumber()
                        && hourEnd.getHourNumber() <= locationActiveProgramProfile.getDefaultBidHoursTo().getHourNumber()) {
                    long resourceCapacityForHE = this.getCurrentCapacity(hourEnd) + this.getCapacityFromNewLocations(hourEnd);
                    log.info("resourceCapacityForHE {}, calcCapacity {}, minCapacityInWatts {}", resourceCapacityForHE, location.getCalculatedCapacity(hourEnd), minCapacityInWatts);
                    if (resourceCapacityForHE + location.getCalculatedCapacity(hourEnd) < minCapacityInWatts) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private long getCapacityFromNewLocations() {
        long total = 0;
        if (newLocations != null) {
            for (LocationSubmissionStatus newLocation : newLocations) {
                total += newLocation.getCalculatedCapacity();
            }
        }
        return total;
    }

    private long getCapacityFromNewLocations(HourEnd hourEnd) {
        long total = 0;
        if (newLocations != null) {
            for (LocationSubmissionStatus newLocation : newLocations) {
                total += newLocation.getCalculatedCapacity(hourEnd);
            }
        }
        return total;
    }

    private long getCurrentCapacity() {
        return 0;
    }

    private long getCurrentCapacity(HourEnd hourEnd) {
        return 0;
    }

    private long calculateLocationCurtailmentTime(ProgramProfile locationActiveProgramProfile) {
        ProgramEventDuration programEventDuration = locationActiveProgramProfile.getEventDurations().get(0);
        return TimeUtil.convertToSeconds(programEventDuration.getMinLeadTimeToShedLoad().intValue(), programEventDuration.getMinLeadTimeToShedLoadUnits());
    }

    public long calculateResourceCurtailmentTime() {
        return calculateResourceCurtailmentTime(this.getIsoProduct());
    }

    private long calculateResourceCurtailmentTime(IsoProduct isoProduct) {
        return TimeUtil.convertToSeconds(isoProduct.getCurtailmentResponseTimeValue(), isoProduct.getCurtailmentResponseTimeUnit());
    }


    public BigDecimal getCalculatedCapacityInMW() {
        //TODO cache this in memory
        long calculatedCapacity = getCalculatedCapacity();
        if (calculatedCapacity > 0) {
            return BigDecimal.valueOf(getCalculatedCapacity()).divide(new BigDecimal(ConstantsProviderModel.MW_PRECISION));
        }
        return BigDecimal.ZERO;
    }
}