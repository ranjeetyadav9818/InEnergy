package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the SERVICE_AGREEMENT database table.
 */
@Entity
@Table(name = "GAS_SERVICE_AGREEMENT")
@NamedQuery(name = "GasServiceAgreement.findAll", query = "SELECT s FROM GasServiceAgreement s")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "serviceAgreementId"
)
@DiscriminatorValue("Gas")
public class GasServiceAgreement extends BaseServiceAgreement  {

    private List<GasPdpSrParticipant> gasPdpSrParticipants = new ArrayList<>();

    private List<GasCustomerNotificationPreference> customerNotificationPreferences = new ArrayList<>();
    //private List<GasProgramServiceAgreementEnrollment> enrollments = new ArrayList<>();

//    public List<MeterForecast> meterForecasts;
    private List<GasSecondaryContact> secondaryContacts = new ArrayList<>();
//    private List<GasEnergyContract> gasEnergyContracts = new ArrayList<>();
//    private List<GasPortalUser> portalUsers = new ArrayList<>();

    public GasServiceAgreement() {
    }

    //bi-directional many-to-one association to PdpSrNotification
    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<GasPdpSrParticipant> getPdpSrParticipants() {
        return gasPdpSrParticipants;
    }


    public void setPdpSrParticipants(List<GasPdpSrParticipant> gasPdpSrParticipants) {
        this.gasPdpSrParticipants = gasPdpSrParticipants;
    }


    //bi-directional many-to-one association to PdpSrNotification
    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<GasCustomerNotificationPreference> getCustomerNotificationPreferences() {
        return customerNotificationPreferences;
    }


    public void setCustomerNotificationPreferences(List<GasCustomerNotificationPreference> customerNotificationPreferences) {
        this.customerNotificationPreferences = customerNotificationPreferences;
    }

//    @OneToMany(mappedBy = "serviceAgreement")
//    @JsonBackReference
//    public List<GasProgramServiceAgreementEnrollment> getEnrollments() {
//        return enrollments;
//    }

//    @OneToMany(mappedBy = "serviceAgreement")
//    @JsonBackReference
//    public List<ServiceAgreementAsset> getAssets() {
//        return assets;
//    }
//
//    public void setAssets(List<ServiceAgreementAsset> assets) {
//        this.assets = assets;
//    }
//
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    @JoinTable(name = "GAS_CONTRACT_SERVICE_AGREEMENT", joinColumns = {@JoinColumn(name = "GAS_SERVICE_AGREEMENT_ID", nullable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "GAS_ENERGY_CONTRACT_ID", nullable = false)})
//    @JsonBackReference
//    public List<GasEnergyContract> getEnergyContracts() {
//        return gasEnergyContracts;
//    }
//
//    public void setEnergyContracts(List<GasEnergyContract> gasEnergyContracts) {
//        this.gasEnergyContracts = gasEnergyContracts;
//    }

//    public void setEnrollments(List<GasProgramServiceAgreementEnrollment> enrollments) {
//        this.enrollments = enrollments;
//    }

//    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonBackReference
//    public List<GasMeterForecast> getMeterForecasts() {
//        return meterForecasts;
//    }
//
//    public void setMeterForecasts(List<GasMeterForecast> meterForecasts) {
//        this.meterForecasts = meterForecasts;
//    }

//    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonBackReference
//    public List<GasRatePlanEnrollment> getRatePlanEnrollments() {
//        return ratePlanEnrollments;
//    }
//
//    public void setRatePlanEnrollments(List<GasRatePlanEnrollment> ratePlanEnrollments) {
//        this.ratePlanEnrollments = ratePlanEnrollments;
//    }

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    public List<GasSecondaryContact> getSecondaryContacts() {
        return secondaryContacts;
    }

    public void setSecondaryContacts(List<GasSecondaryContact> secondaryContacts) {
        this.secondaryContacts = secondaryContacts;
    }

//    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference
//    public List<GasPortalUser> getPortalUsers() {
//        return portalUsers;
//    }

//    public void setPortalUsers(List<GasPortalUser> portalUsers) {
//        this.portalUsers = portalUsers;
//    }



}