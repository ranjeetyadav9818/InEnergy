package com.inenergis.entity.program;

import com.inenergis.entity.ContractType;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.*;
import com.inenergis.entity.program.rateProgram.RateCodeProfile;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryFee;
import com.inenergis.entity.program.rateProgram.RateProfileAncillaryPercentageFee;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "RATE_PLAN_PROFILE")
public class RatePlanProfile extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTRACT_TYPE")
    private String contractTypes;

    @Column(name = "TARIFF_RESOLUTION_NUMBER")
    private String tariffResolutionNumber;

    @Column(name = "TARIFF_REFERENCES_NOTE", length = 65535, columnDefinition = "Text")
    private String tariffReferencesNote;

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;

    @Column(name = "TARIFF_ISSUE_DATE")
    private Date tariffIssueDate;

    @Column(name = "EFFECTIVE_START_DATE")
    private Date effectiveStartDate;

    @Column(name = "EFFECTIVE_END_DATE")
    private Date effectiveEndDate;


    @ManyToOne
    @JoinColumn(name = "RATE_PLAN_ID")
    private RatePlan ratePlan;

    @Column(name = "RATE_SCHEDULE_TITLE")
    private String rateScheduleTitle;

    @Column(name = "RATE_CODE_ID")
    private String rateCodeId;

    @Column(name = "RATE_CODE_SECTOR")
    @Enumerated(EnumType.STRING)
    private RateCodeSector rateCodeSector;


    @Column(name = "GAS_CODE_SECTOR")
    @Enumerated(EnumType.STRING)
    private GasRateCodeSector gasRateCodeSector;

    //change
    @Column(name = "APPLICABLE_CONTRACT")
    @Enumerated(EnumType.STRING)
    private ApplicableContractEnuum applicableContractEnuum;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "USE_COMPANY_LEVEL_SEASONS")
    private boolean useCompanyLevelSeasons;

    @Column(name = "USE_COMPANY_LEVEL_HOLIDAYS")
    private boolean useCompanyLevelHolidays;

    @Column(name = "USE_COMPANY_LEVEL_TOU")
    private boolean useCompanyLevelTOU;

    @Column(name = "DESIGN_TYPE")
    @Enumerated(EnumType.STRING)
    private DesignType designType;

    @Column(name = "TIER_TYPE")
    @Enumerated(EnumType.STRING)
    private TierType tierType;

    @Column(name = "GAS_TIER_TYPE")
    @Enumerated(EnumType.STRING)
    private GasTierType gasTierType;

    @Column(name = "DESIGN_SUB_TYPE")
    @Enumerated(EnumType.STRING)
    private DesignSubType designSubType;

    @Column(name = "SERVICE_TYPE")
    @Enumerated(EnumType.STRING)
    private RatePlanServiceType serviceType;

    @Column(name = "POWER_SOURCE")
    private String powerSource;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicableRatePlan> applicableRatePlans;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonCalendar> seasonCalendars;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HolidayCalendar> holidayCalendars;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeOfUseCalendar_TO_DELETE> timeOfUseCalendarTODELETES;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatePlanDemand> ratePlanDemands;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneralAvailability> generalAvailabilities;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateCodeProfile> rateCodeProfiles;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargesAttribute> chargesAttributes;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditDiscount> creditDiscounts;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatePlanProfileAsset> ratePlanProfileAssets;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateTier> rateTiers;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateProfileConsumptionFee> consumptionFees;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateProfileAncillaryFee> ancillaryFees;

    @OneToMany(mappedBy = "ratePlanProfile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateProfileAncillaryPercentageFee> ancillaryPercentageFees;

    @Column(name = "BILLING_TERM_FREQUENCY")
    @Enumerated(value = EnumType.STRING)
    private BillingTermFrequency billingTermFrequency;

    @Column(name = "BILLING_DAY_OF_WEEK")
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek billingDayOfWeek;

    @Column(name = "BILLING_DAY_OF_MONTH")
    private Integer billingDayOfMonth;

    @Column(name = "BILLING_MONTH")
    private Integer billingMonth;

    @Column(name = "LAST_PAYMENT_DATE")
    private LocalDate lastPaymentDate;

    @Column(name = "APPLICABILITY_DESCRIPTION")
    private String applicabilityDescription;

    public RatePlanProfile() {
        applicableRatePlans = new ArrayList<>();
        seasonCalendars = new ArrayList<>();
        holidayCalendars = new ArrayList<>();
        timeOfUseCalendarTODELETES = new ArrayList<>();
        ratePlanDemands = new ArrayList<>();
        generalAvailabilities = new ArrayList<>();
        rateCodeProfiles = new ArrayList<>();
        chargesAttributes = new ArrayList<>();
        creditDiscounts = new ArrayList<>();
        ratePlanProfileAssets = new ArrayList<>();
        rateTiers = new ArrayList<>();
        consumptionFees = new ArrayList<>();
        ancillaryFees = new ArrayList<>();
    }

    @PreUpdate
    @PrePersist
    public void onUpdate() {
        lastUpdate = new Date();
        rateCodeId = buildRateCodeId();
    }

    public String buildRateCodeId() {
        if (CollectionUtils.isEmpty(rateCodeProfiles)) {
            return StringUtils.EMPTY;
        }
        StringBuilder sbRateCodeId = new StringBuilder();
        rateCodeProfiles.stream()
                .sorted(RateCodeProfile.getComparator())
                .map(c -> c.getRateCode().getName())
                .forEach(sbRateCodeId::append);
        return sbRateCodeId.toString();
    }

    public BigDecimal calculateAverageRate() {
        int numberOfFees = 0;
        BigDecimal total = new BigDecimal(0L);
        if (chargesAttributes != null) {
            for (ChargesAttribute chargesAttribute : chargesAttributes) {
                if (chargesAttribute.getFees() != null) {
                    for (ChargesFee fee : chargesAttribute.getFees()) {
                        total = total.add(fee.getFeeAmount());
                        numberOfFees++;
                    }
                }
            }
        }
        return numberOfFees == 0 ? null : total.divide(new BigDecimal(numberOfFees));
    }

    public String printRelatedRatePlansCodeIds() {
        StringBuilder sb = new StringBuilder();
        if (applicableRatePlans != null) {
            for (ApplicableRatePlan applicableRatePlan : applicableRatePlans) {
                if (applicableRatePlan.getRatePlan().getActiveProfile() != null) {
                    sb = sb.append(applicableRatePlan.getRatePlan().getName()).append(" ");
                }
            }
        }
        return sb.toString();
    }

    public RatePlanProfile(RatePlan ratePlan) {
        super();
        this.ratePlan = ratePlan;
        applicableRatePlans = new ArrayList<>();
        seasonCalendars = new ArrayList<>();
        holidayCalendars = new ArrayList<>();
        timeOfUseCalendarTODELETES = new ArrayList<>();
        ratePlanDemands = new ArrayList<>();
        generalAvailabilities = new ArrayList<>();
        rateCodeProfiles = new ArrayList<>();
        chargesAttributes = new ArrayList<>();
        creditDiscounts = new ArrayList<>();
        ratePlanProfileAssets = new ArrayList<>();
        rateTiers = new ArrayList<>();
        consumptionFees = new ArrayList<>();
        ancillaryFees = new ArrayList<>();
        ancillaryPercentageFees = new ArrayList<>();
    }

    public void addRateTier(RateTier rateTier) {
        if (rateTiers == null) {
            rateTiers = new ArrayList<>();
        }
        rateTiers.add(rateTier);
    }

    public void addConsumptionRate(RateProfileConsumptionFee fee) {
        if (consumptionFees == null) {
            consumptionFees = new ArrayList<>();
        }
        consumptionFees.add(fee);
    }

    public void addAncillaryFee(RateProfileAncillaryFee fee) {
        if (ancillaryFees == null) {
            ancillaryFees = new ArrayList<>();
        }
        ancillaryFees.add(fee);
    }

    public void addAncillaryPercentageFee(RateProfileAncillaryPercentageFee fee) {
        if (ancillaryPercentageFees == null) {
            ancillaryPercentageFees = new ArrayList<>();
        }
        ancillaryPercentageFees.add(fee);
    }

    public List<RateProfileConsumptionFee> getActiveConsumptionFees() {
        return consumptionFees.stream().filter(f -> f.isActive() && f.getRateTier().isActive()).collect(Collectors.toList());
    }

    public List<RateProfileAncillaryFee> getActiveAncillaryFees() {
        return ancillaryFees.stream().filter(f -> f.isActive() && f.getRateTier().isActive()).collect(Collectors.toList());
    }

    public List<RateProfileAncillaryPercentageFee> getActiveAncillaryPercentageFees() {
        return ancillaryPercentageFees.stream().filter(f -> f.isActive() ).collect(Collectors.toList());
    }

    public List<RateTier> getActiveRateTiers() {
        return rateTiers.stream().filter(RateTier::isActive).collect(Collectors.toList());
    }
}
