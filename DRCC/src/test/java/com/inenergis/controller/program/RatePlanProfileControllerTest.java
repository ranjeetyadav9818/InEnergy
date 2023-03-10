package com.inenergis.controller.program;

import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.program.ApplicableRatePlan;
import com.inenergis.entity.program.ChargesAttribute;
import com.inenergis.entity.program.ChargesFee;
import com.inenergis.entity.program.ChargesFeeComparison;
import com.inenergis.entity.program.CreditDiscount;
import com.inenergis.entity.program.CreditDiscountFee;
import com.inenergis.entity.program.CreditsDiscountsFeeComparison;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanDemand;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.RatePlanProfileAsset;
import com.inenergis.entity.program.rateProgram.RateCode;
import com.inenergis.entity.program.rateProgram.RateCodeProfile;
import com.inenergis.model.GeneralAvailabilityDualModel;
import com.inenergis.service.AssetService;
import com.inenergis.service.ChargesFeeService;
import com.inenergis.service.CreditDiscountFeeService;
import com.inenergis.service.HistoryService;
import com.inenergis.service.PercentageFeeHierarchyService;
import com.inenergis.service.PremiseService;
import com.inenergis.service.RateCodeProfileService;
import com.inenergis.service.RateCodeService;
import com.inenergis.service.RatePlanService;
import com.inenergis.util.UIMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;

import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class RatePlanProfileControllerTest {

    @Mock
    private RatePlanService ratePlanService;

    @Mock
    private UIMessage uiMessage;

    @Mock
    private Identity identity;

    @Mock
    private HistoryService historyService;

    @Mock
    private PremiseService premiseService;

    @InjectMocks
    private RatePlanController controller = new RatePlanController();

    private RatePlan ratePlan;

    @Mock
    private RateCodeService rateCodeService;

    @Mock
    private RateCodeProfileService rateCodeProfileService;

    @Mock
    private ChargesFeeService chargesFeeService;

    @Mock
    private CreditDiscountFeeService creditDiscountFeeService;

    @Mock
    private AssetService assetService;

    @Mock
    private PercentageFeeHierarchyService percentageFeeHierarchyService;

    @BeforeEach
    public void inject() {
        MockitoAnnotations.initMocks(this);
        ratePlan = new RatePlan();
        controller.setRatePlan(ratePlan);
        User user = Mockito.mock(User.class);
        Mockito.when(user.getEmail()).thenReturn("author");
        Mockito.when(identity.getAccount()).thenReturn(user);
        controller.setSelectedProfile(new RatePlanProfile());
        controller.getSelectedProfile().setSeasonCalendars(new ArrayList<>());
        controller.getSelectedProfile().setHolidayCalendars(new ArrayList<>());
        controller.getSelectedProfile().setTimeOfUseCalendarTODELETES(new ArrayList<>());
        controller.getSelectedProfile().setRatePlanDemands(new ArrayList<>());
        controller.getSelectedProfile().setGeneralAvailabilities(new ArrayList<>());
        controller.getSelectedProfile().setApplicableRatePlans(new ArrayList<>());
        controller.getSelectedProfile().setChargesAttributes(new ArrayList<>());
        controller.getSelectedProfile().setCreditDiscounts(new ArrayList<>());
    }

    @Test
    public void testStartProfileCreation() throws Exception {
        controller.addProfile(new RatePlan());
        assertNotNull(controller.getNewProfile());
        assertEquals(controller.getNewProfile().getRatePlan(), ratePlan);
    }

    @Test
    public void testSaveNewProfile() throws Exception {
        RatePlanProfile profile = new RatePlanProfile();
        profile.setName("name");
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.anyString())).thenReturn(profile);
        Mockito.when(ratePlanService.getProfileById(Mockito.anyLong())).thenReturn(profile);
        controller.setNewProfile(profile);
        controller.saveNewProfile(new RatePlan());
        Mockito.verify(uiMessage).addMessage("Profile {0} created", "name");
        assertNull(controller.getNewProfile());
    }

    @Test
    public void testEditProfile() throws Exception {
        RatePlanProfile profileOne = controller.getSelectedProfile();
        RatePlanProfile profileTwo = new RatePlanProfile();
        profileTwo.setApplicableRatePlans(Collections.singletonList(new ApplicableRatePlan()));
        profileTwo.setGeneralAvailabilities(new ArrayList<>());
        final Asset asset = new Asset();
        profileTwo.setRatePlanProfileAssets(Arrays.asList(new RatePlanProfileAsset(profileTwo, asset)));
        profileTwo.setRatePlan(new RatePlan());
        controller.editProfile(profileTwo);
        assertNotSame(profileOne, controller.getSelectedProfile());
        Mockito.verify(historyService).getHistory(profileTwo);
        Assertions.assertFalse(controller.getAssetsPickList().getSource().contains(asset));
        Assertions.assertTrue(controller.getAssetsPickList().getTarget().contains(asset));

    }

    private RatePlanProfile initRateCodeController() {
        RatePlanProfile profileTwo = new RatePlanProfile();
        profileTwo.setApplicableRatePlans(Collections.singletonList(new ApplicableRatePlan()));
        controller.setRateCodes(new ArrayList<>(Arrays.asList(new RateCode())));
        final RateCode rateCode = controller.getRateCodes().get(0);
        rateCode.setName("Test rate code");
        profileTwo.setRateCodeProfiles(new ArrayList<>(Arrays.asList(new RateCodeProfile())));
        final RateCodeProfile rateCodeProfile = profileTwo.getRateCodeProfiles().get(0);
        rateCodeProfile.setRatePlanProfile(profileTwo);
        rateCodeProfile.setRateCode(new RateCode());
        rateCodeProfile.getRateCode().setName("Test rate code 2");
        controller.setSelectedProfile(profileTwo);
        return profileTwo;
    }

    @Test
    public void testCancelProfile() {
        controller.setSelectedProfile(new RatePlanProfile());
        controller.cancelProfile();
        assertNull(controller.getSelectedProfile());
    }

    @Test
    public void testSaveAndClose() throws Exception {
        RatePlanProfile selectedProfile = new RatePlanProfile();
        selectedProfile.setName("test1");
        selectedProfile.setGeneralAvailabilities(new ArrayList<>());
        selectedProfile.setId(1L);
        selectedProfile.setChargesAttributes(new ArrayList<>());
        selectedProfile.setCreditDiscounts(new ArrayList<>());

        Mockito.when(ratePlanService.getProfileById(Mockito.anyLong())).thenReturn(selectedProfile);

        controller.setSelectedProfile(selectedProfile);
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.anyString())).thenReturn(selectedProfile);
        //controller.saveProfileAndClose();
        //Mockito.verify(ratePlanService).saveOrUpdateProfile(selectedProfile, "author");
        //assertNull(controller.getSelectedProfile());
        //Mockito.verify(uiMessage).addMessage("Profile {0} updated", "test1");
    }

    @Test
    public void saveProfileWithGeneralEligibilities() {
        ChargesAttribute ca = new ChargesAttribute();
        ChargesFeeComparison chargesFeeComparison = new ChargesFeeComparison();
        chargesFeeComparison.setChargesFeeOne(new ChargesFee());
        chargesFeeComparison.setChargesFeeTwo(new ChargesFee());
        ca.setChargesFeeComparisons(Collections.singletonList(chargesFeeComparison));
        controller.getSelectedProfile().getChargesAttributes().add(ca);
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.any())).thenReturn(controller.getSelectedProfile());
        controller.addGeneralAvailability();
        //controller.saveProfile();
        //Mockito.verify(ratePlanService).saveOrUpdateProfile(Mockito.any(), contains("author"));
    }

    @Test
    public void saveProfileWithInvalidFeeComparison() {
        ChargesAttribute ca = new ChargesAttribute();
        ChargesFeeComparison chargesFeeComparison = new ChargesFeeComparison();
        ChargesFee chargesFee = new ChargesFee();
        chargesFeeComparison.setChargesFeeOne(chargesFee);
        chargesFeeComparison.setChargesFeeTwo(chargesFee);
        ca.setChargesFeeComparisons(Collections.singletonList(chargesFeeComparison));
        controller.getSelectedProfile().getChargesAttributes().add(ca);
        controller.saveProfile();
        Mockito.verify(uiMessage).addMessage(Mockito.anyString(), Mockito.eq(FacesMessage.SEVERITY_ERROR));
    }

    @Test
    public void saveProfileWithValidCreditDiscountFeeComparison() {
        CreditDiscount creditDiscount = new CreditDiscount();
        CreditsDiscountsFeeComparison creditsDiscountsFeeComparison = new CreditsDiscountsFeeComparison();
        creditsDiscountsFeeComparison.setCreditDiscountFeeOne(new CreditDiscountFee());
        creditsDiscountsFeeComparison.setCreditDiscountFeeTwo(new CreditDiscountFee());
        creditDiscount.setCreditsDiscountsFeeComparisons(Collections.singletonList(creditsDiscountsFeeComparison));
        controller.getSelectedProfile().getCreditDiscounts().add(creditDiscount);
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.any())).thenReturn(controller.getSelectedProfile());
        controller.addGeneralAvailability();
        //controller.saveProfile();
//        Mockito.verify(ratePlanService).saveOrUpdateProfile(Mockito.any(), contains("author"));
    }

    @Test
    public void saveProfileWithInvalidCreditDiscountFeeComparison() {
        CreditDiscount creditDiscount = new CreditDiscount();
        CreditDiscountFee creditDiscountFee = new CreditDiscountFee();
        CreditsDiscountsFeeComparison creditsDiscountsFeeComparison = new CreditsDiscountsFeeComparison();
        creditsDiscountsFeeComparison.setCreditDiscountFeeOne(creditDiscountFee);
        creditsDiscountsFeeComparison.setCreditDiscountFeeTwo(creditDiscountFee);
        creditDiscount.setCreditsDiscountsFeeComparisons(Collections.singletonList(creditsDiscountsFeeComparison));
        controller.getSelectedProfile().getCreditDiscounts().add(creditDiscount);
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.any())).thenReturn(controller.getSelectedProfile());
        controller.saveProfile();
        Mockito.verify(uiMessage).addMessage(Mockito.anyString(), Mockito.eq(FacesMessage.SEVERITY_ERROR));
    }

    @Test
    public void testSaveAProfileWithError() throws Exception {
        RatePlanProfile selectedProfile = new RatePlanProfile();
        Mockito.doThrow(new NullPointerException()).when(ratePlanService).saveOrUpdateProfile(selectedProfile, "author");
        try {
            controller.setSelectedProfile(selectedProfile);
            controller.saveProfileAndClose();
            fail("it should throw an exception");
        } catch (NullPointerException e) {
            Mockito.verify(uiMessage, Mockito.times(0)).addMessage(Mockito.anyString(), Mockito.any());
        }
    }

    @Test
    public void testSaveProfileWithAsset() {
        final Asset asset = new Asset();
        asset.setName("asset");
        ArrayList assetArrayList = new ArrayList();
        assetArrayList.add(asset);
        Mockito.when(assetService.getAll()).thenReturn(assetArrayList);
        controller.doInit();
        controller.getSelectedProfile().setRatePlanProfileAssets(new ArrayList<>());
        final RatePlanProfileAsset ratePlanProfileAsset = new RatePlanProfileAsset(controller.getSelectedProfile(), asset);
        final List<RatePlanProfileAsset> ratePlanProfileAssets = new ArrayList();
        ratePlanProfileAssets.add(ratePlanProfileAsset);
        Mockito.when(ratePlanService.saveOrUpdateProfile(Mockito.any(), Mockito.any())).thenReturn(controller.getSelectedProfile());
        controller.getAssetsPickList().getTarget().add(asset);
        //controller.saveProfile();
//        Assertions.assertTrue(controller.getSelectedProfile().getRatePlanProfileAssets().contains(ratePlanProfileAsset));
    }


    @Test
    public void addDemand() {
        controller.addDemand();
        assertEquals(1, controller.getSelectedProfile().getRatePlanDemands().size());
    }

    @Test
    public void removeDemand() {
        controller.addDemand();
        RatePlanDemand ratePlanDemand = controller.getSelectedProfile().getRatePlanDemands().get(0);
        controller.removeDemand(ratePlanDemand);

        assertEquals(0, controller.getSelectedProfile().getRatePlanDemands().size());
    }

    @Test
    public void addFees() {
        ChargesAttribute chargesAttribute = new ChargesAttribute();
        controller.addFees(chargesAttribute);
        assertEquals(1, chargesAttribute.getFees().size());
    }

    @Test
    public void addFeeComparison() {
        ChargesAttribute chargesAttribute = new ChargesAttribute();
        controller.addFeeComparison(chargesAttribute);
        assertEquals(1, chargesAttribute.getChargesFeeComparisons().size());
    }

    @Test
    public void addCreditsDiscountsFeeComparison() {
        CreditDiscount creditDiscount = new CreditDiscount();
        controller.addCreditsDiscountsFeeComparison(creditDiscount);
        assertEquals(1, creditDiscount.getCreditsDiscountsFeeComparisons().size());
    }

    @Test
    public void removeFeeComparison() {
        ChargesAttribute chargesAttribute = new ChargesAttribute();
        ChargesFeeComparison chargesFeeComparison = new ChargesFeeComparison();
        chargesAttribute.setChargesFeeComparisons(new ArrayList<>());
        chargesAttribute.getChargesFeeComparisons().add(chargesFeeComparison);
        controller.removeFeeComparison(chargesAttribute, chargesFeeComparison);
        assertEquals(0, chargesAttribute.getChargesFeeComparisons().size());
    }

    @Test
    public void removeCreditsDiscountsFeeComparisons() {
        CreditDiscount creditDiscount = new CreditDiscount();
        CreditsDiscountsFeeComparison creditsDiscountsFeeComparison = new CreditsDiscountsFeeComparison();
        creditDiscount.setCreditsDiscountsFeeComparisons(new ArrayList<>());
        creditDiscount.getCreditsDiscountsFeeComparisons().add(creditsDiscountsFeeComparison);
        controller.removeCreditsDiscountsFeeComparisons(creditDiscount, creditsDiscountsFeeComparison);
        assertEquals(0, creditDiscount.getCreditsDiscountsFeeComparisons().size());
    }

    @Test
    public void removeChargesAttribute() {
        ChargesAttribute chargesAttribute = new ChargesAttribute();
        controller.getSelectedProfile().setChargesAttributes(new ArrayList<>());
        controller.getSelectedProfile().getChargesAttributes().add(chargesAttribute);
        assertEquals(1, controller.getSelectedProfile().getChargesAttributes().size());
        controller.removeChargesAttribute(chargesAttribute);
        assertEquals(0, controller.getSelectedProfile().getChargesAttributes().size());
    }

    @Test
    public void removeFee() {
        ChargesFee fee = new ChargesFee();
        ChargesAttribute chargesAttribute = new ChargesAttribute();
        chargesAttribute.setFees(new ArrayList<>());
        chargesAttribute.getFees().add(fee);
        assertEquals(1, chargesAttribute.getFees().size());
        controller.removeFee(chargesAttribute, fee);
        assertEquals(0, chargesAttribute.getFees().size());
    }

    @Test
    public void addCreditDiscount() {
        controller.addCreditDiscount();
        assertEquals(1, controller.getSelectedProfile().getCreditDiscounts().size());
    }

    @Test
    public void removeCreditDiscount() {
        CreditDiscount creditDiscount = new CreditDiscount();
        controller.getSelectedProfile().setCreditDiscounts(new ArrayList<>());
        controller.getSelectedProfile().getCreditDiscounts().add(creditDiscount);
        assertEquals(1, controller.getSelectedProfile().getCreditDiscounts().size());
        controller.removeCreditDiscount(creditDiscount);
        assertEquals(0, controller.getSelectedProfile().getCreditDiscounts().size());
    }

    @Test
    public void addDiscountFees() {
        CreditDiscount creditDiscount = new CreditDiscount();
        creditDiscount.setFees(new ArrayList<>());
        controller.addDiscountFees(creditDiscount);
        assertEquals(1, creditDiscount.getFees().size());
    }

    @Test
    public void removeDiscountFee() {
        CreditDiscount creditDiscount = new CreditDiscount();
        CreditDiscountFee creditDiscountFee = new CreditDiscountFee();
        creditDiscount.setFees(new ArrayList<>());
        creditDiscount.getFees().add(creditDiscountFee);
        assertEquals(1, creditDiscount.getFees().size());
        controller.removeDiscountFee(creditDiscount, creditDiscountFee);
        assertEquals(0, creditDiscount.getFees().size());
    }

    @Test
    public void removeNonExistingDemand() {
        controller.removeDemand(new RatePlanDemand());

        assertEquals(0, controller.getSelectedProfile().getRatePlanDemands().size());
    }

    @Test
    public void ratePlanNotNull() {
        assertNotNull(controller.getRatePlan());
    }

    @Test
    public void addGeneralAvailability() {
        Mockito.when(premiseService.getDistinctPremiseTypeValues()).thenReturn(new ArrayList<>());

        controller.addGeneralAvailability();
        assertEquals(1, controller.getSelectedProfile().getGeneralAvailabilities().size());
    }

    @Test
    public void removeGeneralAvailability() {
        Mockito.when(premiseService.getDistinctPremiseTypeValues()).thenReturn(new ArrayList<>());
        controller.addGeneralAvailability();
        GeneralAvailabilityDualModel dualModel = controller.getGeneralEligibility().get(0);
        controller.removeGeneralAvailability(dualModel);

        assertEquals(0, controller.getSelectedProfile().getGeneralAvailabilities().size());
    }

    @Test
    public void clearRateCodeProfiles() {
        RatePlanProfile profile = initRateCodeController();
        final RateCodeProfile rateCodeProfile = profile.getRateCodeProfiles().get(0);
        controller.clearRateCodeProfiles();

        assertEquals(0, controller.getRateCodes().size());
        assertEquals(0, controller.getSelectedProfile().getRateCodeProfiles().size());
    }

    @Test
    public void removeRateCodeProfile() {
        RatePlanProfile profile = initRateCodeController();
        final RateCodeProfile rateCodeProfile = profile.getRateCodeProfiles().get(0);
        controller.removeRateCodeProfile(rateCodeProfile);

        assertEquals(2, controller.getRateCodes().size());
    }

    @Test
    public void doDrop() {
        final RatePlanProfile ratePlanProfile = initRateCodeController();
        final List<RateCodeProfile> rateCodeProfiles = ratePlanProfile.getRateCodeProfiles();
        final int before = rateCodeProfiles.size();

        RateCode rateCode = new RateCode();
        rateCode.setName("R1");
        controller.doDrop(rateCode);
        final int after = rateCodeProfiles.size();

        assertNotEquals(after, before);
    }

    @Test
    public void addChargesAttribute() {
        controller.getSelectedProfile().setChargesAttributes(new ArrayList<>());
        controller.addChargesAttribute();
        assertEquals(1, controller.getSelectedProfile().getChargesAttributes().size());
    }

    @Test
    public void viewProfile() {
        controller.setSelectedProfile(null);
        controller.setViewModeOn(false);

        RatePlanProfile ratePlanProfile = new RatePlanProfile();
        ratePlanProfile.setApplicableRatePlans(new ArrayList<>());
        ratePlanProfile.setGeneralAvailabilities(new ArrayList<>());
        ratePlanProfile.setRatePlanProfileAssets(new ArrayList<>());
        ratePlanProfile.setRatePlan(new RatePlan());
        controller.viewProfile(ratePlanProfile);

        assertNotNull(controller.getSelectedProfile());
        assertTrue(controller.isViewModeOn());
    }

}
