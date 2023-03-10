package com.inenergis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.billing.Payment;
import com.inenergis.entity.marketIntegration.EnergyContract;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the SERVICE_AGREEMENT database table.
 */
@Entity
//@Table(name = "SERVICE_AGREEMENT")
@NamedQuery(name = "ServiceAgreement.findAll", query = "SELECT s FROM ServiceAgreement s")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "serviceAgreementId"
)
@DiscriminatorValue("Electricity")
public class ServiceAgreement extends BaseServiceAgreement implements Serializable, VModelEntity, Cloneable {



    private List<PdpSrParticipant> pdpSrParticipants = new ArrayList<>();

    //private List<AgreementPointMap> agreementPointMaps = new ArrayList<>();
    private List<CustomerNotificationPreference> customerNotificationPreferences = new ArrayList<>();

//    private List<ServiceAgreementAsset> assets = new ArrayList<>();

//    private List<RatePlanEnrollment> ratePlanEnrollments = new ArrayList<>();
    private List<SecondaryContact> secondaryContacts = new ArrayList<>();

//    private List<PortalUser> portalUsers = new ArrayList<>();
//
//    public ServiceAgreement() {
//    }


//    //bi-directional many-to-one association to AgreementPointMap
//    @OneToMany(mappedBy = "serviceAgreement")
//    public List<AgreementPointMap> getAgreementPointMaps() {
//        return this.agreementPointMaps;
//    }
//
//    public void setAgreementPointMaps(List<AgreementPointMap> agreementPointMaps) {
//        this.agreementPointMaps = agreementPointMaps;
//    }
//
//    public AgreementPointMap addAgreementPointMap(AgreementPointMap agreementPointMap) {
//        getAgreementPointMaps().add(agreementPointMap);
//        agreementPointMap.setServiceAgreement(this);
//
//        return agreementPointMap;
//    }
//
//    public AgreementPointMap removeAgreementPointMap(AgreementPointMap agreementPointMap) {
//        getAgreementPointMaps().remove(agreementPointMap);
//        agreementPointMap.setServiceAgreement(null);
//
//        return agreementPointMap;
//    }

    //bi-directional many-to-one association to PdpSrNotification
    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<PdpSrParticipant> getPdpSrParticipants() {
        return pdpSrParticipants;
    }



    public void setPdpSrParticipants(List<PdpSrParticipant> pdpSrParticipants) {
        this.pdpSrParticipants = pdpSrParticipants;
    }


    //bi-directional many-to-one association to PdpSrNotification
    @OneToMany(mappedBy = "serviceAgreement")
    @JsonBackReference
    public List<CustomerNotificationPreference> getCustomerNotificationPreferences() {
        return customerNotificationPreferences;
    }


    public void setCustomerNotificationPreferences(List<CustomerNotificationPreference> customerNotificationPreferences) {
        this.customerNotificationPreferences = customerNotificationPreferences;
    }



//    @OneToMany(mappedBy = "serviceAgreement")
//    @JsonBackReference
//    public List<ServiceAgreementAsset> getAssets() {
//        return assets;
//    }
//
//    public void setAssets(List<ServiceAgreementAsset> assets) {
//        this.assets = assets;
//    }




    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    public List<MeterForecast> getMeterForecasts() {
        return meterForecasts;
    }

    public void setMeterForecasts(List<MeterForecast> meterForecasts) {
        this.meterForecasts = meterForecasts;
    }

//    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonBackReference
//    public List<RatePlanEnrollment> getRatePlanEnrollments() {
//        return ratePlanEnrollments;
//    }
//
//    public void setRatePlanEnrollments(List<RatePlanEnrollment> ratePlanEnrollments) {
//        this.ratePlanEnrollments = ratePlanEnrollments;
//    }

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    public List<SecondaryContact> getSecondaryContacts() {
        return secondaryContacts;
    }

    public void setSecondaryContacts(List<SecondaryContact> secondaryContacts) {
        this.secondaryContacts = secondaryContacts;
    }

//    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference
//    public List<PortalUser> getPortalUsers() {
//        return portalUsers;
//    }

//    public void setPortalUsers(List<PortalUser> portalUsers) {
//        this.portalUsers = portalUsers;
//    }

}