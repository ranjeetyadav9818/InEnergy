package com.inenergis.controller.program;

import com.google.common.base.Strings;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.entity.History;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.*;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.program.CDWAttribute;
import com.inenergis.entity.program.CreditDiscount;
import com.inenergis.entity.program.CreditDiscountFee;
import com.inenergis.entity.program.CreditsDiscountsFeeComparison;
import com.inenergis.entity.program.DRMSProgramMapping;
import com.inenergis.entity.program.EligibleProgram;
import com.inenergis.entity.program.EnrollerSource;
import com.inenergis.entity.program.EnrollmentAttribute;
import com.inenergis.entity.program.NotificationDuplicationSource;
import com.inenergis.entity.program.PremiseType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramCustomerDataAttributeChanges;
import com.inenergis.entity.program.ProgramCustomerNotification;
import com.inenergis.entity.program.ProgramDefinedDispatchLevel;
import com.inenergis.entity.program.ProgramDefinedDispatchReason;
import com.inenergis.entity.program.ProgramDemand;
import com.inenergis.entity.program.ProgramEligibilityCustomerType;
import com.inenergis.entity.program.ProgramEligibilityPremiseType;
import com.inenergis.entity.program.ProgramEligibilityRateSchedule;
import com.inenergis.entity.program.ProgramEligibilitySaStatus;
import com.inenergis.entity.program.ProgramEnroller;
import com.inenergis.entity.program.ProgramEnrollmentAttribute;
import com.inenergis.entity.program.ProgramEnrollmentNotification;
import com.inenergis.entity.program.ProgramEquipment;
import com.inenergis.entity.program.ProgramEssentialCustomer;
import com.inenergis.entity.program.ProgramEventDuration;
import com.inenergis.entity.program.ProgramFirmServiceLevelRule;
import com.inenergis.entity.program.ProgramHoliday;
import com.inenergis.entity.program.ProgramMultipleParticipation;
import com.inenergis.entity.program.ProgramMultipleParticipationProgram;
import com.inenergis.entity.program.ProgramOption;
import com.inenergis.entity.program.ProgramProduct;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramProfileAsset;
import com.inenergis.entity.program.ProgramSeason;
import com.inenergis.entity.program.RateSchedule;
import com.inenergis.entity.program.SublapProgramMapping;
import com.inenergis.service.AssetProfileService;
import com.inenergis.service.AssetService;
import com.inenergis.service.CreditDiscountFeeService;
import com.inenergis.service.HistoryService;
import com.inenergis.service.IsoProductService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class ProgramProfileController implements Serializable {

    @Inject
    ProgramServiceContract programService;
    @Inject
    HistoryService historyService;
    @Inject
    IsoProductService isoProductService;
    @Inject
    AssetService assetService;
    @Inject
    SubLapService subLapService;
    @Inject
    UIMessage uiMessage;
    @Inject
    Identity identity;
    @Inject
    private CreditDiscountFeeService creditDiscountFeeService;
    @Inject
    private AssetProfileService assetProfileService;
    @Inject
    private NetworkTypeService networkTypeService;

    Logger log = LoggerFactory.getLogger(ProgramProfileController.class);

    private Program program;
    private List<Program> availablePrograms;
    private ProgramProfile profile;
    private List<History> historyList;
    private boolean newProfile = false;

    private DualListModel<EnrollerSource> enrollerSources = new DualListModel<>();
    private DualListModel<EnrollmentAttribute> enrollmentAttributes = new DualListModel<>();
    private DualListModel<CDWAttribute> cdwAttributes = new DualListModel<>();
    private DualListModel<ServiceAgreement.SaStatus> saStatusPickList = new DualListModel<>();
    private DualListModel<ProgramEligibilityCustomerType.CustomerType> eligibilityCustomerTypePickList = new DualListModel<>();
    private DualListModel<RateSchedule> rateSchedulesPickList = new DualListModel<>();
    private DualListModel<PremiseType> premiseTypesPickList = new DualListModel<>();
    private DualListModel<DispatchLevel> dispatchLevelPickList = new DualListModel<>();
    private DualListModel<DispatchReason> dispatchReasonPickList = new DualListModel<>();
    private Map<String, DualListModel<Program>> programPickListModel = new HashMap<>();
    private DualListModel<Program> programPickListModelForWholesale = new DualListModel<>();
    private List<IsoProduct> isoProducts;
    private List<SubLap> subLaps;

    private List<String> meterTypes = new ArrayList<>();

    private List<EssentialCustomerType> essentialCustomers;

    private boolean marketEligibleChanged = false;
    private boolean participationActiveChanged = false;

    private boolean viewModeOn = false;
    private List<CreditDiscountFee> creditDiscountFees = new ArrayList<>();
    private List<NetworkType> networkTypes;
    private List<NetworkType> gasNetworkTypes;
    private List<NetworkType> eleNetworkTypes;





    @PostConstruct
    public void init() {
        final Long programId = Long.valueOf(ParameterEncoderService.getDefaultDecodedParameterAsLong());
        doInit(programId);
    }

    public void doInit(Long programId) {
        program = programService.getProgram(programId);
        availablePrograms = programService.getPrograms();
        isoProducts = isoProductService.getAll();
        subLaps = subLapService.getAll();
        networkTypes = networkTypeService.getAll();
        gasNetworkTypes =networkTypeService.getAllByCom(ComodityType.Gas);
        eleNetworkTypes= networkTypeService.getAllByCom(ComodityType.Electricity);
        creditDiscountFees = creditDiscountFeeService.getAllComparisonEligible();
        initPickLists();

    }

    private void initSafetyReductionFactors() {
        programService.initSafetyReductionFactors(profile);
        System.out.println("line 175");
    }

    private void initPickLists() {
        enrollerSources.setSource(new ArrayList<>(Arrays.asList(EnrollerSource.values())));
        enrollerSources.setTarget(new ArrayList<>());

        enrollmentAttributes.setSource(new ArrayList<>(Arrays.asList(EnrollmentAttribute.values())));
        enrollmentAttributes.setTarget(new ArrayList<>());

        cdwAttributes.setSource(new ArrayList<>(Arrays.asList(CDWAttribute.values())));
        cdwAttributes.setTarget(new ArrayList<>());

        saStatusPickList.setSource(new ArrayList<>(Arrays.asList(ServiceAgreement.SaStatus.values())));
        saStatusPickList.setTarget(new ArrayList<>());

        eligibilityCustomerTypePickList.setSource(new ArrayList<>(Arrays.asList(ProgramEligibilityCustomerType.CustomerType.values())));
        eligibilityCustomerTypePickList.setTarget(new ArrayList<>());

        rateSchedulesPickList.setSource(new ArrayList<>(Arrays.asList(RateSchedule.values())));
        rateSchedulesPickList.setTarget(new ArrayList<>());

        premiseTypesPickList.setSource(new ArrayList<>(Arrays.asList(PremiseType.values())));
        premiseTypesPickList.setTarget(new ArrayList<>());

        dispatchLevelPickList.setSource(new ArrayList<>(Arrays.asList(DispatchLevel.values())));
        dispatchLevelPickList.setTarget(new ArrayList<>());

        dispatchReasonPickList.setSource(new ArrayList<>(Arrays.asList(DispatchReason.values())));
        dispatchReasonPickList.setTarget(new ArrayList<>());

        programPickListModelForWholesale.setSource(new ArrayList<>(availablePrograms));
        programPickListModelForWholesale.setTarget(new ArrayList<>());

        essentialCustomers = new ArrayList<>();
    }

    public void saveProfile() {
        if (profile.getId() != null) {//we just want to save these 3 fields, if we want to save it all saveWholeProfile would have been called
            ProgramProfile profileFromDB = programService.getProfile(profile.getId());
            profileFromDB.setName(profile.getName());
            profileFromDB.setEffectiveStartDate(profile.getEffectiveStartDate());
            profileFromDB.setEffectiveEndDate(profile.getEffectiveEndDate());
            profile = profileFromDB;
        }
        saveProfileAndClear();
    }

    public void saveWholeProfile() {
        if (!program.isCapNumberValid()) {
            uiMessage.addMessage("Cap number must be an integer", FacesMessage.SEVERITY_ERROR);
            return;
        }
        List<ProgramDemand> programDemands = profile.getProgramDemands();
        if (programDemands != null) {
            for (ProgramDemand programDemand : programDemands) {
                if (programDemand.getPriorMonths() < programDemand.getTimeHorizon()) {
                    uiMessage.addMessage("Time Horizon must be less than Prior Month", FacesMessage.SEVERITY_ERROR);
                    return;
                }
            }
        }
        final String duplicates = checkRepeatedValues(profile.getProgramProfileAssets());
        if (StringUtils.isNotEmpty(duplicates)){
            uiMessage.addMessage("Can not be repeated assets on Equipment List: {0} ", FacesMessage.SEVERITY_ERROR,duplicates);
            return;
        }
        /* Pick lists being saved */
        syncPickLists();
        /* Other collections being saved */
        if (profile.getEnrollmentNotifications() != null) {
            for (ProgramEnrollmentNotification programEnrollmentNotification : profile.getEnrollmentNotifications()) {
                programEnrollmentNotification.assignEnrollers();
            }
        }
        final Iterator<ProgramEquipment> equipmentIterator = profile.getEquipments().iterator();
        while (equipmentIterator.hasNext()) {
            if (!meterTypes.contains(equipmentIterator.next().getMeterType())) {
                equipmentIterator.remove();
            }
        }
        for (String meterType : meterTypes) {
            ProgramEquipment equipment = new ProgramEquipment();
            equipment.setMeterType(meterType);
            if (!profile.getEquipments().contains(equipment)) {
                equipment.setProfile(profile);
                profile.getEquipments().add(equipment);
            }
        }
        for (ProgramMultipleParticipation multipleParticipation : profile.getProgramMultiParticipations()) {
            if (!Strings.isNullOrEmpty(multipleParticipation.getOptionId())) {
                boolean optionAssigned = false;
                for (ProgramOption programOption : profile.getOptions()) {
                    if (programOption.getUuid().equals(multipleParticipation.getOptionId())) {
                        multipleParticipation.setOption(programOption);
                        optionAssigned = true;
                        break;
                    }
                }
                if (!optionAssigned) {
                    multipleParticipation.setOption(null);
                }
            } else {
                multipleParticipation.setOption(null);
            }
        }
        if (profile.getFslRules() != null) {
            for (ProgramFirmServiceLevelRule rule : profile.getFslRules()) {
                if (rule.getSeasonUuid() != null) {
                    rule.setSeason(findSeasonByUUID(rule.getSeasonUuid()));
                }
            }
        }
        if (!this.marketEligibleChanged && profile.isWholesaleMarketEligible()) {
            profile.setWholesaleMarketEligibleTs(new Date());
        }
        if (!this.participationActiveChanged && profile.isWholesaleParticipationActive()) {
            profile.setWholesaleParticipationActiveTs(new Date());
        }
        removeEmptyObjects(profile);
        creditDiscountFees = creditDiscountFeeService.getAllComparisonEligible();
        saveProfileAndClear();
    }

    public String checkRepeatedValues(List<ProgramProfileAsset> programProfileAssets) {
        StringJoiner strDuplicates =  new StringJoiner(",");
        Set<ProgramProfileAsset> assetsSet =  new HashSet<>();
        final Set<ProgramProfileAsset> duplicates = programProfileAssets.stream().filter(p -> ! assetsSet.add(p)).collect(Collectors.toSet());
        duplicates.stream().forEach(d -> strDuplicates.add(d.getAsset().getName()));
        return strDuplicates.toString();
    }

    public void syncPickLists() {
        for (EnrollerSource enrollerSource : enrollerSources.getSource()) {
            ProgramEnroller enrollerFromEnum = new ProgramEnroller();
            enrollerFromEnum.setName(enrollerSource.name());
            if (profile.getEnrollers().contains(enrollerFromEnum)) {
                profile.getEnrollers().remove(enrollerFromEnum);
            }
        }
        for (EnrollerSource enrollerSource : enrollerSources.getTarget()) {
            ProgramEnroller enrollerFromEnum = new ProgramEnroller();
            enrollerFromEnum.setName(enrollerSource.name());
            if (!profile.getEnrollers().contains(enrollerFromEnum)) {
                enrollerFromEnum.setProfile(profile);
                profile.getEnrollers().add(enrollerFromEnum);
            }
        }

        for (EnrollmentAttribute attribute : enrollmentAttributes.getSource()) {
            ProgramEnrollmentAttribute attributeFromEnum = new ProgramEnrollmentAttribute();
            attributeFromEnum.setName(attribute.name());
            if (profile.getEnrollmentAttributes().contains(attributeFromEnum)) {
                profile.getEnrollmentAttributes().remove(attributeFromEnum);
            }
        }
        for (EnrollmentAttribute attribute : enrollmentAttributes.getTarget()) {
            ProgramEnrollmentAttribute attributeFromEnum = new ProgramEnrollmentAttribute();
            attributeFromEnum.setName(attribute.name());
            if (!profile.getEnrollmentAttributes().contains(attributeFromEnum)) {
                attributeFromEnum.setProfile(profile);
                profile.getEnrollmentAttributes().add(attributeFromEnum);
            }
        }

        for (CDWAttribute attribute : cdwAttributes.getSource()) {
            ProgramCustomerDataAttributeChanges attributeFromEnum = new ProgramCustomerDataAttributeChanges();
            attributeFromEnum.setName(attribute.name());
            if (profile.getCustomerDataAttributeChanges().contains(attributeFromEnum)) {
                profile.getCustomerDataAttributeChanges().remove(attributeFromEnum);
            }
        }
        for (CDWAttribute attribute : cdwAttributes.getTarget()) {
            ProgramCustomerDataAttributeChanges attributeFromEnum = new ProgramCustomerDataAttributeChanges();
            attributeFromEnum.setName(attribute.name());
            if (!profile.getCustomerDataAttributeChanges().contains(attributeFromEnum)) {
                attributeFromEnum.setProfile(profile);
                profile.getCustomerDataAttributeChanges().add(attributeFromEnum);
            }
        }

        for (ServiceAgreement.SaStatus saStatusSource : saStatusPickList.getSource()) {
            ProgramEligibilitySaStatus statusFromEnum = new ProgramEligibilitySaStatus();
            statusFromEnum.setStatus(saStatusSource.getValue());
            if (profile.getSaStatuses().contains(statusFromEnum)) {
                profile.getSaStatuses().remove(statusFromEnum);
            }
        }
        for (ServiceAgreement.SaStatus saStatusTarget : saStatusPickList.getTarget()) {
            ProgramEligibilitySaStatus statusFromEnum = new ProgramEligibilitySaStatus();
            statusFromEnum.setStatus(saStatusTarget.getValue());
            if (!profile.getSaStatuses().contains(statusFromEnum)) {
                statusFromEnum.setProfile(profile);
                profile.getSaStatuses().add(statusFromEnum);
            }
        }

        for (ProgramEligibilityCustomerType.CustomerType customerType : eligibilityCustomerTypePickList.getSource()) {
            ProgramEligibilityCustomerType eligCustomerTypeFromEum = new ProgramEligibilityCustomerType();
            eligCustomerTypeFromEum.setCustomerType(customerType);
            if (profile.getCustomerTypes().contains(eligCustomerTypeFromEum)) {
                profile.getCustomerTypes().remove(eligCustomerTypeFromEum);
            }
        }
        for (ProgramEligibilityCustomerType.CustomerType customerType : eligibilityCustomerTypePickList.getTarget()) {
            ProgramEligibilityCustomerType eligCustomerTypeFromEum = new ProgramEligibilityCustomerType();
            eligCustomerTypeFromEum.setCustomerType(customerType);
            if (!profile.getCustomerTypes().contains(eligCustomerTypeFromEum)) {
                eligCustomerTypeFromEum.setProfile(profile);
                profile.getCustomerTypes().add(eligCustomerTypeFromEum);
            }
        }

        for (RateSchedule rateScheduleSource : rateSchedulesPickList.getSource()) {
            ProgramEligibilityRateSchedule rateScheduleFromEnum = new ProgramEligibilityRateSchedule();
            rateScheduleFromEnum.setRateSchedule(rateScheduleSource.name());
            if (profile.getRateSchedules().contains(rateScheduleFromEnum)) {
                profile.getRateSchedules().remove(rateScheduleFromEnum);
            }
        }
        for (RateSchedule rateScheduleTarget : rateSchedulesPickList.getTarget()) {
            ProgramEligibilityRateSchedule rateScheduleFromEnum = new ProgramEligibilityRateSchedule();
            rateScheduleFromEnum.setRateSchedule(rateScheduleTarget.name());
            if (!profile.getRateSchedules().contains(rateScheduleFromEnum)) {
                rateScheduleFromEnum.setProfile(profile);
                profile.getRateSchedules().add(rateScheduleFromEnum);
            }
        }

        for (PremiseType premiseTypeSource : premiseTypesPickList.getSource()) {
            ProgramEligibilityPremiseType premiseTypeFromEnum = new ProgramEligibilityPremiseType();
            premiseTypeFromEnum.setPremiseType(premiseTypeSource.name());
            if (profile.getPremiseTypes().contains(premiseTypeFromEnum)) {
                profile.getPremiseTypes().remove(premiseTypeFromEnum);
            }
        }
        for (PremiseType premiseTypeTarget : premiseTypesPickList.getTarget()) {
            ProgramEligibilityPremiseType premiseTypeFromEnum = new ProgramEligibilityPremiseType();
            premiseTypeFromEnum.setPremiseType(premiseTypeTarget.name());
            if (!profile.getPremiseTypes().contains(premiseTypeFromEnum)) {
                premiseTypeFromEnum.setProfile(profile);
                profile.getPremiseTypes().add(premiseTypeFromEnum);
            }
        }

        for (DispatchLevel dispatchLevelSource : dispatchLevelPickList.getSource()) {
            ProgramDefinedDispatchLevel dispatchLevel = new ProgramDefinedDispatchLevel();
            dispatchLevel.setDispatchLevel(dispatchLevelSource);
            if (profile.getDispatchLevels().contains(dispatchLevel)) {
                profile.getDispatchLevels().remove(dispatchLevel);
            }
        }
        for (DispatchLevel dispatchLevelTarget : dispatchLevelPickList.getTarget()) {
            ProgramDefinedDispatchLevel dispatchLevel = new ProgramDefinedDispatchLevel();
            dispatchLevel.setDispatchLevel(dispatchLevelTarget);
            if (!profile.getDispatchLevels().contains(dispatchLevel)) {
                dispatchLevel.setProfile(profile);
                profile.getDispatchLevels().add(dispatchLevel);
            }
        }

        for (DispatchReason dispatchReasonSource : dispatchReasonPickList.getSource()) {
            ProgramDefinedDispatchReason dispatchReason = new ProgramDefinedDispatchReason();
            dispatchReason.setDispatchReason(dispatchReasonSource);
            if (profile.getDispatchReasons().contains(dispatchReason)) {
                profile.getDispatchReasons().remove(dispatchReason);
            }
        }
        for (DispatchReason dispatchReasonTarget : dispatchReasonPickList.getTarget()) {
            ProgramDefinedDispatchReason dispatchReason = new ProgramDefinedDispatchReason();
            dispatchReason.setDispatchReason(dispatchReasonTarget);
            if (!profile.getDispatchReasons().contains(dispatchReason)) {
                dispatchReason.setProfile(profile);
                profile.getDispatchReasons().add(dispatchReason);
            }
        }

        profile.setEssentialCustomers(new ArrayList<>());
        for (EssentialCustomerType essentialCustomerType : essentialCustomers) {
            ProgramEssentialCustomer programEssentialCustomer = new ProgramEssentialCustomer();
            programEssentialCustomer.setProfile(profile);
            programEssentialCustomer.setEssentialCustomerType(essentialCustomerType);
            profile.getEssentialCustomers().add(programEssentialCustomer);
        }

        for (Program programForWholeSale : programPickListModelForWholesale.getSource()) {
            EligibleProgram eligibleProgramForSearch = new EligibleProgram();
            eligibleProgramForSearch.setProfile(profile);
            eligibleProgramForSearch.setProgram(programForWholeSale);
            if (profile.getEligiblePrograms().contains(eligibleProgramForSearch)) {
                profile.getEligiblePrograms().remove(eligibleProgramForSearch);
            }
        }
        for (Program programForWholeSale : programPickListModelForWholesale.getTarget()) {
            EligibleProgram eligibleProgramForSearch = new EligibleProgram();
            eligibleProgramForSearch.setProgram(programForWholeSale);
            eligibleProgramForSearch.setProfile(profile);
            if (!profile.getEligiblePrograms().contains(eligibleProgramForSearch)) {
                profile.getEligiblePrograms().add(eligibleProgramForSearch);
            }
        }

        for (ProgramMultipleParticipation multipleParticipation : profile.getProgramMultiParticipations()) {
            final DualListModel<Program> programDualListModel = programPickListModel.get(multipleParticipation.getUuid());
            for (Program programSource : programDualListModel.getSource()) {
                final Iterator<ProgramMultipleParticipationProgram> iterator = multipleParticipation.getPrograms().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getProgram().getId().equals(programSource.getId())) {
                        iterator.remove();
                    }
                }
            }
            for (Program programTarget : programDualListModel.getTarget()) {
                boolean programAlreadyAssigned = false;
                final Iterator<ProgramMultipleParticipationProgram> iterator = multipleParticipation.getPrograms().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getProgram().getId().equals(programTarget.getId())) {
                        programAlreadyAssigned = true;
                    }
                }
                if (!programAlreadyAssigned) {
                    final ProgramMultipleParticipationProgram newParticipationProgram = new ProgramMultipleParticipationProgram();
                    newParticipationProgram.setProgram(programTarget);
                    newParticipationProgram.setParticipation(multipleParticipation);
                    multipleParticipation.getPrograms().add(newParticipationProgram);
                }
            }
        }
    }

    /**
     * This method remove the objects not filled in by the user. Is used for de UI operations that add new child objects to Profile. For example the options
     * TODO delete those methods and the booolean method notFilledIn() created on some entities if a delete option is added to the UI
     *
     * @param profile
     */
    private void removeEmptyObjects(ProgramProfile profile) {
        Collection emptyObjectsToRemoveL1 = new ArrayList();
        boolean removeChilds = true;
        if (profile.getOptions() != null) {
            for (ProgramOption programOption : profile.getOptions()) {
                if (programOption.notFilledIn(removeChilds)) {
                    emptyObjectsToRemoveL1.add(programOption);
                }
            }
            profile.getOptions().removeAll(emptyObjectsToRemoveL1);
            emptyObjectsToRemoveL1.clear();
        }
        if (profile.getSeasons() != null) {
            for (ProgramSeason programSeason : profile.getSeasons()) {
                if (programSeason.notFilledIn()) {
                    emptyObjectsToRemoveL1.add(programSeason);
                }
            }
            profile.getSeasons().removeAll(emptyObjectsToRemoveL1);
            emptyObjectsToRemoveL1.clear();
        }
        if (profile.getHolidays() != null) {
            for (ProgramHoliday programHoliday : profile.getHolidays()) {
                if (programHoliday.notFilledIn()) {
                    emptyObjectsToRemoveL1.add(programHoliday);
                }
            }
            profile.getHolidays().removeAll(emptyObjectsToRemoveL1);
            emptyObjectsToRemoveL1.clear();
        }
        if (profile.getProgramDemands() != null) {
            for (ProgramDemand programDemand : profile.getProgramDemands()) {
                if (programDemand.notFilledIn()) {
                    emptyObjectsToRemoveL1.add(programDemand);
                }
            }
            profile.getProgramDemands().removeAll(emptyObjectsToRemoveL1);
            emptyObjectsToRemoveL1.clear();
        }
    }

    private ProgramSeason findSeasonByUUID(String seasonUuid) {
        for (ProgramSeason season : profile.getSeasons()) {
            if (season.getUuid().equals(seasonUuid)) {
                return season;
            }
        }
        return null;
    }

    private void saveProfileAndClear() {
        List<ProgramProfile> profiles = programService.getOverlappedProfiles(profile);
        if (!profiles.isEmpty()) {
            uiMessage.addMessage("Profile dates overlap with another profile", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            System.out.println("in try line 569");
            programService.saveProgramAndProfile(program,profile, ((User) identity.getAccount()).getEmail());
            uiMessage.addMessage("Program Profile saved");
            clearProfile();
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save program profile, please try again later and contact your administrator if you keep having this problem");
            System.out.println("in catch line 574");
            log.warn("Error saving a program profile", e);
        }
    }

    public void cancelProfile() {
        clearProfile();
    }

    private void clearProfile() {
        System.out.println("in clear profile line 581");
        profile = null;
        newProfile = false;
        historyList = null;
        initPickLists();
        program = programService.getProgram(program.getId());
    }

    public void add() {
        profile = new ProgramProfile();
        newProfile = true;
        profile.setProgram(program);
    }

    public void addOption() {
        if (profile.getOptions() == null) {
            profile.setOptions(new ArrayList<>());
        }
        final ProgramOption option = new ProgramOption();
        option.setProfile(profile);
        profile.getOptions().add(option);
    }

    public void removeOption(ProgramOption programOption) {
        profile.getOptions().remove(programOption);
    }

    public void removeDemand(ProgramDemand programDemand) {
        profile.getProgramDemands().remove(programDemand);
    }

    public void addProduct(ProgramOption option) {
        if (option.getProducts() == null) {
            option.setProducts(new ArrayList<>());
        }
        final ProgramProduct product = new ProgramProduct();
        product.setOption(option);
        option.getProducts().add(product);
    }

    public void removeProduct(ProgramOption programOption, ProgramProduct programProduct) {
        int programOptionIndex = profile.getOptions().indexOf(programOption);
        profile.getOptions().get(programOptionIndex).getProducts().remove(programProduct);
    }

    public void addSeason() {
        if (profile.getSeasons() == null) {
            profile.setSeasons(new ArrayList<>());
        }
        final ProgramSeason season = new ProgramSeason(profile);
        profile.getSeasons().add(season);
    }

    public void removeSeason(ProgramSeason season) {
        profile.getSeasons().remove(season);
    }

    public void addEventDuration() {
        if (profile.getEventDurations() == null) {
            profile.setEventDurations(new ArrayList<>());
        }
        final ProgramEventDuration eventDuration = new ProgramEventDuration();
        eventDuration.setOption(EventDurationOption.ENTIRE_PROGRAM);
        eventDuration.setProfile(profile);
        profile.getEventDurations().add(eventDuration);
        profile.getEventDurations().stream().forEach(e -> filterProgramProducts(e));
    }

    public void removeEventDuration(ProgramEventDuration programEventDuration) {
        profile.getEventDurations().remove(programEventDuration);
    }

    public void addCustomerNotification() {
        if (profile.getCustomerNotifications() == null) {
            profile.setCustomerNotifications(new ArrayList<>());
        }
        final ProgramCustomerNotification customerNotification = new ProgramCustomerNotification();
        customerNotification.setProfile(profile);
        profile.getCustomerNotifications().add(customerNotification);
    }

    public void removeCustomerNotification(ProgramCustomerNotification programCustomerNotification) {
        profile.getCustomerNotifications().remove(programCustomerNotification);
    }

    public void addNotificationDuplicationSource(ProgramCustomerNotification programCustomerNotification) {
        if (programCustomerNotification.getNotificationDuplicationSources() == null) {
            programCustomerNotification.setNotificationDuplicationSources(new ArrayList<>());
        }
        final NotificationDuplicationSource source = new NotificationDuplicationSource();
        source.setProgramCustomerNotification(programCustomerNotification);

        programCustomerNotification.getNotificationDuplicationSources().add(source);
    }

    public void removeNotificationDuplicationSource(ProgramCustomerNotification programCustomerNotification, NotificationDuplicationSource source) {
        programCustomerNotification.getNotificationDuplicationSources().remove(source);
    }

    public void addHoliday() {
        if (profile.getHolidays() == null) {
            profile.setHolidays(new ArrayList<>());
        }
        final ProgramHoliday holiday = new ProgramHoliday();
        holiday.setProfile(profile);
        profile.getHolidays().add(holiday);
    }

    public void removeHoliday(ProgramHoliday programHoliday) {
        profile.getHolidays().remove(programHoliday);
    }

    public void addDRMSProgramMapping() {
        if (profile.getDrmsProgramMappings() == null) {
            profile.setDrmsProgramMappings(new ArrayList<>());
        }
        final DRMSProgramMapping drmsProgramMapping = new DRMSProgramMapping();
        drmsProgramMapping.setProfile(profile);
        profile.getDrmsProgramMappings().add(drmsProgramMapping);
    }

    public void removeDrmsProgramMapping(DRMSProgramMapping drmsProgramMapping) {
        profile.getDrmsProgramMappings().remove(drmsProgramMapping);
    }

    public void addSublapProgramMapping() {
        if (profile.getSublapProgramMappings() == null) {
            profile.setSublapProgramMappings(new ArrayList<>());
        }
        final SublapProgramMapping sublapProgramMapping = new SublapProgramMapping();
        sublapProgramMapping.setProfile(profile);
        profile.getSublapProgramMappings().add(sublapProgramMapping);
    }

    public void removeSublapProgramMapping(SublapProgramMapping sublapProgramMapping) {
        profile.getSublapProgramMappings().remove(sublapProgramMapping);
    }

    public void addEnrollmentNotification() {
        if (profile.getEnrollmentNotifications() == null) {
            profile.setEnrollmentNotifications(new ArrayList<>());
        }
        final ProgramEnrollmentNotification enrollmentNotification = new ProgramEnrollmentNotification();
        enrollmentNotification.setProfile(profile);
        profile.getEnrollmentNotifications().add(enrollmentNotification);
    }

    public void addDemand() {
        if (profile.getProgramDemands() == null) {
            profile.setProgramDemands(new ArrayList<>());
        }
        final ProgramDemand demand = new ProgramDemand();
        demand.setProfile(profile);
        profile.getProgramDemands().add(demand);
    }

    public void addMultipleParticipations() {
        if (profile.getProgramMultiParticipations() == null) {
            profile.setProgramMultiParticipations(new ArrayList<>());
        }
        final ProgramMultipleParticipation participation = new ProgramMultipleParticipation();
        participation.setProfile(profile);
        participation.setPrograms(new ArrayList<>());
        profile.getProgramMultiParticipations().add(participation);
        programPickListModel.put(participation.getUuid(), generateDefaultProgramDualListModel());
    }

    public void editProfile(ProgramProfile profile) {
        System.out.println("in edit Profile line 748");
        cancelProfile();
        this.profile = profile;
        this.historyList = historyService.getHistory(profile);
        this.marketEligibleChanged = profile.isWholesaleMarketEligible();
        this.participationActiveChanged = profile.isWholesaleParticipationActive();
        assignPickListValues();
        assignMeterTypeValues();
        initSafetyReductionFactors();
        if (profile.getCustomerNotifications().isEmpty()) {
            addCustomerNotification();
            System.out.println("line 761");
        }
        if (CollectionUtils.isNotEmpty(profile.getEventDurations())) {
            profile.getEventDurations().stream().forEach(e -> filterProgramProducts(e));
            System.out.println("line 765");
        }
        for (ProgramProfileAsset programProfileAsset : this.profile.getProgramProfileAssets()) {
            loadProfilesByGrid(programProfileAsset);
            loadCatalogAssetsByProfile(programProfileAsset);
            System.out.println("line 770");
        }
        System.out.println("in line 772");
    }

    private void assignPickListValues() {
        System.out.println("in line 771");
        for (ProgramEnroller programEnroller : profile.getEnrollers()) {

            final EnrollerSource enrollerSource = EnrollerSource.valueOf(programEnroller.getName());
            enrollerSources.getSource().remove(enrollerSource);
            enrollerSources.getTarget().add(enrollerSource);
        }
        for (ProgramEnrollmentAttribute programEnrollmentAttribute : profile.getEnrollmentAttributes()) {
            final EnrollmentAttribute enrollmentAttribute = EnrollmentAttribute.valueOf(programEnrollmentAttribute.getName());
            enrollmentAttributes.getSource().remove(enrollmentAttribute);
            enrollmentAttributes.getTarget().add(enrollmentAttribute);
        }
        for (ProgramCustomerDataAttributeChanges programCustomerDataAttributeChanges : profile.getCustomerDataAttributeChanges()) {
            final CDWAttribute cdwAttribute = CDWAttribute.valueOf(programCustomerDataAttributeChanges.getName());
            cdwAttributes.getSource().remove(cdwAttribute);
            cdwAttributes.getTarget().add(cdwAttribute);
        }
        for (ProgramEligibilitySaStatus programEligibilitySaStatus : profile.getSaStatuses()) {
            ServiceAgreement.SaStatus saStatus = getSaStatusByValue(programEligibilitySaStatus.getStatus());
            saStatusPickList.getSource().remove(saStatus);
            saStatusPickList.getTarget().add(saStatus);
        }
        for (ProgramEligibilityCustomerType eligibilityCustomerType : profile.getCustomerTypes()) {
            eligibilityCustomerTypePickList.getSource().remove(eligibilityCustomerType.getCustomerType());
            eligibilityCustomerTypePickList.getTarget().add(eligibilityCustomerType.getCustomerType());
        }
        for (ProgramEligibilityRateSchedule programEligibilityRateSchedule : profile.getRateSchedules()) {
            final RateSchedule rateSchedule = RateSchedule.valueOf(programEligibilityRateSchedule.getRateSchedule());
            rateSchedulesPickList.getSource().remove(rateSchedule);
            rateSchedulesPickList.getTarget().add(rateSchedule);
        }
        for (ProgramEligibilityPremiseType programEligibilityPremiseType : profile.getPremiseTypes()) {
            final PremiseType premiseType = PremiseType.valueOf(programEligibilityPremiseType.getPremiseType());
            premiseTypesPickList.getSource().remove(premiseType);
            premiseTypesPickList.getTarget().add(premiseType);
        }
        for (ProgramDefinedDispatchLevel programDefinedDispatchLevel : profile.getDispatchLevels()) {
            dispatchLevelPickList.getSource().remove(programDefinedDispatchLevel.getDispatchLevel());
            dispatchLevelPickList.getTarget().add(programDefinedDispatchLevel.getDispatchLevel());
        }
        for (ProgramDefinedDispatchReason programDefinedDispatchReason : profile.getDispatchReasons()) {
            dispatchReasonPickList.getSource().remove(programDefinedDispatchReason.getDispatchReason());
            dispatchReasonPickList.getTarget().add(programDefinedDispatchReason.getDispatchReason());
        }
        for (ProgramMultipleParticipation multipleParticipation : profile.getProgramMultiParticipations()) {
            final DualListModel<Program> programDualListModel = generateDefaultProgramDualListModel();
            for (ProgramMultipleParticipationProgram selectedProgram : multipleParticipation.getPrograms()) {
                programDualListModel.getSource().remove(selectedProgram.getProgram());
                programDualListModel.getTarget().add(selectedProgram.getProgram());
            }
            programPickListModel.put(multipleParticipation.getUuid(), programDualListModel);
        }
        for (EligibleProgram eligibleProgram : profile.getEligiblePrograms()) {
            programPickListModelForWholesale.getSource().remove(eligibleProgram.getProgram());
            List target = programPickListModelForWholesale.getTarget();
            if (target != null) {
                if (!target.contains(eligibleProgram.getProgram())) {
                    target.add(eligibleProgram.getProgram());
                }
            }
        }
        for (ProgramEssentialCustomer programEssentialCustomer : profile.getEssentialCustomers()) {
            essentialCustomers.add(programEssentialCustomer.getEssentialCustomerType());
        }
    }

    private ServiceAgreement.SaStatus getSaStatusByValue(String status) {
        for (ServiceAgreement.SaStatus saStatus : ServiceAgreement.SaStatus.values()) {
            if (saStatus.getValue().equals(status)) {
                return saStatus;
            }
        }
        return null;
    }

    private DualListModel<Program> generateDefaultProgramDualListModel() {
        final DualListModel<Program> programDualListModel = new DualListModel<>();
        programDualListModel.setSource(new ArrayList<>());
        programDualListModel.setTarget(new ArrayList<>());
        for (Program availableProgram : availablePrograms) {
            if (!availableProgram.equals(program)) {
                programDualListModel.getSource().add(availableProgram);
            }
        }
        return programDualListModel;
    }

    private void assignMeterTypeValues() {
        System.out.println("in line 859");
        meterTypes = new ArrayList<>();
        for (ProgramEquipment equipment : profile.getEquipments()) {
            meterTypes.add(equipment.getMeterType());
        }
        System.out.println("in line 864");
    }

    public List<String> enrollersAsString() {
        List<String> result = new ArrayList<>();
        if (profile.getEnrollers() != null) {
            for (ProgramEnroller programEnroller : profile.getEnrollers()) {
                result.add(programEnroller.getName());
            }
        }
        return result;
    }

    public String getEnrollmentLabel(String s) {
        return EnrollerSource.valueOf(s).getLabel();
    }

    public void addFSLRule(String type, boolean essential) {
        if (profile.getFslRules() == null) {
            profile.setFslRules(new ArrayList<>());
        }
        ProgramFirmServiceLevelRule rule = new ProgramFirmServiceLevelRule();
        rule.setType(type);
        rule.setEssential(essential);
        rule.setComparisonAttribute("FSL Commitment");
        rule.setProfile(profile);
        profile.getFslRules().add(rule);
    }

    public void addFSLRuleSeason(boolean essential) {
        addFSLRule("season", essential);
    }

    public void addFSLRuleLoadReduction(boolean essential) {
        addFSLRule("load_reduction", essential);
    }

    public void removeFslRule(ProgramFirmServiceLevelRule rule) {
        profile.getFslRules().remove(rule);
    }

    public void onRowEdit(RowEditEvent event) {

    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void addCreditDiscount() {
        profile.getCreditDiscounts().add(new CreditDiscount(profile));
    }

    public void removeCreditDiscount(CreditDiscount discount) {
        profile.getCreditDiscounts().remove(discount);
    }

    public void addDiscountFees(CreditDiscount discount) {
        discount.getFees().add(new CreditDiscountFee(discount));
    }

    public void removeDiscountFee(CreditDiscount discount, CreditDiscountFee fee) {
        discount.getFees().remove(fee);
    }

    public void addCreditsDiscountsFeeComparison(CreditDiscount creditDiscount) {
        if (creditDiscount.getCreditsDiscountsFeeComparisons() == null) {
            creditDiscount.setCreditsDiscountsFeeComparisons(new ArrayList<>());
        }
        creditDiscount.getCreditsDiscountsFeeComparisons().add(new CreditsDiscountsFeeComparison(creditDiscount));
    }

    public void removeCreditsDiscountsFeeComparisons(CreditDiscount creditDiscount, CreditsDiscountsFeeComparison feeComparison) {
        creditDiscount.getCreditsDiscountsFeeComparisons().remove(feeComparison);
    }

    public void filterProgramProducts(ProgramEventDuration eventDuration) {
        EventDurationOption option = eventDuration.getOption();
        if (CollectionUtils.isEmpty(profile.getOptions())) {
            eventDuration.setOptionProducts(new ArrayList<>());
        } else {
            eventDuration.setOptionProducts(profile.getOptions().stream().filter(op -> op.getType() != null && (op.getType() == option || EventDurationOption.ENTIRE_PROGRAM == option)).flatMap(o ->
                    {
                        final List<ProgramProduct> products = o.getProducts();
                        if (CollectionUtils.isEmpty(products)) {
                            return new ArrayList<ProgramProduct>().stream();
                        }
                        return products.stream().filter(p -> StringUtils.isNotEmpty(p.getName()));
                    }
            ).collect(Collectors.toList()));
        }
    }

    public List<EventDurationOption> filterProgramOptions() {
        if (CollectionUtils.isEmpty(profile.getOptions())) {
            return Arrays.asList(EventDurationOption.ENTIRE_PROGRAM);
        }
        List<EventDurationOption> options = profile.getOptions().stream().filter(op -> op.getType() != null).map(o -> o.getType()).distinct().collect(Collectors.toList());
        if (options == null) {
            options = new ArrayList<>();
        }
        if (!options.contains(EventDurationOption.ENTIRE_PROGRAM)) {
            options.add(EventDurationOption.ENTIRE_PROGRAM);
        }
        return options;
    }

    public void loadProfilesByGrid(ProgramProfileAsset profileAsset){
        System.out.println("in line 975");
        if (profileAsset.getNetworkType() == null) {
            System.out.println("in line 977");
            return;
        }
        profileAsset.setAssetProfilesByGrid(assetProfileService.getProfilesByGrid(profileAsset.getNetworkType()));
    }
    public void loadCatalogAssetsByProfile(ProgramProfileAsset profileAsset){
        System.out.println("in line 982");
        if (profileAsset.getAssetProfile() == null) {
            System.out.println("in lin e984");
            return;
        }
        profileAsset.setAssetsByFilter(assetService.getByProfile(profileAsset.getAssetProfile()));
    }
    public void addEquipment(){
        final ProgramProfileAsset newProfile = new ProgramProfileAsset();
        newProfile.setProfile(profile);
        profile.getProgramProfileAssets().add(newProfile);
    }
    public void removeEquipment(ProgramProfileAsset profileAsset){
        profile.getProgramProfileAssets().remove(profileAsset);
    }
}