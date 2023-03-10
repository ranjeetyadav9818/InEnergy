package com.inenergis.entity.program;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Event;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.workflow.ProgramPlanInstance;
import com.inenergis.util.ConstantsProviderModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(of = {"program", "serviceAgreement"})
@Entity
@Table(name = "PROGRAM_SA_ENROLLMENT")
public class ProgramServiceAgreementEnrollment extends IdentifiableEntity {
    protected static final Logger log = LoggerFactory.getLogger(ProgramServiceAgreementEnrollment.class);

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;
    @ManyToOne
    @JoinColumn(name = "AGGREGATOR_ID")
    private ProgramAggregator aggregator;
    @ManyToOne
    @JoinColumn(name = "SA_ID")
    @JsonManagedReference
    private BaseServiceAgreement serviceAgreement;
    @Column(name = "ENROLLMENT_SOURCE")
    private String enrollmentSource;
    @Column(name = "ENROLLMENT_CHANNEL")
    private String enrollmentChannel;
    @Column(name = "ENROLLMENT_STATUS")
    private String enrollmentStatus;
    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;
    @Column(name = "ORIGINAL_EFFECTIVE_START_DATE")
    private Date originalEffectiveStartDate;
    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;
    @Column(name = "DRMS_ID")
    private String drmsId;
    @Column(name = "UNENROLL_REASON")
    private String unenrollReason;
    @Column(name = "THIRD_PARTY_NAME")
    private String thirdPartyName;
    @Column(name = "BILL_PROTECTION")
    private boolean billProtection;
    @Column(name = "RESERVATION_CAPACITY_APPLIED_VALUE")
    private String reservationCapacityAppliedValue;
    @Column(name = "PLAN_OPTIONS")
    private String planOptions;
    @Column(name = "AVERAGE_SUMMER_ON_PEAK_WATT")
    private Long averageSummerOnPeakWatt;
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProgramFirmServiceLevel> fsls;
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProgramEligibilitySnapshot> snapshots;
    @OneToMany(mappedBy = "programServiceAgreementEnrollment", fetch = FetchType.LAZY)
    private List<LocationSubmissionStatus> locations;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "impactedCustomers")
    private List<Event> events;
    @OneToMany(mappedBy = "programServiceAgreementEnrollment", fetch = FetchType.LAZY)
    private List<ProgramPlanInstance> planInstances;


    @Transient
    public boolean isActivelyEnrolled() {
        return enrollmentStatus != null && enrollmentStatus.equals(EnrolmentStatus.ENROLLED.getName()) && effectiveStartDate != null && effectiveStartDate.before(new Date());
    }

    @Transient
    public boolean isActivelyEnrolledOrInProgress() {
        return enrollmentStatus != null && (enrollmentStatus.equals(EnrolmentStatus.ENROLLED.getName()) || enrollmentStatus.equals(EnrolmentStatus.IN_PROGRESS.getName())
                || enrollmentStatus.equals(EnrolmentStatus.REINSTATE.getName()));
    }

    public String getOtherEnrollments() {
        StringBuilder sb = new StringBuilder();
        for (ProgramServiceAgreementEnrollment agreementEnrollment : (getServiceAgreement()).getEnrollments()) {
            if (!agreementEnrollment.getProgram().equals(this.getProgram()) && agreementEnrollment.getEffectiveEndDate() == null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(agreementEnrollment.getProgram().getName());
            }
        }
        return sb.toString();
    }

    @Transient
    public ProgramFirmServiceLevel getLastFSL() {
        if (fsls != null) {
            for (ProgramFirmServiceLevel fsl : fsls) {
                if (fsl.getEffectiveEndDate() == null) {
                    return fsl;
                }
            }
        }
        return null;
    }

    @Transient
    public ProgramFirmServiceLevel getCurrentFSL() {
        Date now = Date.from(ZonedDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        return getFSLByDate(now);
    }

    public ProgramFirmServiceLevel getFSLByDate(Date now) {
        if (fsls != null) {
            for (ProgramFirmServiceLevel fsl : fsls) {
                if (fsl.getEffectiveStartDate().before(now) && (fsl.getEffectiveEndDate() == null || fsl.getEffectiveEndDate().after(now))) {
                    return fsl;
                }
            }
        }
        log.warn("No fsl returned. fsls size {}, now: {}", fsls, now);
        return null;
    }

    public boolean isEligible() {
        if (snapshots != null) {
            for (ProgramEligibilitySnapshot snapshot : snapshots) {
                if (!snapshot.isEligible()) {
                    return false;
                }
            }
        }
        return true;
    }

    public LocationSubmissionStatus getLastLocation() {
        if (!CollectionUtils.isEmpty(getLocations())) {
            LocationSubmissionStatus result = getLocations().get(0);
            for (LocationSubmissionStatus locationSubmissionStatus : getLocations()) {
                if (result.getIsoSubmissionDate() == null || result.getIsoSubmissionDate().before(locationSubmissionStatus.getIsoSubmissionDate())) {
                    result = locationSubmissionStatus;
                }
            }
            return result;
        }
        return null;
    }

    public void setEffectiveStartDate(Date effectiveStartDate) {
        if (this.effectiveStartDate == null) {
            this.originalEffectiveStartDate = effectiveStartDate;
        }
        this.effectiveStartDate = effectiveStartDate;
    }
}