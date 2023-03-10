package com.inenergis.controller.general;

import com.inenergis.entity.DataMappingType;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.genericEnum.*;
import com.inenergis.entity.program.*;
import lombok.Getter;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Named
@ApplicationScoped
@Getter
public class ConstantsProvider implements Serializable {

    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(MM_DD_YYYY);
    public static final String THREE_DECIMAL_POSITION_PATTERN = "0.000";
    public static final String TWO_DECIMAL_POSITION_PATTERN = "0.00";
    public static final String NON_EVENT_LOAD = "ref_load";
    public static final String FAULT_FROM_ITRON = "Error received from ITRON: {0}";
    public static final String ERROR_CONNECTING_TO_ITRON = "There was a problem connecting to the Itron API";
    public static final String ICON_VCARD = "icon-account_box";
    public static final String ICON_DOC_TEXT = "icon-sort";
    public static final String ICON_LOCATION_OUTLINE = "icon-location_on";
    public static final String ICON_LINK = "icon-link";
    public static final String ICON_MAIL = "icon-mail_outline";
    public static final String ICON_DOLLAR = "icon-attach_money";
    public static final String ICON_FLAG_1 = "icon-flag";
    public static final String ICON_BELL_2 = "icon-account_balance";
    public static final String ICON_BUILDING = "icon-business";
    public static final String ICON_TAGS = "icon-more";
    public static final String MONEY_TIP_FORMAT = "\\$ %s";
    public static SimpleDateFormat DATE_AXIS_FORMATTER_FOR_CHARTS = new SimpleDateFormat("yyyy-MM-dd");

    public final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    private String CUSTOMER_TIMEZONE = "America/Los_Angeles";

    private USStates[] states = USStates.values();

    private List<String> unenrollmentReasons = Arrays.asList("Customer Requested", "CDW Attribute Change", "Program Decision");
    private List<String> bidCancelReasons = Arrays.asList("Upcoming Weather", "Customer Fatigue", "Program Constraints", "Program Dispatch", "Other");

    private FslRuleComparisonReference[] fslComparisonReferences = FslRuleComparisonReference.values();

    private ElectricalUnit[] electricalUnits = ElectricalUnit.values();
    private EssentialCustomerType[] essentialCustomerTypes = EssentialCustomerType.values();

    private List<String> orAnd = Arrays.asList("or", "and");
    public static final String YES = "Yes";

    public static final String NO = "No";

    public static final DataMappingType[] dataMappingTypes = DataMappingType.values();

    private HourEnd[] hourEnds = HourEnd.values();

    private MinutesOrHours[] minutesOrHours = MinutesOrHours.values();

    private MinutesOrHoursOrDays[] minutesOrHoursOrDays = MinutesOrHoursOrDays.values();
    private DispatchType[] dispatchTypes = DispatchType.values();

    private NotificationType[] notificationTypes = NotificationType.values();
    private Vendor[] vendors = Vendor.values();

    private AwardInstruction[] awardInstructions = AwardInstruction.values();
    private DRMS[] drmss = DRMS.values();
    private DispatchLevel[] dispatchLevels = DispatchLevel.values();
    private DispatchReason[] dispatchReasons = DispatchReason.values();
    private AggregatorDispatchType[] aggregatorDispatchTypes = AggregatorDispatchType.values();
    private EventDurationOption[] eventDurationOptions = EventDurationOption.values();
    private EnrolmentStatus[] enrollmentStatuses = EnrolmentStatus.values();

    private BidStatus[] bidStatuses = BidStatus.values();
    private EventType[] eventTypes = EventType.values();
    private EventStatus[] eventStatuses = EventStatus.values();

    public static final String REST_ERROR_CODE = "Internal error processing request";

    public Date now() {
        return new Date();
    }

    public Date today() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public Date todayCustomerTimeZone() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.of(CUSTOMER_TIMEZONE)).toInstant());
    }

    private HourOfDayAndHalf[] hourOfDayAndHalves = HourOfDayAndHalf.values();
    private BidSubmissionIsoInterval[] bidSubmissionIsoInterval = BidSubmissionIsoInterval.values();
    private BidSubmissionTradeTime[] bidSubmissionTradeTimes = BidSubmissionTradeTime.values();
    private BidSubmissionTradeTimeHours[] bidSubmissionTradeTimeHours = BidSubmissionTradeTimeHours.values();
    private DispatchCancelReason[] dispatchCancelReasons = DispatchCancelReason.values();

    private ProductType[] productTypes = ProductType.values();
    private RetailDispatchScheduleType[] dispatchScheduleTypes = RetailDispatchScheduleType.values();

    private IntervalType[] intervalTypes = IntervalType.values();

    private WorkPlanType[] workPlanTypes = WorkPlanType.values();
    private TaskStatus[] taskInstanceModifiableStatuses = ((TaskStatus[]) Arrays.asList(TaskStatus.IN_PROCESS, TaskStatus.CANCELLED, TaskStatus.COMPLETED).toArray());
    private NotificationDefinitionId[] NOTIFICATION_DEFINITION_IDS = NotificationDefinitionId.values();
    private List<Integer> FORECAST_DAYS = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    private DispatchForecastFormat[] dispatchForecastFormats = DispatchForecastFormat.values();
    private DispatchForecastType[] dispatchForecastTypes = DispatchForecastType.values();
    private DispatchForecastLevel[] dispatchForecastLevels = DispatchForecastLevel.values();
    private RateCodeSector[] rateCodeSectors = RateCodeSector.values();
    private GasRateCodeSector[] gasRateCodeSectors = GasRateCodeSector.values();


    private RatePlanStatus[] ratePlanStatuses = RatePlanStatus.values();
    private SeasonCalendarType[] seasonCalendarTypes = SeasonCalendarType.values();
    private SeasonTOU[] seasonTOUS = SeasonTOU.values();
    private DesignType[] designTypes = DesignType.values();
    //change
    private  ApplicableContractEnuum[] applicableContractEnuums = ApplicableContractEnuum.values();
    private DesignSubType[] designSubTypes = DesignSubType.values();
    private Month[] months = Month.values();
    private Byte[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private WeekOrdinalNumber[] weekOrdinalNumbers = WeekOrdinalNumber.values();
    private DayOfWeek[] weekDays = DayOfWeek.values();
    private TimeType[] timeTypes = TimeType.values();
    private RelationalOperator[] relationalOperators = RelationalOperator.values();
    private DemandType[] demandTypes = DemandType.values();
    private GeneralEligibilityAttributeType[] generalEligibilityAttributeType = GeneralEligibilityAttributeType.values();
    private BillingTermFrequency[] billingTermFrequencies = BillingTermFrequency.values();
    private LocalTime[] timeValuesEvery30Min = {
            LocalTime.of(0, 0), LocalTime.of(0, 30),
            LocalTime.of(1, 0), LocalTime.of(1, 30),
            LocalTime.of(2, 0), LocalTime.of(2, 30),
            LocalTime.of(3, 0), LocalTime.of(3, 30),
            LocalTime.of(4, 0), LocalTime.of(4, 30),
            LocalTime.of(5, 0), LocalTime.of(5, 30),
            LocalTime.of(6, 0), LocalTime.of(6, 30),
            LocalTime.of(7, 0), LocalTime.of(7, 30),
            LocalTime.of(8, 0), LocalTime.of(8, 30),
            LocalTime.of(9, 0), LocalTime.of(9, 30),
            LocalTime.of(10, 0), LocalTime.of(10, 30),
            LocalTime.of(11, 0), LocalTime.of(11, 30),
            LocalTime.of(12, 0), LocalTime.of(12, 30),
            LocalTime.of(13, 0), LocalTime.of(13, 30),
            LocalTime.of(14, 0), LocalTime.of(14, 30),
            LocalTime.of(15, 0), LocalTime.of(15, 30),
            LocalTime.of(16, 0), LocalTime.of(16, 30),
            LocalTime.of(17, 0), LocalTime.of(17, 30),
            LocalTime.of(18, 0), LocalTime.of(18, 30),
            LocalTime.of(19, 0), LocalTime.of(19, 30),
            LocalTime.of(20, 0), LocalTime.of(20, 30),
            LocalTime.of(21, 0), LocalTime.of(21, 30),
            LocalTime.of(22, 0), LocalTime.of(22, 30),
            LocalTime.of(23, 0), LocalTime.of(23, 30)
    };
    private int[] oneToTwentyEight = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    private ActivityStatus[] activityStatuses = ActivityStatus.values();
    private ChargesCategory[] chargeAttributesCategory = ChargesCategory.values();
    private ChargesOption[] chargesOptions = ChargesOption.values();
    private ChargesServiceType[] chargesServiceType = ChargesServiceType.values();
    private ChargesEquipmentType[] chargesEquipmentType = ChargesEquipmentType.values();
    private EventOption[] feesEventOptions = EventOption.values();
    private FeeType[] feeTypes = FeeType.values();
//    private GasFeeType[] gasFeeTypes = GasFeeType.values();
//    private AncillaryGasFeeType[] ancillaryGasFeeTypes = AncillaryGasFeeType.values();

    private FeeFrequency[] feeFrequencies = FeeFrequency.values();
    private FeeableUnit[] feeableUnits = FeeableUnit.values();
    private FeeBaseline[] feeBaselines = FeeBaseline.values();
    private FeeBaselineUnit[] feeBaselineUnits = FeeBaselineUnit.values();
    private DiscountType[] discountTypes = DiscountType.values();
    private PartyRole[] partyRoles = PartyRole.values();
    private EnergyContractFeeType[] energyContractFeeTypes = EnergyContractFeeType.values();
    private EnergyContractCreditType[] energyContractCreditTypes = EnergyContractCreditType.values();
    private FeeIndex[] feeIndices = FeeIndex.values();
    private ChargesFeeComparator[] chargesFeeComparators = ChargesFeeComparator.values();
    private FeeUnitOfMeasure[] feeUnitOfMeasures = FeeUnitOfMeasure.values();
    private AmountType[] amountTypes = AmountType.values();
    private EnergyContractType[] energyContractTypes = EnergyContractType.values();
    private CommodityType[] commodityTypes = CommodityType.values();
    private ComodityType[] comodityTypes = ComodityType.values();
    private CommodityProductType[] commodityProductTypes = CommodityProductType.values();
    private CommodityPowerSource[] commodityPowerSources = CommodityPowerSource.values();
    private CommodityFrequency[] commodityFrequencies = CommodityFrequency.values();
    private RelatedContractType[] relatedContractTypes = RelatedContractType.values();
    private CommodityUnit[] commodityUnits = CommodityUnit.values();
    private DiscountCategory[] discountCategories = DiscountCategory.values();
    private CreditDiscountFrequency[] creditDiscountFrequencies = CreditDiscountFrequency.values();
    private ProgramType[] programTypes = ProgramType.values();
    private String mailListRegexp = "^([_A-Za-z0-9-\\+]+(|\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))+(,([_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})))*$";
    private String mailLRegexp = "^([_A-Za-z0-9-\\+]+(|\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,}))+$";
    private CompanySector[] companySectors = CompanySector.values();
    private PaymentFrequency[] paymentFrequencies = PaymentFrequency.values();
    private ContractAddressType[] contractAddressTypes = ContractAddressType.values();
    private POCType[] pocTypes = POCType.values();
    private String emailValidationMessage = "Must be a valid email address";
    private PhoneType[] phoneTypes = PhoneType.values();
    private RatePlanServiceType[] ratePlanServiceTypes = RatePlanServiceType.values();
    private FeeDetailUnit[] feeDetailUnits = FeeDetailUnit.values();
    private AssetOwnership[] assetOwnerships = AssetOwnership.values();
    private AssetUsage[] assetUsages = AssetUsage.values();
    public static final MaintenanceClass[] maintenanceClasses = MaintenanceClass.values();
    private LinkageType[] linkTypes = LinkageType.values();
    private DemandMinType[] demandMinTypes = DemandMinType.values();
    private AttributeValidation[] attributeValidations = AttributeValidation.values();
    private DeviceLinkType[] deviceLinkTypes = DeviceLinkType.values();
    private TierType[] tierTypes = TierType.values();
    private GasTierType[] gasTierTypes = GasTierType.values();

    private TierPenaltyPeriod[] penaltyPeriods = TierPenaltyPeriod.values();
    private TierDemandInterval[] tierIntervals = TierDemandInterval.values();
    private TierBoundType[] tierBounds = TierBoundType.values();
    private TierOperatorType[] tierFormulaOperators = TierOperatorType.values();
    private TierVariableType[] tierFormulaVariables = TierVariableType.values();
    private GasTierVariableType[] gasfromFormulaVariables = GasTierVariableType.values();



    private RateEventFee[] rateEventFees = RateEventFee.values();
    private PaymentType[] paymentTypes = PaymentType.values();
    private RateConsumptionFeeType[] rateTypes = RateConsumptionFeeType.values();
    private RateAncillaryFrequency[] ancillaryFrequencyTypes = RateAncillaryFrequency.values();
    private GasRateAncillaryFrequency[] gasRateAncillaryFrequencies = GasRateAncillaryFrequency.values();

    private RateAncillaryFeeType[] ancillaryRateTypes = RateAncillaryFeeType.values();
    private AgedInvoiceCategory[] agedInvoiceCategories = AgedInvoiceCategory.values();
    private CapUnit[] capUnits = CapUnit.values();
    private GasCapUnit[] gasCapUnits = GasCapUnit.values();

    private RatePercentageAncillaryApplicability[] ancillaryPercentageApplicabilities = RatePercentageAncillaryApplicability.values();
    private RatePercentageAncillaryType[] ancillaryPercentageTypes = RatePercentageAncillaryType.values();
    private PercentageHierarchyType[] feeHierarchyTypes = PercentageHierarchyType.values();

    public DataMappingType[] getDataMappingTypes() {
        return dataMappingTypes;
    }  // Those methods have been added for the EL expression renderer to be able to resolve the property access

    public MaintenanceClass[] getMaintenanceClasses() {
        return maintenanceClasses;
    }
    public static final Locale LOCALE = new Locale("en", "US");

    private AssetProfileType[] profileTypes = AssetProfileType.values();
}