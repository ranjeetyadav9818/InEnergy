package com.inenergis.controller.program;

import com.inenergis.controller.lazyDataModel.LazyRatePlanDataModel;
import com.inenergis.controller.model.RateAncillaryFeeGroup;
import com.inenergis.controller.model.RateConsumptionFeeGroup;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ContractType;
import com.inenergis.entity.History;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.genericEnum.GeneralEligibilityAttributeType;
import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.genericEnum.RatePlanStatus;
import com.inenergis.entity.maintenanceData.FeeType;
import com.inenergis.entity.maintenanceData.GasFeeType;
import com.inenergis.entity.maintenanceData.PowerSource;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.program.ApplicableRatePlan;
import com.inenergis.entity.program.ChargesAttribute;
import com.inenergis.entity.program.ChargesFee;
import com.inenergis.entity.program.ChargesFeeComparison;
import com.inenergis.entity.program.CreditDiscount;
import com.inenergis.entity.program.CreditDiscountFee;
import com.inenergis.entity.program.CreditsDiscountsFeeComparison;
import com.inenergis.entity.program.GeneralAvailability;
import com.inenergis.entity.program.GeneralAvailabilityApplicableValue;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.entity.program.RatePlanDemand;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.RatePlanProfileAsset;
import com.inenergis.entity.program.rateProgram.*;
import com.inenergis.model.GeneralAvailabilityDualModel;
import com.inenergis.service.AssetService;
import com.inenergis.service.ChargesFeeService;
import com.inenergis.service.CreditDiscountFeeService;
import com.inenergis.service.HistoryService;
import com.inenergis.service.InvoiceService;
import com.inenergis.service.MaintenanceDataService;
import com.inenergis.service.MeterService;
import com.inenergis.service.PercentageFeeHierarchyService;
import com.inenergis.service.PremiseService;
import com.inenergis.service.RateCodeProfileService;
import com.inenergis.service.RateCodeService;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.service.TimeOfUseService;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.TimeUtil;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Ajax;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class RatePlanController implements Serializable {

    public static final String WARNING_YOU_ARE_ADDING_REPEATED_FEE_GROUPS = "Warning. You are adding repeated fee groups";

    @Inject
    private RatePlanService ratePlanService;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private EntityManager entityManager;

    @Inject
    private RateCodeService rateCodeService;

    @Inject
    private ChargesFeeService chargesFeeService;

    @Inject
    private CreditDiscountFeeService creditDiscountFeeService;

    @Inject
    private AssetService assetService;

    @Inject
    private Identity identity;

    @Inject
    private HistoryService historyService;

    @Inject
    private PremiseService premiseService;

    @Inject
    private MeterService meterService;

    @Inject
    private ServiceAgreementService serviceAgreementService;

    @Inject
    private RateCodeProfileService rateCodeProfileService;

    @Inject
    private MaintenanceDataService maintenanceDataService;

    @Inject
    private TimeOfUseCalendarService timeOfUseCalendarService;

    @Inject
    private TimeOfUseService timeOfUseService;

    @Inject
    private InvoiceService invoiceService;

    @Inject
    private PercentageFeeHierarchyService percentageFeeHierarchyService;

    private Logger log = LoggerFactory.getLogger(RatePlanController.class);

    private boolean editMode = false;
    private RatePlan ratePlan;
    private RatePlanProfile newProfile;
    //change
    private List<ContractType> contractTypeList;
    private ContractType contractType;
    private RatePlanProfile selectedProfile;
    private String filterRateCodeId;
    private RateCodeSector filterRateCodeSector;
    private RatePlanStatus filterRateCodeStatus;
    private LazyRatePlanDataModel lazyRatePlanDataModel;
    private List<RateCode> rateCodes;
    private List<ChargesFee> chargesFees = new ArrayList<>();
    private List<CreditDiscountFee> creditDiscountFees = new ArrayList<>();
    private DualListModel<Asset> assetsPickList = new DualListModel<>();
    private List<History> historyList;
    private DualListModel<RatePlan> ratePlanDualListModel = new DualListModel<>();
    private List<GeneralAvailabilityDualModel> generalEligibility = new ArrayList<>();
    private boolean viewModeOn = false;
    private List<String> feeHierarchyNames = new ArrayList<>();
    private int rateAncillaryFeeGroupSequence = 0;
    private int rateConsumptionFeeGroupSequence = 0;
private boolean aplicable=false;
    @PostConstruct
    public void init() {
        doInit();
    }

    public void doInit() {
        filterRateCodeStatus = RatePlanStatus.ACTIVE;
        contractTypeList = ratePlanService.getContractTypes();
        lazyRatePlanDataModel = new LazyRatePlanDataModel(entityManager, generateStatusPrefFilter());
        chargesFees = chargesFeeService.getAllComparisonEligible();
        creditDiscountFees = creditDiscountFeeService.getAllComparisonEligible();
        feeHierarchyNames = percentageFeeHierarchyService.getDistinctPercentageFeeHierarchyNames();
    }

    public void search() {
        lazyRatePlanDataModel = new LazyRatePlanDataModel(entityManager, generateStatusPrefFilter());
    }


    private Map<String, Object> generateStatusPrefFilter() {
        Map<String, Object> preFilter = new HashMap<>();

        if (StringUtils.isNotBlank(filterRateCodeId)) {
            preFilter.put("codeId", filterRateCodeId);
        }

        if (filterRateCodeSector != null) {
            preFilter.put("sector", filterRateCodeSector);
        }

        preFilter.put("status", filterRateCodeStatus);

        return preFilter;
    }

    public void save() {
        ratePlan = ratePlanService.saveOrUpdate(ratePlan);
        clear();
    }

    public void clear() {
        filterRateCodeSector = null;
        filterRateCodeId = null;
        filterRateCodeStatus = RatePlanStatus.ACTIVE;
        editMode = false;
    }

    public void add() {
        ratePlan = new RatePlan();
        editMode = true;
    }

    public void update(RatePlan ratePlan) {
        this.ratePlan = ratePlan;
        editMode = true;
    }

    public void addProfile(RatePlan ratePlan) {
        newProfile = new RatePlanProfile(ratePlan);
    }

    public void saveNewProfile(RatePlan ratePlan) {
        newProfile = ratePlanService.saveOrUpdateProfile(newProfile, ((User) identity.getAccount()).getEmail());
        ratePlan.addProfile(newProfile);
        uiMessage.addMessage("Profile {0} created", newProfile.getName());
        cancelNewProfile();
    }

    public void cancelNewProfile() {
        newProfile = null;
    }

    public void editProfile(RatePlanProfile profile) {
//        rateCodes = rateCodeService.listBySector(profile.getRatePlan().getSector());


        selectedProfile = profile;
        historyList = historyService.getHistory(selectedProfile);
        // Applicable Rate Plans
        final List<ApplicableRatePlan> applicableRatePlanList = selectedProfile.getApplicableRatePlans();
        List<RatePlan> applicableRatePlans = applicableRatePlanList.stream()
                .map(ApplicableRatePlan::getRatePlan)
                .collect(Collectors.toList());
        List<RatePlan> availableRatePlans = ratePlanService.getAll();
        availableRatePlans.removeAll(applicableRatePlans);
        availableRatePlans.remove(ratePlan);
        availableRatePlans = availableRatePlans.stream()
                .filter(plan -> plan.getStatus() == RatePlanStatus.ACTIVE)
                .filter(plan -> !plan.equals(selectedProfile.getRatePlan()))
                .collect(Collectors.toList());

        ratePlanDualListModel.setSource(availableRatePlans);
        ratePlanDualListModel.setTarget(applicableRatePlans);

        for (GeneralAvailability generalAvailability : profile.getGeneralAvailabilities()) {
            List<String> target = generalAvailability.getApplicableValues().stream()
                    .map(GeneralAvailabilityApplicableValue::getValue)
                    .collect(Collectors.toList());

            List<String> source = getDistinctValues(generalAvailability).stream()
                    .filter(s -> !target.contains(s))
                    .collect(Collectors.toList());

            generalEligibility.add(new GeneralAvailabilityDualModel(generalAvailability, new DualListModel<>(source, target)));
        }
        if (CollectionUtils.isNotEmpty(selectedProfile.getRateCodeProfiles())) {
            final List<RateCode> rateCodesToRemove = selectedProfile.getRateCodeProfiles().stream().map(RateCodeProfile::getRateCode).collect(Collectors.toList());
            rateCodes.removeAll(rateCodesToRemove);
            reOrderRateCodeProfiles();
        }

        assetsPickList.setSource(assetService.getAll());
        assetsPickList.setTarget(new ArrayList<>());

        if (profile.getRatePlanProfileAssets() != null) {
            for (RatePlanProfileAsset ratePlanProfileAsset : profile.getRatePlanProfileAssets()) {
                final Asset asset = ratePlanProfileAsset.getAsset();
                assetsPickList.getSource().remove(asset);
                if (!assetsPickList.getTarget().contains(asset)) {
                    assetsPickList.getTarget().add(asset);
                }
            }
        }
        assetsPickList.getSource().sort(Comparator.comparing(Asset::getName, Collator.getInstance()));
        assetsPickList.getTarget().sort(Comparator.comparing(Asset::getName, Collator.getInstance()));
        rateConsumptionFeeGroupSequence = selectedProfile.getActiveConsumptionFees().stream().map(RateProfileFee::getGroupId).reduce(0, Integer::max) + 1;
        rateAncillaryFeeGroupSequence = selectedProfile.getActiveAncillaryFees().stream().map(RateProfileFee::getGroupId).reduce(0, Integer::max) + 1;
        viewModeOn = false;
    }

    private List<String> getDistinctValues(GeneralAvailability generalAvailability) {
        switch (generalAvailability.getAttributeType()) {
            case PREMISE_TYPE:
                return premiseService.getDistinctPremiseTypeValues();
            case HAS_3RD_PARTY_DRP:
                return serviceAgreementService.getDistinctHas3rdPartyValues();
            case CUST_CLASS_CD:
                return serviceAgreementService.getDistinctCustClassCdValues();
            case CUST_SIZE:
                return serviceAgreementService.getDistinctCustSizeValues();
            case MTR_CONFIG_TYPE:
                return meterService.getDistinctMtrConfigTypeValues();
            default:
                return new ArrayList<>();
        }
    }

    public void loadDistinctValues(GeneralAvailabilityDualModel model) {
        model.setDualListModel(new DualListModel<>(getDistinctValues(model.getGeneralAvailability()), new ArrayList<>()));
    }


    public void viewProfile(RatePlanProfile profile) {
        editProfile(profile);
        viewModeOn = true;
    }


    public void cancelProfile() {
        selectedProfile = null;
        historyList = null;
    }

    public void saveProfile() {
        if (!isChargesFeeComparisonValid(selectedProfile) || !isCreditDiscountFeeComparisonValid(selectedProfile)) {

            uiMessage.addMessage("Fees Comparison Configuration is Invalid", FacesMessage.SEVERITY_ERROR);
            return;
        }

        selectedProfile.setApplicableRatePlans(ratePlanDualListModel.getTarget().stream()
                .map(ApplicableRatePlan::new)
                .peek(applicableRatePlan -> applicableRatePlan.setRatePlanProfile(selectedProfile))
                .collect(Collectors.toList()));

        for (GeneralAvailabilityDualModel model : generalEligibility) {
            if (!selectedProfile.getGeneralAvailabilities().contains(model.getGeneralAvailability())) {
                selectedProfile.getGeneralAvailabilities().add(new GeneralAvailability(selectedProfile));
            }

            GeneralAvailability generalAvailability = selectedProfile.getGeneralAvailabilities()
                    .get(selectedProfile.getGeneralAvailabilities().indexOf(model.getGeneralAvailability()));

            List<GeneralAvailabilityApplicableValue> selectedValues = model.getDualListModel().getTarget().stream()
                    .map(s -> new GeneralAvailabilityApplicableValue(model.getGeneralAvailability(), s))
                    .collect(Collectors.toList());
            for (GeneralAvailabilityApplicableValue selectedValue : selectedValues) {
                if (!generalAvailability.getApplicableValues().contains(selectedValue)) {
                    generalAvailability.getApplicableValues().add(selectedValue);
                }
            }

            generalAvailability.getApplicableValues().removeIf(existingValue -> !selectedValues.contains(existingValue));
        }

        final Date effectiveEndDate = selectedProfile.getEffectiveEndDate();
        if (effectiveEndDate != null) {
            selectedProfile.setEffectiveEndDate(TimeUtil.getEndOfDay(effectiveEndDate, ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        }

        if (selectedProfile.isActive() && !canActivateProfile()) {
            uiMessage.addMessage("Profile {0} updated can't be activated. Check start/end dates.",
                    FacesMessage.SEVERITY_ERROR, selectedProfile.getName());
            return;
        }

        for (Asset asset : assetsPickList.getSource()) {
            RatePlanProfileAsset ratePlanProfileAsset = new RatePlanProfileAsset(selectedProfile,  asset);
            if (selectedProfile.getRatePlanProfileAssets().contains(ratePlanProfileAsset)) {
                selectedProfile.getRatePlanProfileAssets().remove(ratePlanProfileAsset);
            }
        }
        for (Asset asset : assetsPickList.getTarget()) {
            RatePlanProfileAsset ratePlanProfileAsset = new RatePlanProfileAsset(selectedProfile, asset);
            if (!selectedProfile.getRatePlanProfileAssets().contains(ratePlanProfileAsset)) {
                selectedProfile.getRatePlanProfileAssets().add(ratePlanProfileAsset);
            }
        }
        final List<RateProfileAncillaryFee> ancillaryFeesToDelete = selectedProfile.getAncillaryFees().stream().filter(fee -> StringUtils.isEmpty(fee.getName()) && fee.isActive()).collect(Collectors.toList());
        selectedProfile.getAncillaryFees().removeAll(ancillaryFeesToDelete);

        final List<RateProfileConsumptionFee> consumptionFeesToDelete = selectedProfile.getConsumptionFees().stream().filter(fee -> StringUtils.isEmpty(fee.getName()) && fee.isActive()).collect(Collectors.toList());
        selectedProfile.getConsumptionFees().removeAll(consumptionFeesToDelete);

        switch (selectedProfile.getBillingTermFrequency()) {
            case WEEKLY:
                selectedProfile.setBillingDayOfMonth(null);
                selectedProfile.setBillingMonth(null);
                break;
            case MONTHLY:
                selectedProfile.setBillingDayOfWeek(null);
                selectedProfile.setBillingMonth(null);
            case ANNUALLY:
                selectedProfile.setBillingDayOfWeek(null);
                break;
        }
        selectedProfile = ratePlanService.saveOrUpdateProfile(selectedProfile, ((User) identity.getAccount()).getEmail());
        uiMessage.addMessage("Profile {0} updated", selectedProfile.getName());
    }

    private boolean canActivateProfile() {
        RatePlanProfile currentActiveProfile = selectedProfile.getRatePlan().getActiveProfile();

        return currentActiveProfile == null || currentActiveProfile.equals(selectedProfile) ||
                ((selectedProfile.getEffectiveStartDate().before(currentActiveProfile.getEffectiveStartDate()) ||
                        selectedProfile.getEffectiveStartDate().after(currentActiveProfile.getEffectiveEndDate())) &&
                        (selectedProfile.getEffectiveEndDate().before(currentActiveProfile.getEffectiveStartDate()) ||
                                selectedProfile.getEffectiveEndDate().after(currentActiveProfile.getEffectiveEndDate())));
    }

    public void saveProfileAndClose() {
        saveProfile();
        Ajax.update("form");
        cancelProfile();
    }

    public void addDemand() {
        selectedProfile.getRatePlanDemands().add(new RatePlanDemand(selectedProfile));
    }

    public void removeDemand(RatePlanDemand ratePlanDemand) {
        selectedProfile.getRatePlanDemands().remove(ratePlanDemand);
    }

    public void addGeneralAvailability() {
        GeneralAvailability ga = new GeneralAvailability(selectedProfile);
        ga.setAttributeType(GeneralEligibilityAttributeType.PREMISE_TYPE);
        ga.setApplicableValues(new ArrayList<>());

        selectedProfile.getGeneralAvailabilities().add(ga);

        GeneralAvailabilityDualModel model = new GeneralAvailabilityDualModel(ga, new DualListModel<>());
        loadDistinctValues(model);

        generalEligibility.add(new GeneralAvailabilityDualModel(ga, model.getDualListModel()));
    }

    public void removeGeneralAvailability(GeneralAvailabilityDualModel generalAvailabilityDualModel) {
        selectedProfile.getGeneralAvailabilities().remove(generalAvailabilityDualModel.getGeneralAvailability());
        generalEligibility.remove(generalAvailabilityDualModel);
    }

    public void clearRateCodeProfiles() {
        selectedProfile.getRateCodeProfiles().clear();
        rateCodes = rateCodeService.listBySector(ratePlan.getSector());
    }

    public void removeRateCodeProfile(RateCodeProfile rateCodeProfile) {
        selectedProfile.getRateCodeProfiles().remove(rateCodeProfile);
        rateCodes.add(rateCodeProfile.getRateCode());
    }

    public void onRateCodeDrop(DragDropEvent ddEvent) {
        final RateCode rateCode = (RateCode) ddEvent.getData();
        doDrop(rateCode);
    }

    public void doDrop(RateCode rateCode) {
        RateCodeProfile rateCodeProfile = new RateCodeProfile();
        rateCodeProfile.setRateCode(rateCode);
        rateCodeProfile.setRatePlanProfile(selectedProfile);
        final List<RateCodeProfile> rateCodeProfiles = selectedProfile.getRateCodeProfiles();
        final Optional<RateCodeProfile> existsRateCode = rateCodeProfiles.stream().filter(c -> c.getRateCode().equals(rateCode)).findFirst();
        if (!existsRateCode.isPresent()) {
            rateCodeProfiles.add(rateCodeProfile);
            rateCodeProfileService.update(rateCodeProfile);
        }
        rateCodes.remove(rateCode);
    }


    public void reOrderRateCodeProfiles() {
        final List<RateCodeProfile> rateCodeProfiles = selectedProfile.getRateCodeProfiles();
        if (CollectionUtils.isEmpty(rateCodeProfiles)) {
            return;
        }
        rateCodeProfiles.sort(RateCodeProfile.getComparator());
    }

    public void addChargesAttribute() {
        selectedProfile.getChargesAttributes().add(new ChargesAttribute(selectedProfile));
    }

    public void addFees(ChargesAttribute attribute) {
        if (attribute.getFees() == null) {
            attribute.setFees(new ArrayList<>());
        }

        attribute.getFees().add(new ChargesFee(attribute));
    }

    public void addFeeComparison(ChargesAttribute attribute) {
        if (attribute.getChargesFeeComparisons() == null) {
            attribute.setChargesFeeComparisons(new ArrayList<>());
        }
        attribute.getChargesFeeComparisons().add(new ChargesFeeComparison(attribute));
    }

    public void addCreditsDiscountsFeeComparison(CreditDiscount creditDiscount) {
        if (creditDiscount.getCreditsDiscountsFeeComparisons() == null) {
            creditDiscount.setCreditsDiscountsFeeComparisons(new ArrayList<>());
        }
        creditDiscount.getCreditsDiscountsFeeComparisons().add(new CreditsDiscountsFeeComparison(creditDiscount));
    }

    public void removeFeeComparison(ChargesAttribute attribute, ChargesFeeComparison feeComparison) {
        attribute.getChargesFeeComparisons().remove(feeComparison);
    }

    public void removeCreditsDiscountsFeeComparisons(CreditDiscount creditDiscount, CreditsDiscountsFeeComparison feeComparison) {
        creditDiscount.getCreditsDiscountsFeeComparisons().remove(feeComparison);
    }

    public void removeChargesAttribute(ChargesAttribute attribute) {
        selectedProfile.getChargesAttributes().remove(attribute);
    }

    public void removeFee(ChargesAttribute attribute, ChargesFee fee) {
        attribute.getFees().remove(fee);
    }

    public void addCreditDiscount() {
        selectedProfile.getCreditDiscounts().add(new CreditDiscount(selectedProfile));
    }

    public void removeCreditDiscount(CreditDiscount discount) {
        selectedProfile.getCreditDiscounts().remove(discount);
    }

    public void addDiscountFees(CreditDiscount discount) {
        discount.getFees().add(new CreditDiscountFee(discount));
    }

    public void removeDiscountFee(CreditDiscount discount, CreditDiscountFee fee) {
        discount.getFees().remove(fee);
    }

    private boolean isChargesFeeComparisonValid(RatePlanProfile ratePlanProfile) {
        for (ChargesAttribute ca : ratePlanProfile.getChargesAttributes()) {
            if (ca != null && CollectionUtils.isNotEmpty(ca.getChargesFeeComparisons())) {
                for (ChargesFeeComparison comparison : ca.getChargesFeeComparisons()) {
                    if (comparison.getChargesFeeOne().equals(comparison.getChargesFeeTwo())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isCreditDiscountFeeComparisonValid(RatePlanProfile ratePlanProfile) {
        for (CreditDiscount cd : ratePlanProfile.getCreditDiscounts()) {
            final List<CreditsDiscountsFeeComparison> creditsDiscountsFeeComparisons = cd.getCreditsDiscountsFeeComparisons();
            if (CollectionUtils.isEmpty(creditsDiscountsFeeComparisons)) {
                return true;
            }
            for (CreditsDiscountsFeeComparison comparison : creditsDiscountsFeeComparisons) {
                if (comparison.getCreditDiscountFeeOne().equals(comparison.getCreditDiscountFeeTwo())) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<PowerSource> getPowerSources() {
        return maintenanceDataService.getPowerSources();
    }

    public void addTier() {
        RateTier rateTier = null;
        switch (selectedProfile.getTierType()) {
            case USAGE:
                rateTier = new RateUsageTier();
                selectedProfile.addRateTier(rateTier);
                break;
            case DEMAND:
                rateTier = new RateDemandTier();
                selectedProfile.addRateTier(rateTier);
                break;
            case VOLTAGE:
                rateTier = new RateVoltageTier();
                selectedProfile.addRateTier(rateTier);
                break;
            case PRESSURE:
                rateTier = new RatePressureTier();
                selectedProfile.addRateTier(rateTier);
                break;

        }
        rateTier.setRatePlanProfile(selectedProfile);
        final List<RateTier> activeRateTiers = selectedProfile.getActiveRateTiers();
        final int size = activeRateTiers.size();
        addAncillaryFeesNotPresentAtDbForNewlyCreatedTiers(selectedProfile.getActiveAncillaryFees(), size, rateTier);
        addConsumptionFeesNotPresentAtDbForNewlyCreatedTiers(selectedProfile.getActiveConsumptionFees(), size, rateTier);
    }

    private RateProfileConsumptionFee addConsumptionFee(RateTier rateTier, int groupId) {
        RateProfileConsumptionFee fee = new RateProfileConsumptionFee();
        fee.setRatePlanProfile(selectedProfile);
        fee.setRateTier(rateTier);
        fee.setGroupId(groupId);
        selectedProfile.addConsumptionRate(fee);
        return fee;
    }

    public void removeTier(RateTier tier) {
        boolean referencedTier = invoiceService.existInvoicesLinesForTier(tier);
        if (referencedTier) {
            final int index = selectedProfile.getRateTiers().indexOf(tier);
            final RateTier rateTier = selectedProfile.getRateTiers().get(index);
            rateTier.setActive(false);
            selectedProfile.getAncillaryFees().stream().filter(f -> f.getRateTier().equals(tier)).forEach(f -> f.setActive(false));
            selectedProfile.getConsumptionFees().stream().filter(f -> f.getRateTier().equals(tier)).forEach(f -> f.setActive(false));
        } else {
            selectedProfile.setAncillaryFees(selectedProfile.getAncillaryFees().stream().filter(f -> !f.getRateTier().equals(tier)).collect(Collectors.toList()));
            selectedProfile.setConsumptionFees(selectedProfile.getConsumptionFees().stream().filter(f -> !f.getRateTier().equals(tier)).collect(Collectors.toList()));
            selectedProfile.getRateTiers().remove(tier);
        }
    }

    public List<FeeType> getFeeTypes() {
        return maintenanceDataService.getFeeTypes();
    }


    public List<GasFeeType> getGasFeeTypes() {
        return maintenanceDataService.getGasFeeTypes();

    }


    public List<com.inenergis.entity.maintenanceData.AncillaryFee> getAncillaryFees() {
        return maintenanceDataService.getAncillaryFees();

    }

    public List<TimeOfUseCalendar> getCalendars() {
        return timeOfUseCalendarService.getAll();
    }

    public List<TimeOfUse> getTous(RateConsumptionFeeGroup fee) {
        if (fee.getCalendar() != null) {
            return timeOfUseService.getByCalendar(fee.getCalendar());
        } else {
            return null;
        }
    }

    public List<TimeOfUse> getTous(RateAncillaryFeeGroup fee) {
        if (fee.getCalendar() != null) {
            return timeOfUseService.getByCalendar(fee.getCalendar());
        } else {
            return null;
        }
    }

    public void addConsumptionRateGroup() {
        final List<RateTier> activeRateTiers = selectedProfile.getActiveRateTiers();
        if (activeRateTiers != null) {
            for (RateTier rateTier : activeRateTiers) {
                addConsumptionFee(rateTier, rateConsumptionFeeGroupSequence);
            }
            rateConsumptionFeeGroupSequence++;
        }
    }

    public List<RateConsumptionFeeGroup> getConsumptionFees() {
        List<RateConsumptionFeeGroup> result = new ArrayList<>();
        if (selectedProfile != null) {
            final List<RateProfileConsumptionFee> activeConsumptionFees = selectedProfile.getActiveConsumptionFees();
            if (CollectionUtils.isEmpty(activeConsumptionFees)) {
                return result;
            }
            final List<RateTier> activeRateTiers = selectedProfile.getActiveRateTiers();
            if (CollectionUtils.isEmpty(activeRateTiers)) {
                return result;
            }
            int numberOfTiers = activeRateTiers.size();
            if (numberOfTiers != 0) {
                if (numberOfTiers != 0) {
                    final Map<Integer, List<RateProfileConsumptionFee>> keyFee = activeConsumptionFees.stream().collect(Collectors.groupingBy(RateProfileConsumptionFee::getGroupId));
                    keyFee.entrySet().stream().map(Map.Entry::getValue)
                            .peek(fees -> fees.sort(RateProfileConsumptionFee::compareTo))
                            .map(RateConsumptionFeeGroup::new)
                            .forEach(result::add);
                    result.sort(Comparator.comparing(RateConsumptionFeeGroup::getGroupId));
                }
            }
        }
        return result;
    }

    public void removeConsumptionRate(RateConsumptionFeeGroup feeGroup) {
        final List<RateProfileConsumptionFee> fees = selectedProfile.getConsumptionFees();
        disableUsedFees(fees, feeGroup.getFees());
        selectedProfile.setConsumptionFees(fees);
    }

    private void disableUsedFees(List<? extends RateProfileFee> fees, List<? extends RateProfileFee> groupFees) {
        for (RateProfileFee fee : groupFees) {
            final int index = fees.indexOf(fee);
            if (index == -1) {
                continue;
            }
            final RateProfileFee currentFee = fees.get(index);
            if (currentFee == null) {
                continue;
            }
            if (currentFee.getId() == null || invoiceService.countInvoicesWithFee(currentFee) == 0) {
                fees.remove(fee);
            } else {
                fees.get(index).setActive(false);
            }
        }
    }

    public void addAncillaryFlatFeeGroup() {
        final List<RateTier> activeRateTiers = selectedProfile.getActiveRateTiers();
        if (activeRateTiers != null) {
            for (RateTier rateTier : activeRateTiers) {
                addAncillaryFlatFee(rateTier, rateAncillaryFeeGroupSequence);
            }
            rateAncillaryFeeGroupSequence++;
        }
    }

    private RateProfileAncillaryFee addAncillaryFlatFee(RateTier rateTier, int groupId) {
        RateProfileAncillaryFee fee = new RateProfileAncillaryFee();
        fee.setRatePlanProfile(selectedProfile);
        fee.setRateTier(rateTier);
        fee.setGroupId(groupId);
        selectedProfile.addAncillaryFee(fee);
        return fee;
    }

    public void addAncillaryPercentageFee() {
        RateProfileAncillaryPercentageFee fee = new RateProfileAncillaryPercentageFee();
        fee.setRatePlanProfile(selectedProfile);
        selectedProfile.addAncillaryPercentageFee(fee);
    }

    public List<RateAncillaryFeeGroup> getAncillaryFlatFees() {
        List<RateAncillaryFeeGroup> result = new ArrayList<>();
        if (selectedProfile != null) {
            final List<RateProfileAncillaryFee> activeAncillaryFees = selectedProfile.getActiveAncillaryFees();
            if (CollectionUtils.isEmpty(activeAncillaryFees)) {
                return result;
            }
            final List<RateTier> activeRateTiers = selectedProfile.getActiveRateTiers();
            if (CollectionUtils.isEmpty(activeRateTiers)) {
                return result;
            }
            int numberOfTiers = activeRateTiers.size();
            if (numberOfTiers != 0) {
                final Map<Integer, List<RateProfileAncillaryFee>> keyFee = activeAncillaryFees.stream().collect(Collectors.groupingBy(RateProfileAncillaryFee::getGroupId));
                keyFee.entrySet().stream().map(Map.Entry::getValue)
                        .peek(fees -> fees.sort(RateProfileAncillaryFee::compareTo))
                        .map(RateAncillaryFeeGroup::new)
                        .forEach(result::add);
                result.sort(Comparator.comparing(RateAncillaryFeeGroup::getGroupId));
            }
        }
        return result;
    }

    private void addAncillaryFeesNotPresentAtDbForNewlyCreatedTiers(List<RateProfileAncillaryFee> activeAncillaryFees, int numberOfTiers, RateTier rateTier) {
        final Map<Integer, List<RateProfileAncillaryFee>> feesByName = activeAncillaryFees.stream().collect(Collectors.groupingBy(RateProfileAncillaryFee::getGroupId));
        for (Map.Entry<Integer, List<RateProfileAncillaryFee>> entry : feesByName.entrySet()) {
            if (entry.getValue().size() < numberOfTiers) {
                if (entry.getValue().size() > 0) {
                    final RateProfileAncillaryFee feeToCopy = entry.getValue().get(0);
                    final RateProfileAncillaryFee fee = addAncillaryFlatFee(rateTier, feeToCopy.getGroupId());
                    fee.setName(feeToCopy.getName());
                    fee.setCalendar(feeToCopy.getCalendar());
                    fee.setTimeOfUse(feeToCopy.getTimeOfUse());
                    fee.setFrequency(feeToCopy.getFrequency());
                    fee.setGasFrequency(feeToCopy.getGasFrequency());
                    fee.setRateTier(rateTier);
                }
            }

        }
    }

    private void addConsumptionFeesNotPresentAtDbForNewlyCreatedTiers(List<RateProfileConsumptionFee> activeAncillaryFees, int numberOfTiers, RateTier rateTier) {
        final Map<Integer, List<RateProfileConsumptionFee>> feesByName = activeAncillaryFees.stream().collect(Collectors.groupingBy(RateProfileConsumptionFee::getGroupId));
        for (Map.Entry<Integer, List<RateProfileConsumptionFee>> entry : feesByName.entrySet()) {
            if (entry.getValue().size() < numberOfTiers) {
                if (entry.getValue().size() > 0) {
                    final RateProfileConsumptionFee feeToCopy = entry.getValue().get(0);
                    final RateProfileConsumptionFee fee = addConsumptionFee(rateTier, feeToCopy.getGroupId());
                    fee.setName(feeToCopy.getName());
                    fee.setCalendar(feeToCopy.getCalendar());
                    fee.setTimeOfUse(feeToCopy.getTimeOfUse());
                    fee.setEvent(feeToCopy.getEvent());
                    fee.setRateType(feeToCopy.getRateType());
                    fee.setRateTier(rateTier);
                }
            }
        }
        selectedProfile.getConsumptionFees().sort(Comparator.comparing(RateProfileConsumptionFee::getGroupId));
    }

    public List<RateProfileAncillaryPercentageFee> getAncillaryPercentageFeeGroup() {
        if (selectedProfile != null) {
            return selectedProfile.getActiveAncillaryPercentageFees();
        }
        return new ArrayList<>();
    }

    public void removeAncillaryFee(RateAncillaryFeeGroup feeGroup) {
        final List<RateProfileAncillaryFee> fees = selectedProfile.getAncillaryFees();
        disableUsedFees(fees, feeGroup.getFees());
        selectedProfile.setAncillaryFees(fees);
    }

    public void removeAncillaryFee(RateProfileAncillaryPercentageFee fee) {
        fee.setActive(false);
    }

    public void onRateRowEdit(RowEditEvent editEvent) {
        final RateConsumptionFeeGroup feeGroup = (RateConsumptionFeeGroup) editEvent.getObject();
        if (CollectionUtils.isEmpty(feeGroup.getFees())) {
            return;
        }
        if (repeatedValues(new ArrayList<>(selectedProfile.getActiveConsumptionFees()), new ArrayList<>(feeGroup.getFees()))) {
            uiMessage.addMessage(WARNING_YOU_ARE_ADDING_REPEATED_FEE_GROUPS, FacesMessage.SEVERITY_WARN);
        }
    }

    public void onRateAncillaryRowEdit(RowEditEvent editEvent) {
        final RateAncillaryFeeGroup feeGroup = (RateAncillaryFeeGroup) editEvent.getObject();
        if (CollectionUtils.isEmpty(feeGroup.getFees())) {
            return;
        }
        if (repeatedValues(new ArrayList<>(selectedProfile.getActiveAncillaryFees()), new ArrayList<>(feeGroup.getFees()))) {
            uiMessage.addMessage(WARNING_YOU_ARE_ADDING_REPEATED_FEE_GROUPS, FacesMessage.SEVERITY_WARN);
        }
    }
    public static boolean repeatedValues(List<RateProfileFee> activeFees, List<RateProfileFee> newFees) {
        if (CollectionUtils.isEmpty(activeFees)) {
            return false;
        }
        final RateProfileFee firstFee = newFees.get(0);
        if (activeFees.stream().filter(fee -> newFees.stream().noneMatch(fee::equals))//remove new fees from active fees
                .anyMatch(fee -> fee.shouldBeInTheSameGroup(firstFee))) { //search if is there any repeated
            return true;
        }
        return false;
    }

    public void onAncillaryPercentageFeeRowEdit(RowEditEvent editInitEvent) {
        RateProfileAncillaryPercentageFee fee = (RateProfileAncillaryPercentageFee) editInitEvent.getObject();
        if (getAncillaryPercentageFeeGroup().stream().filter(f -> ! fee.equals(f)).anyMatch(f -> f.isEquivalentTo(fee))) {
            uiMessage.addMessage(WARNING_YOU_ARE_ADDING_REPEATED_FEE_GROUPS, FacesMessage.SEVERITY_WARN);
        }
        (fee).setApplicableFees(fee.getApplicableFeesTransient());
    }
}