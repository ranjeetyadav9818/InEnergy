package com.inenergis.service;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Meter;
import com.inenergis.entity.Premise;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.genericEnum.GeneralEligibilityAttributeType;
import com.inenergis.entity.program.GeneralAvailability;
import com.inenergis.entity.program.GeneralAvailabilityApplicableValue;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanDemand;
import com.inenergis.entity.program.RatePlanProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RatePlanEligibilityServiceTest {

    @Mock
    private RatePlanService ratePlanService;

    @InjectMocks
    private RatePlanEligibilityService ratePlanEligibilityService = new RatePlanEligibilityService();

    private ServiceAgreement serviceAgreement;
    private RatePlan ratePlan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Premise premise = new Premise();
        premise.setPremiseType("Premise Type II");

        Meter meter = new Meter();
        meter.setConfigType("Meter Type III");

        ServicePoint servicePoint = new ServicePoint();
        servicePoint.setPremise(premise);
        servicePoint.setMeter(meter);

        AgreementPointMap agreementPointMap = new AgreementPointMap();
        agreementPointMap.setServicePoint(servicePoint);

        serviceAgreement = new ServiceAgreement();
        serviceAgreement.addAgreementPointMap(agreementPointMap);
        serviceAgreement.setHas3rdPartyDrp("true");
        serviceAgreement.setCustClassCd("Customer Class CD IV");
        serviceAgreement.setCust_size("Customer Size V");

        GeneralAvailabilityApplicableValue generalAvailabilityApplicableValue1 = new GeneralAvailabilityApplicableValue();
        generalAvailabilityApplicableValue1.setValue("Premise Type II");
        GeneralAvailabilityApplicableValue generalAvailabilityApplicableValue2 = new GeneralAvailabilityApplicableValue();
        generalAvailabilityApplicableValue2.setValue("Meter Type III");
        GeneralAvailabilityApplicableValue generalAvailabilityApplicableValue3 = new GeneralAvailabilityApplicableValue();
        generalAvailabilityApplicableValue3.setValue("true");
        GeneralAvailabilityApplicableValue generalAvailabilityApplicableValue4 = new GeneralAvailabilityApplicableValue();
        generalAvailabilityApplicableValue4.setValue("Customer Class CD IV");
        GeneralAvailabilityApplicableValue generalAvailabilityApplicableValue5 = new GeneralAvailabilityApplicableValue();
        generalAvailabilityApplicableValue5.setValue("Customer Size V");

        GeneralAvailability generalAvailability = new GeneralAvailability();
        generalAvailability.setApplicableValues(Arrays.asList(
                generalAvailabilityApplicableValue1, generalAvailabilityApplicableValue2, generalAvailabilityApplicableValue3, generalAvailabilityApplicableValue4, generalAvailabilityApplicableValue5));
        generalAvailability.setAttributeType(GeneralEligibilityAttributeType.PREMISE_TYPE);

        RatePlanProfile ratePlanProfile = new RatePlanProfile();
        ratePlanProfile.setEffectiveStartDate(
                Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        ratePlanProfile.setEffectiveEndDate(
                Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));

        ratePlanProfile.setGeneralAvailabilities(Collections.singletonList(generalAvailability));
        ratePlanProfile.setRatePlanDemands(Collections.singletonList(new RatePlanDemand()));

        ratePlan = new RatePlan();
        ratePlan.addProfile(ratePlanProfile);

        Mockito.when(ratePlanService.getAll()).thenReturn(Arrays.asList(new RatePlan(), ratePlan));
    }

    @Test
    void isEligible() {
        assertTrue(ratePlanEligibilityService.checkEligibility(serviceAgreement).isEligible());
    }

    @Test
    void isEligibleWithRatePlan() {
        assertTrue(ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan).isEligible());
    }

    @Test
    void isEligibleWithRatePlan2() {
        ratePlan.getActiveProfile().getGeneralAvailabilities().get(0).setAttributeType(GeneralEligibilityAttributeType.MTR_CONFIG_TYPE);
        assertTrue(ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan).isEligible());
    }

    @Test
    void isEligibleWithRatePlan3() {
        ratePlan.getActiveProfile().getGeneralAvailabilities().get(0).setAttributeType(GeneralEligibilityAttributeType.HAS_3RD_PARTY_DRP);
        assertTrue(ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan).isEligible());
    }

    @Test
    void isEligibleWithRatePlan4() {
        ratePlan.getActiveProfile().getGeneralAvailabilities().get(0).setAttributeType(GeneralEligibilityAttributeType.CUST_SIZE);
        RatePlanEligibilityService.Eligibility eligibility = ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan);

        assertTrue(eligibility.isEligible());
    }

    @Test
    void isEligibleWithRatePlan5() {
        ratePlan.getActiveProfile().getGeneralAvailabilities().get(0).setAttributeType(GeneralEligibilityAttributeType.CUST_CLASS_CD);
        RatePlanEligibilityService.Eligibility eligibility = ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan);

        assertTrue(eligibility.isEligible());
    }

    @Test
    void isEligibleWithRatePlan_Fail() {
        GeneralAvailabilityApplicableValue value = new GeneralAvailabilityApplicableValue();
        value.setValue("Test to Fail");
        ratePlan.getActiveProfile().getGeneralAvailabilities().get(0).setApplicableValues(Collections.singletonList(value));
        RatePlanEligibilityService.Eligibility eligibility = ratePlanEligibilityService.checkEligibility(serviceAgreement, ratePlan);
        assertFalse(eligibility.isEligible());
        Assertions.assertEquals("Premise Type (Premise Type II)\n", eligibility.getIneligibleReasons());
    }
}