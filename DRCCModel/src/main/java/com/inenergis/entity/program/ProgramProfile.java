package com.inenergis.entity.program;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.bidding.SafetyReductionFactorHe;
import com.inenergis.entity.genericEnum.AggregatorDispatchType;
import com.inenergis.entity.genericEnum.DRMS;
import com.inenergis.entity.genericEnum.MinutesOrHours;
import com.inenergis.entity.marketIntegration.IsoProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(of = {"name", "effectiveStartDate"})
@Entity
@Table(name = "PROGRAM_PROFILE")
public class ProgramProfile extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;

    @Column(name = "APP_BACK_DATING_NEW_ENROLL")
    private boolean appBackDatingNewEnroll;

    @Column(name = "APP_BACK_DATING_UNENROLL")
    private boolean appBackDatingUnenroll;

    @Column(name = "APP_FUTURE_DATING_NEW_ENROLL")
    private boolean appFutureDatingNewEnroll;

    @Column(name = "APP_FUTURE_DATING_UNENROLL")
    private boolean appFutureDatingUnenroll;

    @Column(name = "ENROLLMENT_EFFECTIVE_DATE")
    private String enrollmentEffectiveDate;

    @Column(name = "EARLIEST_EFFECTIVE_DATE")
    private String earliestEffectiveDate;

    @Column(name = "SA_STATUS_REVIEW_EXCEPTION")
    private boolean saStatusReviewException;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "SERVICE_TYPE_REVIEW_EXCEPTION")
    private boolean serviceTypeReviewException;

    @Column(name = "CUSTOMER_TYPE_REVIEW_EXCEPTION")
    private boolean customerTypeReviewException;

    @Column(name = "RATE_SCHEDULE_REVIEW_EXCEPTION")
    private boolean rateScheduleReviewException;

    @Column(name = "PREMISE_TYPE_REVIEW_EXCEPTION")
    private boolean premiseTypeReviewException;

    @Column(name = "3RD_PARTY_DRP")
    private String thirdPartyDRP;

    @Column(name = "3RD_PARTY_DRP_REVIEW_EXCEPTION")
    private boolean thirdPartyDRPReviewException;

    @Column(name = "MEDICAL_BASELINE")
    private String medicalBaseline;

    @Column(name = "MEDICAL_BASELINE_REVIEW_EXCEPTION")
    private boolean medicalBaselineReviewException;

    @Column(name = "LIFE_SUPPORT")
    private String lifeSupport;

    @Column(name = "LIFE_SUPPORT_REVIEW_EXCEPTION")
    private boolean lifeSupportReviewException;

    @Column(name = "ROLL_USE_FLAG_RESIDENTIAL")
    private boolean rollUseFlagResidential;

    @Column(name = "ROLL_USE_FLAG_RESIDENTIAL_SIGN")
    private String rollUseFlagResidentialSign;

    @Column(name = "ROLL_USE_FLAG_RESIDENTIAL_VALUE")
    private BigDecimal rollUseFlagResidentialValue;

    @Column(name = "ROLL_USE_FLAG_COMMERCIAL")
    private boolean rollUseFlagCommercial;

    @Column(name = "ROLL_USE_FLAG_COMMERCIAL_SIGN")
    private String rollUseFlagCommercialSign;

    @Column(name = "ROLL_USE_FLAG_COMMERCIAL_VALUE")
    private BigDecimal rollUseFlagCommercialValue;

    @Column(name = "ROLL_USE_FLAG_REVIEW_EXCEPTION")
    private boolean rollUseFlagReviewException;

    @Column(name = "MIN_METER_READ_CYCLE")
    private String minutesMeterReadCycle;

    @Column(name = "CDW_NOTIFY_TYPE")
    private String cdwNotifyType;

    @Column(name = "CDW_NOTIFY_EMAIL")
    private String cdwNotifyEmail;

    @Column(name = "EQUIPMENT_REQUIRED")
    private boolean equipmentRequired;

    @Column(name = "EQUIPMENT_INSTALL_PRIOR_TO")
    private String equipmentInstallPriorTo;

    @Column(name = "EQUIPMENT_REQUIRED_REVIEW_EXCEPTION")
    private boolean equipmentRequiredReviewException;

    @Column(name = "SERVICE_LEVEL_REVIEW_EXCEPTION")
    private boolean serviceLevelReviewException;

    @Column(name = "ELIGIBILITY_AUTO_RETRY")
    private boolean eligibilityAutoRetry;

    @Column(name = "ELIGIBILITY_AUTO_RETRY_DAYS")
    private String eligibilityAutoRetryDays;

    @Column(name = "MULTIPLE_PARTICIPATION_REVIEW_EXCEPTION")
    private boolean multipleParticipationReviewException;

    @Column(name = "DEMAND_REVIEW_EXCEPTION")
    private boolean demandReviewException;

    @Column(name = "WHOLESALE_MARKET_ELIGIBLE")
    private boolean wholesaleMarketEligible;

    @Column(name = "WHOLESALE_MARKET_ELIGIBLE_TS")
    private Date wholesaleMarketEligibleTs;

    @Column(name = "WHOLESALE_PARTICIPATION_ACTIVE")
    private boolean wholesaleParticipationActive;

    @Column(name = "WHOLESALE_PARTICIPATION_ACTIVE_TS")
    private Date wholesaleParticipationActiveTs;

    @Column(name = "WHOLESALE_AUTORESOURCE_MAINTENAINCAE")
    private boolean wholesaleAutoresourceMaintenance;

    @Column(name = "WHOLESALE_STATISTICAL_SAMPLING_METHOD")
    private boolean wholesaleStatisticalSamplingMethod;

    @Column(name = "WHOLESALE_CROSS_PROGRAM_AGGREGATION")
    private boolean wholesaleCrossProgramAggregation;

    @ManyToOne
    @JoinColumn(name = "WHOLESALE_ISO_PRODUCT_ID")
    private IsoProduct wholesaleIsoProduct;

    @Column(name = "DEFAULT_BID_HOURS_FROM")
    @Enumerated(EnumType.STRING)
    private HourEnd defaultBidHoursFrom;

    @Column(name = "DEFAULT_BID_HOURS_TO")
    @Enumerated(EnumType.STRING)
    private HourEnd defaultBidHoursTo;

    @Column(name = "DEFAULT_BID_PRICE")
    private Long defaultBidPrice;

    @Column(name = "DUAL_EVENT_BID_PRICE")
    private Long dualEventBidPrice;

    @Column(name = "CONSECUTIVE_DISPATCH_DAYS")
    private Integer consecutiveDispatchDays;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SafetyReductionFactorHe> safetyReductionFactors;

    @Column(name = "DRMS")
    @Enumerated(EnumType.STRING)
    private DRMS drms;

    @Column(name = "AGGREGATOR_DISPATCH_TYPE")
    @Enumerated(EnumType.STRING)
    private AggregatorDispatchType aggregatorDispatchType;

    @Column(name = "MIN_TIME_SHED_LOAD_TIME")
    private Integer minTimeShedLoadTime;

    @Column(name = "MIN_TIME_SHED_LOAD_UNIT")
    @Enumerated(EnumType.STRING)
    private MinutesOrHours minTimeShedLoadUnit;

    @Column(name = "FSL_TIME_HORIZON")
    private Integer flsTimeHorizon;

    @Column(name = "USE_COMPANY_LEVEL_SEASONS")
    private boolean useCompanyLevelSeasons;

    @Column(name = "USE_COMPANY_LEVEL_HOLIDAYS")
    private boolean useCompanyLevelHolidays;

    @Transient
    private Long drOption;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramOption> options;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramSeason> seasons;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DRMSProgramMapping> drmsProgramMappings;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SublapProgramMapping> sublapProgramMappings;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramHoliday> holidays;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEnroller> enrollers;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEnrollmentAttribute> enrollmentAttributes;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEnrollmentNotification> enrollmentNotifications;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEquipment> equipments;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramCustomerDataAttributeChanges> customerDataAttributeChanges;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramDemand> programDemands;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramMultipleParticipation> programMultiParticipations;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEligibilitySaStatus> saStatuses;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEligibilityCustomerType> customerTypes;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEligibilityRateSchedule> rateSchedules;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEligibilityPremiseType> premiseTypes;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramDefinedDispatchLevel> dispatchLevels;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEventDuration> eventDurations;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramCustomerNotification> customerNotifications;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramDefinedDispatchReason> dispatchReasons;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramFirmServiceLevelRule> fslRules;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EligibleProgram> eligiblePrograms;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramEssentialCustomer> essentialCustomers;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramProfileAsset> programProfileAssets;

    @OneToMany(mappedBy = "programProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditDiscount> creditDiscounts;

    public ProgramProfile() {
        this.eligiblePrograms = new ArrayList();
        this.essentialCustomers = new ArrayList();
        this.programProfileAssets = new ArrayList();
        this.creditDiscounts = new ArrayList();
        this.drmsProgramMappings = new ArrayList();
        this.sublapProgramMappings = new ArrayList();
        this.holidays = new ArrayList();
        this.enrollers = new ArrayList();
        this.enrollmentAttributes = new ArrayList();
        this.enrollmentNotifications = new ArrayList();
        this.equipments = new ArrayList();
        this.customerDataAttributeChanges = new ArrayList();
        this.programDemands = new ArrayList();
        this.programMultiParticipations = new ArrayList();
        this.saStatuses = new ArrayList();
        this.customerTypes = new ArrayList();
        this.rateSchedules = new ArrayList();
        this.premiseTypes = new ArrayList();
        this.dispatchLevels = new ArrayList();
        this.eventDurations = new ArrayList();
        this.customerNotifications = new ArrayList();
        this.dispatchReasons = new ArrayList();
        this.fslRules = new ArrayList();
        this.seasons = new ArrayList();
        this.options = new ArrayList();
        this.safetyReductionFactors = new ArrayList();
    }

    @PreUpdate
    @PrePersist
    public void onUpdate() {
        lastUpdate = new Date();
        if (effectiveEndDate != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(effectiveEndDate.toInstant(), ZoneId.systemDefault()).toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
            effectiveEndDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public boolean isAttributeInTheProgramEnrollmentAttributeList(String attribute) {
        ProgramEnrollmentAttribute attributeToCheck = new ProgramEnrollmentAttribute();
        attributeToCheck.setName(attribute);
        return getEnrollmentAttributes() != null && getEnrollmentAttributes().contains(attributeToCheck);
    }

    @Transient
    public List<ProgramFirmServiceLevelRule> getEssentialFSLRules() {
        return getFSLRulesByEssential(true);
    }

    @Transient
    public List<ProgramFirmServiceLevelRule> getNonEssentialFSLRules() {
        return getFSLRulesByEssential(false);
    }

    private List<ProgramFirmServiceLevelRule> getFSLRulesByEssential(boolean essential) {
        if (fslRules != null) {
            List<ProgramFirmServiceLevelRule> result = new ArrayList<>();
            for (ProgramFirmServiceLevelRule fslRule : fslRules) {
                if (fslRule.isEssential() == essential) {
                    result.add(fslRule);
                }
            }
            return result;
        }
        return null;
    }

    public enum ConsecutiveDispatchDays {

        DAY1(1),
        DAY2(2),
        DAY3(3),
        DAY4(4),
        DAY5(5),
        DAY6(6),
        DAY7(7),
        DAY8(8),
        DAY9(9),
        DAY10(10);


        private final Integer dayNumber;

        ConsecutiveDispatchDays(final Integer dayNumber) {
            this.dayNumber = dayNumber;
        }

        public Integer getDayNumber() {
            return dayNumber;
        }
    }

    public ConsecutiveDispatchDays[] getValuesForConsecutiveDispatchDays() {
        return ConsecutiveDispatchDays.values();
    }

    public ProgramCustomerNotification getDefaultNotification() {
        if (!getCustomerNotifications().isEmpty()) {
            // todo: to be completed in PDR development
            return getCustomerNotifications().get(0);
        }

        return null;
    }
    public List<Asset> getAssets(){
        if (CollectionUtils.isEmpty(programProfileAssets)) {
            return new ArrayList<>();
        }
        return programProfileAssets.stream().map(a -> a.getAsset()).collect(Collectors.toList());
    }
}