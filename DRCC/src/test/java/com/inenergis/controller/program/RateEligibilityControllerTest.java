package com.inenergis.controller.program;

import com.inenergis.controller.model.RatePlanEligibility;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.RatePlanStatus;
import com.inenergis.entity.program.ApplicableRatePlan;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanEnrollment;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.service.RatePlanEligibilityService;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.faces.application.FacesMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

public class RateEligibilityControllerTest {

    @Mock
    private ServiceAgreementService serviceAgreementService;

    @Mock
    private RatePlanService ratePlanService;

    @Mock
    private RatePlanEligibilityService ratePlanEligibilityService;

    @Mock
    private UIMessage uiMessage;

    @InjectMocks
    private RateEligibilityController controller = new RateEligibilityController();

    @BeforeEach
    public void inject() {
        MockitoAnnotations.initMocks(this);
        RatePlanEligibilityService.Eligibility eligibility = ratePlanEligibilityService.new Eligibility();
        eligibility.setEligible(true);
        Mockito.when(ratePlanEligibilityService.checkEligibility(any(), any())).thenReturn(eligibility);
    }


    @Test
    public void testSearchWithoutSA() throws Exception {
        controller.search();
        Mockito.verify(uiMessage).addMessage(anyString(), eq(FacesMessage.SEVERITY_ERROR), isNull());
    }

    @Test
    public void testGenerateRatePlansThroughSearch() throws Exception {
        controller.setSaId("1");
        Mockito.when(serviceAgreementService.getById("1")).thenReturn(new ServiceAgreement());
        RatePlan ratePlan = getActiveRatePlan();
        Mockito.when(ratePlanService.getAll()).thenReturn(Arrays.asList(ratePlan));
        controller.search();
        Assertions.assertEquals(ratePlan, controller.getRatePlans().get(0).getRatePlan());
    }

    @Test
    public void testEnroll() throws Exception {
        RatePlanEligibility eligibility = new RatePlanEligibility();
        RatePlan activeRatePlan = getActiveRatePlan();
        eligibility.setRatePlan(activeRatePlan);
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        controller.setServiceAgreement(serviceAgreement);
        controller.enroll(eligibility);
        Mockito.verify(ratePlanService).enroll(activeRatePlan,serviceAgreement);
    }

    @Test
    public void testUnenrollSeveralMatches() throws Exception {
        RatePlanEligibility eligibility = new RatePlanEligibility();
        RatePlan activeRatePlan = getActiveRatePlan();
        eligibility.setRatePlan(activeRatePlan);
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        RatePlanEnrollment enrollment = new RatePlanEnrollment();
        enrollment.setStatus(RatePlanEnrollment.ENROLLED);
        enrollment.setRatePlan(activeRatePlan);
        serviceAgreement.setRatePlanEnrollments(Arrays.asList(enrollment));
        controller.setServiceAgreement(serviceAgreement);
        Mockito.when(ratePlanService.findActiveRatePlanEnrollmentInServiceAgreement(serviceAgreement,activeRatePlan)).thenReturn(enrollment);
        controller.unenroll(eligibility);
        Assertions.assertTrue(controller.isShowModal());
    }

    @Test
    public void testUnenrollOneMatch() throws Exception {
        RatePlanEligibility eligibility = new RatePlanEligibility();
        RatePlan activeRatePlan = getActiveRatePlan();
        eligibility.setRatePlan(activeRatePlan);
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        RatePlanEnrollment enrollment = generateRatePlanEnrollment();
        enrollment.getRatePlan().getActiveProfile().setApplicableRatePlans(new ArrayList<>());
        serviceAgreement.setRatePlanEnrollments(Arrays.asList(enrollment));
        controller.setServiceAgreement(serviceAgreement);
        Mockito.when(ratePlanService.findActiveRatePlanEnrollmentInServiceAgreement(serviceAgreement,activeRatePlan)).thenReturn(enrollment);
        controller.unenroll(eligibility);
        Mockito.verify(ratePlanService).unenroll(enrollment);
    }


    @Test
    public void testUnenrollOne() throws Exception {
        RatePlanEnrollment enrollment = generateRatePlanEnrollment();
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        controller.setServiceAgreement(serviceAgreement);
        controller.setTempEnrollment(enrollment);
        controller.unenrollOne();
        Mockito.verify(ratePlanService).unenroll(enrollment);
    }

    public RatePlanEnrollment generateRatePlanEnrollment() {
        RatePlanEnrollment enrollment = new RatePlanEnrollment();
        enrollment.setStatus(RatePlanEnrollment.ENROLLED);
        enrollment.setRatePlan(getActiveRatePlan());
        return enrollment;
    }


    @Test
    public void testUnenrollAll() throws Exception {
        RatePlanEnrollment enrollment = generateRatePlanEnrollment();
        controller.setTempEnrollment(enrollment);
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        serviceAgreement.setRatePlanEnrollments(Arrays.asList(enrollment));
        controller.setServiceAgreement(serviceAgreement);
        controller.unenrollAll();
        Mockito.verify(ratePlanService).unenrollAndRelated(enrollment,serviceAgreement);
    }

    @Test
    public void testCancelUnenrollment() throws Exception {
        controller.setSaId("");
        controller.setServiceAgreement(new ServiceAgreement());
        controller.clear();
        Assertions.assertNull(controller.getSaId());
        Assertions.assertNull(controller.getServiceAgreement());
    }

    @Test
    public void testClear() throws Exception {
        controller.setTempEnrollment(new RatePlanEnrollment());
        controller.setShowModal(true);
        controller.cancelUnenrollment();
        Assertions.assertFalse(controller.isShowModal());
        Assertions.assertNull(controller.getTempEnrollment());
    }

    private RatePlan getActiveRatePlan() {
        RatePlan ratePlan = new RatePlan();
        ratePlan.setStatus(RatePlanStatus.ACTIVE);
        RatePlanProfile profile = new RatePlanProfile();
        ApplicableRatePlan applicableRatePlan = new ApplicableRatePlan();
        applicableRatePlan.setRatePlan(ratePlan);
        profile.setApplicableRatePlans(Arrays.asList(applicableRatePlan));
        ratePlan.setProfiles(Arrays.asList(profile));
        ratePlan.getProfiles().get(0).setActive(true);
        ratePlan.getProfiles().get(0).setEffectiveStartDate(new Date(LocalDateTime.of(2017, 1, 1, 0, 0).toLocalDate().toEpochDay()));
        return ratePlan;
    }

}