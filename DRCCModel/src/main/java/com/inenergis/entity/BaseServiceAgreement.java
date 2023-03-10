package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.Payment;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="BASE_SERVICE_AGREEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "COMMODITY", discriminatorType = DiscriminatorType.STRING)
public class BaseServiceAgreement {

    protected static final long serialVersionUID = 1L;
    protected String serviceAgreementId;
    protected Account account;
    protected String druid;
    protected Date druidCreated;
    protected String druidWarning;
    protected String has3rdPartyDrp;
    protected boolean lifeSupportInd;
    protected String mailingAddress1;
    protected String mailingAddress2;
    protected String mailingCityUpr;
    protected String mailingPostal;
    protected String mailingState;
    protected boolean medicalBaselineInd;
    protected String phone;
    protected String rateSchedule;
    protected String saStatus;
    protected boolean supplierIsDrp;
    protected String uniqSaId;
    protected Date uniqSaIdCreateDate;
    protected String uniqSaIdWarnFlag;
    protected String saUuid;
    protected Date startDate;
    protected Date endDate;
    protected String naics;
    protected String billCycleCd;
    protected String custClassCd;
    protected String revenueClassDesc;
    protected boolean feraFlag;
    protected String billSystem;
    protected String cust_size;
    protected String marketSegment;
    protected boolean careFlag;
    protected String typeCd;
    protected String customerLSECompanyName;
    protected Date rateCodeEffectiveDate;
    protected String businessActivityDescription;
    protected String divisionCode19;
    protected String climateZone;
    protected String essDivisionCode;
    protected boolean resInd;
    protected String customerLseCode;
    protected String essentialCustomerFlag;
    protected Long averageSummerOnPeakWatt;

    private List<PortalUser> portalUsers = new ArrayList<>();



    private List<ProgramServiceAgreementEnrollment> enrollments = new ArrayList<>();
    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<ProgramServiceAgreementEnrollment> getEnrollments() {
        return enrollments;
    }
    public void setEnrollments(List<ProgramServiceAgreementEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    private List<AgreementPointMap> agreementPointMaps = new ArrayList<>();
    private List<RatePlanEnrollment> ratePlanEnrollments = new ArrayList<>();
    private List<ServiceAgreementAsset> assets = new ArrayList<>();
    private List<Invoice> invoices = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    public List<MeterForecast> meterForecasts;
    private List<EnergyContract> energyContracts = new ArrayList<>();

    public BaseServiceAgreement() {
    }

    public enum SaStatus {

        INCOMPLETE("05", "Incomplete"),
        PENDING_START("10", "Pending start"),
        ACTIVE("20", "Active"),
        PENDING_STOP("30", "Pending stop"),
        STOPPED("40", "Stopped"),
        REACTIVATED("50", "Reactivated"),
        CLOSED("60", "Closed"),
        CANCELLED("70", "Cancelled");

        protected final String value;
        protected final String text;

        private SaStatus(final String value, final String text) {
            this.text = text;
            this.value = value;
        }

        public String toString() {
            return text;
        }

        public String getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }


    @Id
    @Column(name = "SERVICE_AGREEMENT_ID")
    public String getServiceAgreementId() {
        return this.serviceAgreementId;
    }

    public void setServiceAgreementId(String serviceAgreementId) {
        this.serviceAgreementId = serviceAgreementId;
    }


    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    @JsonManagedReference
    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Column(name = "DRUID")
    public String getDruid() {
        return this.druid;
    }

    public void setDruid(String druid) {
        this.druid = druid;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DRUID_CREATED")
    public Date getDruidCreated() {
        return this.druidCreated;
    }

    public void setDruidCreated(Date druidCreated) {
        this.druidCreated = druidCreated;
    }


    @Column(name = "DRUID_WARNING")
    public String getDruidWarning() {
        return this.druidWarning;
    }

    public void setDruidWarning(String druidWarning) {
        this.druidWarning = druidWarning;
    }


    @Column(name = "HAS_3RD_PARTY_DRP")
    public String getHas3rdPartyDrp() {
        return this.has3rdPartyDrp;
    }

    public void setHas3rdPartyDrp(String has3rdPartyDrp) {
        this.has3rdPartyDrp = has3rdPartyDrp;
    }


    @Column(name = "LIFE_SUPPORT_IND")
    public boolean isLifeSupportInd() {
        return this.lifeSupportInd;
    }

    public void setLifeSupportInd(boolean lifeSupportInd) {
        this.lifeSupportInd = lifeSupportInd;
    }


    @Column(name = "MAILING_ADDRESS1")
    public String getMailingAddress1() {
        return this.mailingAddress1;
    }

    public void setMailingAddress1(String mailingAddress1) {
        this.mailingAddress1 = mailingAddress1;
    }


    @Column(name = "MAILING_ADDRESS2")
    public String getMailingAddress2() {
        return this.mailingAddress2;
    }

    public void setMailingAddress2(String mailingAddress2) {
        this.mailingAddress2 = mailingAddress2;
    }


    @Column(name = "MAILING_CITY_UPR")
    public String getMailingCityUpr() {
        return this.mailingCityUpr;
    }

    public void setMailingCityUpr(String mailingCityUpr) {
        this.mailingCityUpr = mailingCityUpr;
    }


    @Column(name = "MAILING_POSTAL")
    public String getMailingPostal() {
        return this.mailingPostal;
    }

    public void setMailingPostal(String mailingPostal) {
        this.mailingPostal = mailingPostal;
    }


    @Column(name = "MAILING_STATE")
    public String getMailingState() {
        return this.mailingState;
    }

    public void setMailingState(String mailingState) {
        this.mailingState = mailingState;
    }


    @Column(name = "MEDICAL_BASELINE_IND")
    public boolean isMedicalBaselineInd() {
        return this.medicalBaselineInd;
    }

    public void setMedicalBaselineInd(boolean medicalBaselineInd) {
        this.medicalBaselineInd = medicalBaselineInd;
    }


    @Column(name = "PHONE")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Column(name = "RATE_SCHEDULE")
    public String getRateSchedule() {
        return this.rateSchedule;
    }

    public void setRateSchedule(String rateSchedule) {
        this.rateSchedule = rateSchedule;
    }


    @Column(name = "SA_STATUS")
    public String getSaStatus() {
        return this.saStatus;
    }

    public void setSaStatus(String saStatus) {
        this.saStatus = saStatus;
    }


    @Column(name = "SUPPLIER_IS_DRP")
    public boolean isSupplierIsDrp() {
        return supplierIsDrp;
    }


    public void setSupplierIsDrp(boolean supplierIsDrp) {
        this.supplierIsDrp = supplierIsDrp;
    }

    @Column(name = "UNIQ_SA_ID")
    public String getUniqSaId() {
        return uniqSaId;
    }


    public void setUniqSaId(String uniqSaId) {
        this.uniqSaId = uniqSaId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UNIQ_SA_ID_CREATE_DATE")
    public Date getUniqSaIdCreateDate() {
        return uniqSaIdCreateDate;
    }


    public void setUniqSaIdCreateDate(Date uniqSaIdCreateDate) {
        this.uniqSaIdCreateDate = uniqSaIdCreateDate;
    }

    @Column(name = "UNIQ_SA_ID_WARN_FLAG")
    public String getUniqSaIdWarnFlag() {
        return uniqSaIdWarnFlag;
    }


    public void setUniqSaIdWarnFlag(String uniqSaIdWarnFlag) {
        this.uniqSaIdWarnFlag = uniqSaIdWarnFlag;
    }

    @Column(name = "SA_UUID")
    public String getSaUuid() {
        return saUuid;
    }


    public void setSaUuid(String saUuid) {
        this.saUuid = saUuid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SA_START_DATE")
    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SA_END_DATE")
    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "SA_NAICS")
    public String getNaics() {
        return naics;
    }


    public void setNaics(String naics) {
        this.naics = naics;
    }

    @Column(name = "BILL_CYCLE_CD")
    public String getBillCycleCd() {
        return billCycleCd;
    }


    public void setBillCycleCd(String billCycleCd) {
        this.billCycleCd = billCycleCd;
    }

    @Column(name = "CUST_CLASS_CD")
    public String getCustClassCd() {
        return custClassCd;
    }


    public void setCustClassCd(String custClassCd) {
        this.custClassCd = custClassCd;
    }

    @Column(name = "REVENUE_CLASS_DESC")
    public String getRevenueClassDesc() {
        return revenueClassDesc;
    }


    public void setRevenueClassDesc(String revenueClassDesc) {
        this.revenueClassDesc = revenueClassDesc;
    }

    @Column(name = "FERA_FLAG")
    public boolean isFeraFlag() {
        return feraFlag;
    }


    public void setFeraFlag(boolean feraFlag) {
        this.feraFlag = feraFlag;
    }

    @Column(name = "BILL_SYSTEM")
    public String getBillSystem() {
        return billSystem;
    }


    public void setBillSystem(String billSystem) {
        this.billSystem = billSystem;
    }

    @Column(name = "CUST_SIZE")
    public String getCust_size() {
        return cust_size;
    }


    public void setCust_size(String cust_size) {
        this.cust_size = cust_size;
    }

    @Column(name = "MARKET_SEGMENT")
    public String getMarketSegment() {
        return marketSegment;
    }


    public void setMarketSegment(String marketSegment) {
        this.marketSegment = marketSegment;
    }

    @Column(name = "CARE_FLAG")
    public boolean isCareFlag() {
        return careFlag;
    }


    public void setCareFlag(boolean careFlag) {
        this.careFlag = careFlag;
    }


    @Column(name = "SA_TYPE_CD")
    public String getTypeCd() {
        return typeCd;
    }


    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
    }

    @Column(name = "CUSTOMER_LSE_COMPANY_NAME")
    public String getCustomerLSECompanyName() {
        return customerLSECompanyName;
    }


    public void setCustomerLSECompanyName(String customerLSECompanyName) {
        this.customerLSECompanyName = customerLSECompanyName;
    }

    @Column(name = "RATE_CODE_EFFECTIVE_DATE")
    public Date getRateCodeEffectiveDate() {
        return rateCodeEffectiveDate;
    }


    public void setRateCodeEffectiveDate(Date rateCodeEffectiveDate) {
        this.rateCodeEffectiveDate = rateCodeEffectiveDate;
    }

    @Column(name = "BUSINESS_ACTIVITY_DESC")
    public String getBusinessActivityDescription() {
        return businessActivityDescription;
    }


    public void setBusinessActivityDescription(String businessActivityDescription) {
        this.businessActivityDescription = businessActivityDescription;
    }

    @Column(name = "DIVISION_CODE_19")
    public String getDivisionCode19() {
        return divisionCode19;
    }


    public void setDivisionCode19(String divisionCode19) {
        this.divisionCode19 = divisionCode19;
    }

    @Column(name = "CLIMATE_ZONE")
    public String getClimateZone() {
        return climateZone;
    }


    public void setClimateZone(String climateZone) {
        this.climateZone = climateZone;
    }

    @Column(name = "ESS_DIVISION_CODE")
    public String getEssDivisionCode() {
        return essDivisionCode;
    }


    public void setEssDivisionCode(String essDivisionCode) {
        this.essDivisionCode = essDivisionCode;
    }

    @Column(name = "RES_IND")
    public boolean isResInd() {
        return resInd;
    }

    public void setResInd(boolean resInd) {
        this.resInd = resInd;
    }

    @Column(name = "CUSTOMER_LSE_CODE")
    public String getCustomerLseCode() {
        return customerLseCode;
    }

    public void setCustomerLseCode(String customerLseCode) {
        this.customerLseCode = customerLseCode;
    }

    @Column(name = "ESSENTIAL_CUSTOMER_FLAG")
    public String getEssentialCustomerFlag() {
        return essentialCustomerFlag;
    }

    public void setEssentialCustomerFlag(String essentialCustomerFlag) {
        this.essentialCustomerFlag = essentialCustomerFlag;
    }

    //bi-directional many-to-one association to AgreementPointMap
    @OneToMany(mappedBy = "serviceAgreement")
    public List<AgreementPointMap> getAgreementPointMaps() {
        return this.agreementPointMaps;
    }

    public void setAgreementPointMaps(List<AgreementPointMap> agreementPointMaps) {
        this.agreementPointMaps = agreementPointMaps;
    }

    public AgreementPointMap addAgreementPointMap(AgreementPointMap agreementPointMap) {
        getAgreementPointMaps().add(agreementPointMap);
        agreementPointMap.setServiceAgreement(this);

        return agreementPointMap;
    }

    public AgreementPointMap removeAgreementPointMap(AgreementPointMap agreementPointMap) {
        getAgreementPointMaps().remove(agreementPointMap);
        agreementPointMap.setServiceAgreement(null);

        return agreementPointMap;
    }

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    public List<RatePlanEnrollment> getRatePlanEnrollments() {
        return ratePlanEnrollments;
    }

    public void setRatePlanEnrollments(List<RatePlanEnrollment> ratePlanEnrollments) {
        this.ratePlanEnrollments = ratePlanEnrollments;
    }

    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<ServiceAgreementAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<ServiceAgreementAsset> assets) {
        this.assets = assets;
    }


    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "CONTRACT_SERVICE_AGREEMENT", joinColumns = {@JoinColumn(name = "SERVICE_AGREEMENT_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "ENERGY_CONTRACT_ID", nullable = false)})
    @JsonBackReference
    public List<EnergyContract> getEnergyContracts() {
        return energyContracts;
    }

    public void setEnergyContracts(List<EnergyContract> energyContracts) {
        this.energyContracts = energyContracts;
    }



    @Transient
    public String translateSAStatusLabel() {
        if ("05".equals(this.getSaStatus())) {
            return SaStatus.INCOMPLETE.getText();
        } else if ("10".equals(this.getSaStatus())) {
            return SaStatus.PENDING_START.getText();
        } else if ("20".equals(this.getSaStatus())) {
            return SaStatus.ACTIVE.getText();
        } else if ("30".equals(this.getSaStatus())) {
            return SaStatus.PENDING_STOP.getText();
        } else if ("40".equals(this.getSaStatus())) {
            return SaStatus.STOPPED.getText();
        } else if ("50".equals(this.getSaStatus())) {
            return SaStatus.REACTIVATED.getText();
        } else if ("60".equals(this.getSaStatus())) {
            return SaStatus.CLOSED.getText();
        } else if ("70".equals(this.getSaStatus())) {
            return SaStatus.CANCELLED.getText();
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseServiceAgreement that = (BaseServiceAgreement) o;

        if (serviceAgreementId != null ? !serviceAgreementId.equals(that.serviceAgreementId) : that.serviceAgreementId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return serviceAgreementId != null ? serviceAgreementId.hashCode() : 0;
    }

    public String idFieldName() {
        return "serviceAgreementId";
    }

    public List<String> relationshipFieldNames() {
        return Arrays.asList("account");
    }

    public List<String> excludedFieldsToCompare() {
        return Arrays.asList("pdpSrParticipants ", "agreementPointMaps", "customerNotificationPreferences");
    }

    public static String stripOutNoDigit(String phone) {
        if (phone != null && StringUtils.isNotBlank(phone)) {
            return phone.replaceAll("\\D+", "");
        }
        return phone;
    }

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    public List<PortalUser> getPortalUsers() {
        return portalUsers;
    }

    public void setPortalUsers(List<PortalUser> portalUsers) {
        this.portalUsers = portalUsers;
    }




    @Transient
    public Long getAverageSummerOnPeakWatt() {
        return averageSummerOnPeakWatt;
    }

    public void setAverageSummerOnPeakWatt(Long value) {
        averageSummerOnPeakWatt = value;
    }

    @Override
    public BaseServiceAgreement clone(){
        return this.clone();
    }



    @Transient
    public String getDecriminatorValue() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }



}
