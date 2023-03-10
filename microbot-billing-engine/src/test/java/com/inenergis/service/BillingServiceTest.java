package com.inenergis.service;

import com.inenergis.billingEngine.service.InvoiceService;
import com.inenergis.billingEngine.service.RateProfileConsumptionFeeService;
import com.inenergis.billingEngine.service.TimeOfUseCalendarService;
import com.inenergis.entity.Event;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.entity.genericEnum.RateConsumptionFeeType;
import com.inenergis.entity.genericEnum.RateEventFee;
import com.inenergis.entity.genericEnum.SeasonTOU;
import com.inenergis.entity.genericEnum.TierBoundType;
import com.inenergis.entity.genericEnum.TierOperatorType;
import com.inenergis.entity.genericEnum.TierPenaltyPeriod;
import com.inenergis.entity.genericEnum.TierType;
import com.inenergis.entity.genericEnum.TierVariableType;
import com.inenergis.entity.genericEnum.TimeOfUseDayType;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.masterCalendar.TimeOfUseDay;
import com.inenergis.entity.masterCalendar.TimeOfUseHour;
import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.RatePlanProfile;
import com.inenergis.entity.program.rateProgram.RateDemandTier;
import com.inenergis.entity.program.rateProgram.RateProfileConsumptionFee;
import com.inenergis.entity.program.rateProgram.RateTier;
import com.inenergis.entity.program.rateProgram.RateUsageTier;
import com.inenergis.billingEngine.exception.InvoiceCalculationException;
import com.inenergis.billingEngine.service.billing.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BillingServiceTest {

    @Mock
    private TimeOfUse timeOfUse;

    @Mock
    private TimeOfUseCalendar timeOfUseCalendar;

    @Mock
    private RateProfileConsumptionFee consumptionFee;

    @Mock
    private TimeOfUseCalendarService timeOfUseCalendarService;

    @Mock
    private RateProfileConsumptionFeeService rateProfileConsumptionFeeService;

    @Mock
    private RateTier rateTier;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private Program program;

    @Mock
    private RatePlanProfile rateProfile;

    @Mock
    private ServicePoint servicePoint;

    @Mock
    private TimeOfUseCalendar calendar;

    @Mock
    private Invoice invoice;

    @InjectMocks
    private BillingService billingService;

    private IntervalData meterData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        meterData = new IntervalData();
        meterData.setValue(BigDecimal.ONE);
        meterData.setTime("201501250900");

        Mockito.when(invoiceService.getTotalConsumptionByInvoice(Mockito.any())).thenReturn(BigDecimal.ONE);
        Mockito.when(timeOfUseCalendar.getName()).thenReturn("Winter");
        Mockito.when(timeOfUse.getTou()).thenReturn(SeasonTOU.OFF_PEAK);
        Mockito.when(timeOfUse.getTimeOfUseCalendar()).thenReturn(timeOfUseCalendar);
        Mockito.when(timeOfUse.getTimeOfUseHours()).thenReturn(Collections.singletonList(new TimeOfUseHour(9, timeOfUse)));
        Mockito.when(timeOfUse.getTimeOfUseDays()).thenReturn(Collections.singletonList(new TimeOfUseDay(TimeOfUseDayType.WEEK_ENDS, timeOfUse)));
        Mockito.when(consumptionFee.getTimeOfUse()).thenReturn(timeOfUse);
        Mockito.when(consumptionFee.getEvent()).thenReturn(RateEventFee.PDP);
        Mockito.when(consumptionFee.getPrice()).thenReturn(BigDecimal.valueOf(0.79));
        Mockito.when(rateTier.getFromBound()).thenReturn(TierBoundType.AMOUNT);
        Mockito.when(rateTier.getFromAmountValue()).thenReturn(BigDecimal.valueOf(1));
        Mockito.when(rateTier.getPenaltyAmount()).thenReturn(BigDecimal.valueOf(5.24));
        Mockito.when(rateTier.getPenaltyPeriod()).thenReturn(TierPenaltyPeriod.PER_INVOICE);
        Mockito.when(rateTier.getToAmountValue()).thenReturn(BigDecimal.valueOf(200));
        Mockito.when(consumptionFee.getRateTier()).thenReturn(rateTier);
        Mockito.when(program.getName()).thenReturn("PDP");
        Event event = new Event();
        event.setProgram(program);
//        Mockito.when(eventService.findByDate(Mockito.any())).thenReturn(event);
        Mockito.when(timeOfUseCalendar.getTimeOfUses()).thenReturn(Collections.singletonList(timeOfUse));
        Mockito.when(timeOfUseCalendarService.findByDate(Mockito.any())).thenReturn(Arrays.asList(timeOfUseCalendar));
        Mockito.when(timeOfUseCalendarService.getTimeOfUse(Mockito.any(), Mockito.any())).thenReturn(timeOfUse);
        Mockito.when(rateProfileConsumptionFeeService.findBy(Mockito.any(RateConsumptionFeeType.class), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Collections.singletonList(consumptionFee));
    }

//    @Test
//    void processMeterDataNull() {
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> billingService.processMeterData(null, null, map));
//        assertEquals("IntervalData must not be null", exception.getMessage());
//    }
//
//    @Test
//    void processMeterDataNoValue() {
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> billingService.processMeterData(new IntervalData(), null, map));
//        assertEquals("MeterData value must be specified", exception.getMessage());
//    }
//
//    @Test
//    void processMeterDataNoDate() {
//        meterData.setTime(null);
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> billingService.processMeterData(meterData, null, map));
//        assertEquals("MeterData date must be specified", exception.getMessage());
//    }
//
//    @Test
//    void processMeterDataInvalidDate() {
//        meterData.setTime("wrong_date_time");
//        assertThrows(DateTimeParseException.class, () -> billingService.processMeterData(meterData, null, map));
//    }
//
//    @Test
//    void processMeterDataReturnsObject() {
//        assertNotNull(billingService.processMeterData(meterData, null));
//    }
//
//    @Test
//    void processMeterDataWithDebit() {
//        meterData.setValue(BigDecimal.valueOf(10.25));
//        assertEquals(RateConsumptionFeeType.DEBIT, billingService.processMeterData(meterData, null).getType());
//    }
//
//    @Test
//    void processMeterDataWithCredit() {
//        meterData.setValue(BigDecimal.valueOf(-0.025));
//        assertEquals(RateConsumptionFeeType.CREDIT, billingService.processMeterData(meterData, null).getType());
//    }
//
//    @Test
//    void processMeterDataWithTouOffPeak() {
//        RateCharge rateCharge = billingService.processMeterData(meterData, null);
//        assertEquals(SeasonTOU.OFF_PEAK, rateCharge.getTimeOfUse().getTou());
//    }
//
//    @Test
//    void processMeterDataWithWinterCalendar() {
//        RateCharge rateCharge = billingService.processMeterData(meterData, null);
//        assertNotNull(rateCharge.getTimeOfUse().getTimeOfUseCalendar());
//        assertEquals("Winter", rateCharge.getTimeOfUse().getTimeOfUseCalendar().getName());
//    }
//
//    @Test
//    void processMeterDataWithEvent() {
//        billingService.processMeterData(meterData, null);
//        Mockito.verify(eventService).findRateEventFee(Mockito.any());
//    }
//
//    @Test
//    void processMeterDataLoadCharges() {
//        RateCharge rateCharge = billingService.processMeterData(meterData, null);
//        assertTrue(rateCharge.getConsumptionFees().size() > 0);
//    }

    @Test
    void calculateUsageTier() throws InvoiceCalculationException {
        final List<RateUsageTier> rateUsageTiers = Arrays.asList(new RateUsageTier(), new RateUsageTier(), new RateUsageTier());

        populateTier(1L,rateUsageTiers.get(0), new BigDecimal(7),new BigDecimal(1),new BigDecimal(99));
        populateTier(2L,rateUsageTiers.get(1), new BigDecimal(7),new BigDecimal(100),new BigDecimal(199));
        populateTier(3L,rateUsageTiers.get(2), new BigDecimal(7),new BigDecimal(200),new BigDecimal(300));

        Set<RateTier> rateTiers = new HashSet<>(rateUsageTiers);
        rateUsageTiers.get(2).setToBound(TierBoundType.UNLIMITED);
        Mockito.when(rateProfile.getTierType()).thenReturn(TierType.USAGE);
        final BigDecimal highestConsumption = new BigDecimal(203);
        final BigDecimal accumulatedConsumption = new BigDecimal(203);
        final BigDecimal value = new BigDecimal(203);
        final Map<RateTier, BigDecimal> res = billingService.calculateTier(rateProfile, rateTiers, highestConsumption, accumulatedConsumption, servicePoint, value, calendar, invoice);

        assertEquals( 99D,res.get(rateUsageTiers.get(0)).doubleValue());
        assertEquals( 100,res.get(rateUsageTiers.get(1)).doubleValue());
        assertEquals( 4D,res.get(rateUsageTiers.get(2)).doubleValue());
    }

    @Test
    void calculateDemandTier() throws InvoiceCalculationException {
        final List<RateDemandTier> rateDemandTiers = Arrays.asList(new RateDemandTier(), new RateDemandTier(), new RateDemandTier());

        populateTier(1L,rateDemandTiers.get(0), new BigDecimal(7),new BigDecimal(1),new BigDecimal(99));
        populateTier(2L,rateDemandTiers.get(1), new BigDecimal(7),new BigDecimal(100),new BigDecimal(199));
        populateTier(3L,rateDemandTiers.get(2), new BigDecimal(7),new BigDecimal(200),new BigDecimal(300));

        Set<RateTier> rateTiers = new HashSet<>(rateDemandTiers);
        rateDemandTiers.get(2).setToBound(TierBoundType.UNLIMITED);
        Mockito.when(rateProfile.getTierType()).thenReturn(TierType.DEMAND);
        final BigDecimal highestConsumption = new BigDecimal(203);
        final BigDecimal accumulatedConsumption = new BigDecimal(203);
        final BigDecimal value = new BigDecimal(203);
        final Map<RateTier, BigDecimal> res = billingService.calculateTier(rateProfile, rateTiers, highestConsumption, accumulatedConsumption, servicePoint, value, calendar, invoice);

        assertEquals( 203D,res.get(rateDemandTiers.get(2)).doubleValue());
    }

    private void populateTier(Long id, RateTier rateUsageTier, BigDecimal penaltyAmount, BigDecimal fromAmount, BigDecimal toAmount) {
        rateUsageTier.setId(id);
        rateUsageTier.setPenaltyAmount(penaltyAmount);
        rateUsageTier.setFromBound(TierBoundType.AMOUNT);
        rateUsageTier.setToBound(TierBoundType.AMOUNT);
        rateUsageTier.setFromAmountValue(fromAmount);
        rateUsageTier.setToAmountValue(toAmount);
        rateUsageTier.setToFormulaVariable(TierVariableType.BASELINE_ALLOWANCE);
        rateUsageTier.setToFormulaOperator(TierOperatorType.PLUS);
        rateUsageTier.setRatePlanProfile(rateProfile);
        rateUsageTier.setName(" Tier from "+rateUsageTier.getFromAmountValue()+" to "+rateUsageTier.getToAmountValue());
    }

}