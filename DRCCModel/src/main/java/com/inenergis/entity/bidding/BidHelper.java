package com.inenergis.entity.bidding;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Event;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoHoliday;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EnergyUtil;
import com.inenergis.util.RiskCalculator;
import org.apache.cxf.common.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public final class BidHelper {
    private BidHelper() {
    }

    public static final String SEGMENT_NAME = "Segment ";

    public static Bid createBid(IsoResource isoResource, Date tradeDate, boolean hasOutage, List<Event> eventsThisYear) {
        Bid bid = new Bid(isoResource, tradeDate);
        HashMap<Program, MeterForecast> programs = new LinkedHashMap<>();
        BidHelper.buildForecastByProgram(tradeDate, bid, programs);
        BidHelper.createSegments(bid, programs);

        assignRisks(bid, eventsThisYear);

        assignStatus(hasOutage, bid);

        return bid;
    }

    public static void assignStatus(boolean hasOutage, Bid bid) {
        if (hasOutage) {
            bid.setStatus(BidStatus.OUTAGE);
        } else {
            boolean autoBidLowRisk = bid.getIsoResource().getIsoProduct().isAutoBidLowRisk();
            bid.setStatus(autoBidLowRisk && bid.getProperties().allRisksAreLow() ? BidStatus.AUTO_BID : BidStatus.ACTION_REQUIRED);
        }
    }

    public static void assignRisks(Bid bid, List<Event> eventsThisYear) {
        RiskCalculator riskCalculator = new RiskCalculator();
        bid.getProperties().setCapacityRisk(riskCalculator.validateCapacity(bid).getRisk());
        bid.getProperties().setProgramRisk(riskCalculator.validateProgram(bid, eventsThisYear));
        bid.getProperties().setCustomerFatigueRisk(riskCalculator.validateFatigue(bid, eventsThisYear));
    }

    public static void buildForecastByProgram(Date tradeDate, Bid bid, HashMap<Program, MeterForecast> programs) {
        for (LocationSubmissionStatus locationSubmissionStatus : bid.getRegistration().getLocations()) {
            if (locationSubmissionStatus.isExhausted()) {
                continue;
            }
            Program programKey = locationSubmissionStatus.getProgram();
            MeterForecast locationsCombinedForecast;
            if (programs.containsKey(programKey)) {
                locationsCombinedForecast = programs.get(programKey);
            } else {
                locationsCombinedForecast = new MeterForecast();
                programs.put(programKey, locationsCombinedForecast);
            }
            sumTotalHECapacityByLocation(locationSubmissionStatus, locationsCombinedForecast, tradeDate);
        }
    }

    public static List<BaseServiceAgreement> getReadyServiceAgreements(List<ProgramServiceAgreementEnrollment> enrollments) {
        return enrollments.stream().map(e -> e.getLastLocation())
                .filter(Objects::nonNull)
                .filter(l -> !l.isExhausted()).map(l -> l.getProgramServiceAgreementEnrollment().getServiceAgreement())
                .collect(Collectors.toList());
    }
    public static ProgramServiceAgreementEnrollment getActiveEnrollment(ServiceAgreement serviceAgreement) {
        final List<ProgramServiceAgreementEnrollment> activeEnrollments = serviceAgreement.getEnrollments()
                .stream().map(e -> e.getLastLocation())
                .filter(Objects::nonNull)
                .filter(l -> !l.isExhausted()).map(l -> l.getProgramServiceAgreementEnrollment()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(activeEnrollments)) {
            return activeEnrollments.get(0);
        }
        return null;
    }

    public static void createSegments(Bid bid, HashMap<Program, MeterForecast> programs) {
        for (Map.Entry<Program, MeterForecast> programMeterForecastEntry : programs.entrySet()) {
            Segment segment1 = new Segment();
            populateSegment(segment1, bid, 1, programMeterForecastEntry.getKey(), programMeterForecastEntry.getValue());
            Segment segment2 = new Segment();
            populateSegment(segment2, bid, 2, programMeterForecastEntry.getKey(), programMeterForecastEntry.getValue());
        }
    }

    private static void sumTotalHECapacityByLocation(LocationSubmissionStatus locationSubmissionStatus, MeterForecast forecast, Date tradeDate) {
        forecast.setHourEnd1(forecast.getHourEnd1() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_1));
        forecast.setHourEnd2(forecast.getHourEnd2() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_2));
        forecast.setHourEnd3(forecast.getHourEnd3() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_3));
        forecast.setHourEnd4(forecast.getHourEnd4() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_4));
        forecast.setHourEnd5(forecast.getHourEnd5() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_5));
        forecast.setHourEnd6(forecast.getHourEnd6() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_6));
        forecast.setHourEnd7(forecast.getHourEnd7() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_7));
        forecast.setHourEnd8(forecast.getHourEnd8() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_8));
        forecast.setHourEnd9(forecast.getHourEnd9() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_9));
        forecast.setHourEnd10(forecast.getHourEnd10() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_10));
        forecast.setHourEnd11(forecast.getHourEnd11() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_11));
        forecast.setHourEnd12(forecast.getHourEnd12() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_12));
        forecast.setHourEnd13(forecast.getHourEnd13() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_13));
        forecast.setHourEnd14(forecast.getHourEnd14() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_14));
        forecast.setHourEnd15(forecast.getHourEnd15() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_15));
        forecast.setHourEnd16(forecast.getHourEnd16() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_16));
        forecast.setHourEnd17(forecast.getHourEnd17() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_17));
        forecast.setHourEnd18(forecast.getHourEnd18() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_18));
        forecast.setHourEnd19(forecast.getHourEnd19() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_19));
        forecast.setHourEnd20(forecast.getHourEnd20() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_20));
        forecast.setHourEnd21(forecast.getHourEnd21() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_21));
        forecast.setHourEnd22(forecast.getHourEnd22() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_22));
        forecast.setHourEnd23(forecast.getHourEnd23() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_23));
        forecast.setHourEnd24(forecast.getHourEnd24() + locationSubmissionStatus.getCalculatedCapacity(tradeDate, HourEnd.HE_24));
    }

    private static void populateSegment(Segment segment, Bid bid, int indName, Program program, MeterForecast meterForecast) {
        segment.setBid(bid);
        segment.setProgram(program);
        segment.setName(SEGMENT_NAME + indName);
        segment.setStatus(BidStatus.ACTION_REQUIRED.getName());// TODO For the moment the same as Bid
        assignCalculatedValues(meterForecast, segment);
        if (segment.getProgram().getActiveProfile() != null) {
            assignPrices(segment.getProgram().getActiveProfile(), segment);
        }
        if (indName > 1) {
            applySafetyFactors(segment);
        } else {
            applyPMinToCapacity(segment);
        }
        bid.getSegments().add(segment);
    }

    public static void assignCalculatedValues(MeterForecast programForecast, Segment segment) {
        if (programForecast != null) {
            assignForecastedCapacities(programForecast, segment);
            if (segment.getProgram().getActiveProfile() != null) {
                assignSafetyFactors(segment.getProgram().getActiveProfile(), segment);
            }
        }
    }

    private static void assignForecastedCapacities(MeterForecast forecast, Segment segment) {
        segment.setForecastedCapacityHe1(forecast.getHourEnd1());
        segment.setForecastedCapacityHe2(forecast.getHourEnd2());
        segment.setForecastedCapacityHe3(forecast.getHourEnd3());
        segment.setForecastedCapacityHe4(forecast.getHourEnd4());
        segment.setForecastedCapacityHe5(forecast.getHourEnd5());
        segment.setForecastedCapacityHe6(forecast.getHourEnd6());
        segment.setForecastedCapacityHe7(forecast.getHourEnd7());
        segment.setForecastedCapacityHe8(forecast.getHourEnd8());
        segment.setForecastedCapacityHe9(forecast.getHourEnd9());
        segment.setForecastedCapacityHe10(forecast.getHourEnd10());
        segment.setForecastedCapacityHe11(forecast.getHourEnd11());
        segment.setForecastedCapacityHe12(forecast.getHourEnd12());
        segment.setForecastedCapacityHe13(forecast.getHourEnd13());
        segment.setForecastedCapacityHe14(forecast.getHourEnd14());
        segment.setForecastedCapacityHe15(forecast.getHourEnd15());
        segment.setForecastedCapacityHe16(forecast.getHourEnd16());
        segment.setForecastedCapacityHe17(forecast.getHourEnd17());
        segment.setForecastedCapacityHe18(forecast.getHourEnd18());
        segment.setForecastedCapacityHe19(forecast.getHourEnd19());
        segment.setForecastedCapacityHe20(forecast.getHourEnd20());
        segment.setForecastedCapacityHe21(forecast.getHourEnd21());
        segment.setForecastedCapacityHe22(forecast.getHourEnd22());
        segment.setForecastedCapacityHe23(forecast.getHourEnd23());
        segment.setForecastedCapacityHe24(forecast.getHourEnd24());
    }

    private static void assignSafetyFactors(ProgramProfile programProfile, Segment segment) {
        segment.setSafetyFactorHe1(pickSafetyFactorByDayOfWeek(programProfile, 0, segment));
        segment.setSafetyFactorHe2(pickSafetyFactorByDayOfWeek(programProfile, 1, segment));
        segment.setSafetyFactorHe3(pickSafetyFactorByDayOfWeek(programProfile, 2, segment));
        segment.setSafetyFactorHe4(pickSafetyFactorByDayOfWeek(programProfile, 3, segment));
        segment.setSafetyFactorHe5(pickSafetyFactorByDayOfWeek(programProfile, 4, segment));
        segment.setSafetyFactorHe6(pickSafetyFactorByDayOfWeek(programProfile, 5, segment));
        segment.setSafetyFactorHe7(pickSafetyFactorByDayOfWeek(programProfile, 6, segment));
        segment.setSafetyFactorHe8(pickSafetyFactorByDayOfWeek(programProfile, 7, segment));
        segment.setSafetyFactorHe9(pickSafetyFactorByDayOfWeek(programProfile, 8, segment));
        segment.setSafetyFactorHe10(pickSafetyFactorByDayOfWeek(programProfile, 9, segment));
        segment.setSafetyFactorHe11(pickSafetyFactorByDayOfWeek(programProfile, 10, segment));
        segment.setSafetyFactorHe12(pickSafetyFactorByDayOfWeek(programProfile, 11, segment));
        segment.setSafetyFactorHe13(pickSafetyFactorByDayOfWeek(programProfile, 12, segment));
        segment.setSafetyFactorHe14(pickSafetyFactorByDayOfWeek(programProfile, 13, segment));
        segment.setSafetyFactorHe15(pickSafetyFactorByDayOfWeek(programProfile, 14, segment));
        segment.setSafetyFactorHe16(pickSafetyFactorByDayOfWeek(programProfile, 15, segment));
        segment.setSafetyFactorHe17(pickSafetyFactorByDayOfWeek(programProfile, 16, segment));
        segment.setSafetyFactorHe18(pickSafetyFactorByDayOfWeek(programProfile, 17, segment));
        segment.setSafetyFactorHe19(pickSafetyFactorByDayOfWeek(programProfile, 18, segment));
        segment.setSafetyFactorHe20(pickSafetyFactorByDayOfWeek(programProfile, 19, segment));
        segment.setSafetyFactorHe21(pickSafetyFactorByDayOfWeek(programProfile, 20, segment));
        segment.setSafetyFactorHe22(pickSafetyFactorByDayOfWeek(programProfile, 21, segment));
        segment.setSafetyFactorHe23(pickSafetyFactorByDayOfWeek(programProfile, 22, segment));
        segment.setSafetyFactorHe24(pickSafetyFactorByDayOfWeek(programProfile, 23, segment));
    }

    private static long pickSafetyFactorByDayOfWeek(ProgramProfile programProfile, int safetyFactorIndex, Segment segment) {
        Date tradeDate = segment.getBid().getTradeDate();
        LocalDate date = tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        IsoResource isoResource = segment.getBid().getIsoResource();
        return pickSafetyFactorByDayOfWeek(programProfile, safetyFactorIndex, date, isoResource);
    }

    public static long pickSafetyFactorByDayOfWeek(ProgramProfile programProfile, int safetyFactorIndex, LocalDate date, IsoResource isoResource) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<SafetyReductionFactorHe> safetyReductionFactors = programProfile.getSafetyReductionFactors();
        if (CollectionUtils.isEmpty(safetyReductionFactors)) {
            return 0L;
        }
        SafetyReductionFactorHe safetyReductionFactorHe = safetyReductionFactors.get(safetyFactorIndex);
        if (isoResource != null  && isoResource.getIsoProduct() != null) {
            for (IsoHoliday isoHoliday : isoResource.getIsoProduct().getProfile().getHolidays()) {
                if (isoHoliday.getDate() != null && isOnHoliday(isoHoliday.getDate(), date)) {
                    return safetyReductionFactorHe.getProgramHoliday();
                }
            }
        }
        switch (dayOfWeek) {
            case MONDAY:
                return safetyReductionFactorHe.getMonday();
            case TUESDAY:
                return safetyReductionFactorHe.getTuesday();
            case WEDNESDAY:
                return safetyReductionFactorHe.getWednesday();
            case THURSDAY:
                return safetyReductionFactorHe.getThursday();
            case FRIDAY:
                return safetyReductionFactorHe.getFriday();
            case SATURDAY:
                return safetyReductionFactorHe.getSaturday();
            case SUNDAY:
                return safetyReductionFactorHe.getSunday();
            default:
                return 0L;
        }
    }

    private static boolean isOnHoliday(Date holidayDate, LocalDate tradeDate) {
        LocalDate localHolidayDate = holidayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return tradeDate.isEqual(localHolidayDate);
    }

    private static void assignPrices(ProgramProfile activeProfile, Segment segment) {
        HourEnd hourEnd = activeProfile.getDefaultBidHoursFrom();
        if (hourEnd != null) {
            if (HourEnd.HE_1.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_1.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe1(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_2.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_2.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe2(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_3.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_3.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe3(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_4.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_4.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe4(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_5.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_5.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe5(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_6.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_6.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe6(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_7.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_7.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe7(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_8.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_8.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe8(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_9.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_9.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe9(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_10.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_10.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe10(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_11.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_11.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe11(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_12.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_12.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe12(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_13.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_13.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe13(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_14.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_14.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe14(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_15.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_15.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe15(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_16.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_16.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe16(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_17.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_17.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe17(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_18.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_18.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe18(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_19.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_19.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe19(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_20.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_20.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe20(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_21.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_21.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe21(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_22.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_22.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe22(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_23.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_23.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe23(activeProfile.getDefaultBidPrice());
            }
            if (HourEnd.HE_24.getHourNumber() >= activeProfile.getDefaultBidHoursFrom().getHourNumber() && HourEnd.HE_24.getHourNumber() <= activeProfile.getDefaultBidHoursTo().getHourNumber()) {
                segment.setPriceHe24(activeProfile.getDefaultBidPrice());
            }
        } else if (activeProfile.getDefaultBidPrice() != null) {
            segment.setPriceHe1(activeProfile.getDefaultBidPrice());
            segment.setPriceHe2(activeProfile.getDefaultBidPrice());
            segment.setPriceHe3(activeProfile.getDefaultBidPrice());
            segment.setPriceHe4(activeProfile.getDefaultBidPrice());
            segment.setPriceHe5(activeProfile.getDefaultBidPrice());
            segment.setPriceHe6(activeProfile.getDefaultBidPrice());
            segment.setPriceHe7(activeProfile.getDefaultBidPrice());
            segment.setPriceHe8(activeProfile.getDefaultBidPrice());
            segment.setPriceHe9(activeProfile.getDefaultBidPrice());
            segment.setPriceHe10(activeProfile.getDefaultBidPrice());
            segment.setPriceHe11(activeProfile.getDefaultBidPrice());
            segment.setPriceHe12(activeProfile.getDefaultBidPrice());
            segment.setPriceHe13(activeProfile.getDefaultBidPrice());
            segment.setPriceHe14(activeProfile.getDefaultBidPrice());
            segment.setPriceHe15(activeProfile.getDefaultBidPrice());
            segment.setPriceHe16(activeProfile.getDefaultBidPrice());
            segment.setPriceHe17(activeProfile.getDefaultBidPrice());
            segment.setPriceHe18(activeProfile.getDefaultBidPrice());
            segment.setPriceHe19(activeProfile.getDefaultBidPrice());
            segment.setPriceHe20(activeProfile.getDefaultBidPrice());
            segment.setPriceHe21(activeProfile.getDefaultBidPrice());
            segment.setPriceHe22(activeProfile.getDefaultBidPrice());
            segment.setPriceHe23(activeProfile.getDefaultBidPrice());
            segment.setPriceHe24(activeProfile.getDefaultBidPrice());
        }

    }

    private static Long applyPercentage(Long lBase, Long lPct) {
        if (lBase == 0L || lPct == 0L) {
            return lBase;
        }
        BigDecimal base = BigDecimal.valueOf(lBase);
        BigDecimal pct = BigDecimal.valueOf(lPct).divide(ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL); //The number is stored multiplied with a given precision
        BigDecimal result = base.multiply(ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL.subtract(pct)).divide(ConstantsProviderModel.ONE_HUNDRED_BIG_DECIMAL);
        return result.longValue();
    }

    private static void applySafetyFactors(Segment segment) {
        segment.setCapacityHe1(applyPercentage(segment.getForecastedCapacityHe1(), segment.getSafetyFactorHe1()));
        segment.setCapacityHe2(applyPercentage(segment.getForecastedCapacityHe2(), segment.getSafetyFactorHe2()));
        segment.setCapacityHe3(applyPercentage(segment.getForecastedCapacityHe3(), segment.getSafetyFactorHe3()));
        segment.setCapacityHe4(applyPercentage(segment.getForecastedCapacityHe4(), segment.getSafetyFactorHe4()));
        segment.setCapacityHe5(applyPercentage(segment.getForecastedCapacityHe5(), segment.getSafetyFactorHe5()));
        segment.setCapacityHe6(applyPercentage(segment.getForecastedCapacityHe6(), segment.getSafetyFactorHe6()));
        segment.setCapacityHe7(applyPercentage(segment.getForecastedCapacityHe7(), segment.getSafetyFactorHe7()));
        segment.setCapacityHe8(applyPercentage(segment.getForecastedCapacityHe8(), segment.getSafetyFactorHe8()));
        segment.setCapacityHe9(applyPercentage(segment.getForecastedCapacityHe9(), segment.getSafetyFactorHe9()));
        segment.setCapacityHe10(applyPercentage(segment.getForecastedCapacityHe10(), segment.getSafetyFactorHe10()));
        segment.setCapacityHe11(applyPercentage(segment.getForecastedCapacityHe11(), segment.getSafetyFactorHe11()));
        segment.setCapacityHe12(applyPercentage(segment.getForecastedCapacityHe12(), segment.getSafetyFactorHe12()));
        segment.setCapacityHe13(applyPercentage(segment.getForecastedCapacityHe13(), segment.getSafetyFactorHe13()));
        segment.setCapacityHe14(applyPercentage(segment.getForecastedCapacityHe14(), segment.getSafetyFactorHe14()));
        segment.setCapacityHe15(applyPercentage(segment.getForecastedCapacityHe15(), segment.getSafetyFactorHe15()));
        segment.setCapacityHe16(applyPercentage(segment.getForecastedCapacityHe16(), segment.getSafetyFactorHe16()));
        segment.setCapacityHe17(applyPercentage(segment.getForecastedCapacityHe17(), segment.getSafetyFactorHe17()));
        segment.setCapacityHe18(applyPercentage(segment.getForecastedCapacityHe18(), segment.getSafetyFactorHe18()));
        segment.setCapacityHe19(applyPercentage(segment.getForecastedCapacityHe19(), segment.getSafetyFactorHe19()));
        segment.setCapacityHe20(applyPercentage(segment.getForecastedCapacityHe20(), segment.getSafetyFactorHe20()));
        segment.setCapacityHe21(applyPercentage(segment.getForecastedCapacityHe21(), segment.getSafetyFactorHe21()));
        segment.setCapacityHe22(applyPercentage(segment.getForecastedCapacityHe22(), segment.getSafetyFactorHe22()));
        segment.setCapacityHe23(applyPercentage(segment.getForecastedCapacityHe23(), segment.getSafetyFactorHe23()));
        segment.setCapacityHe24(applyPercentage(segment.getForecastedCapacityHe24(), segment.getSafetyFactorHe24()));
    }

    private static void applyPMinToCapacity(Segment segment) {
        long pMin = segment.bid.getIsoResource().getActivePmaxPmin().getPmin();
        long pMinWatts = EnergyUtil.convertToWatts(pMin, ElectricalUnit.MW);

        segment.setCapacityHe1(pMinWatts);
        segment.setCapacityHe2(pMinWatts);
        segment.setCapacityHe3(pMinWatts);
        segment.setCapacityHe4(pMinWatts);
        segment.setCapacityHe5(pMinWatts);
        segment.setCapacityHe6(pMinWatts);
        segment.setCapacityHe7(pMinWatts);
        segment.setCapacityHe8(pMinWatts);
        segment.setCapacityHe9(pMinWatts);
        segment.setCapacityHe10(pMinWatts);
        segment.setCapacityHe11(pMinWatts);
        segment.setCapacityHe12(pMinWatts);
        segment.setCapacityHe13(pMinWatts);
        segment.setCapacityHe14(pMinWatts);
        segment.setCapacityHe15(pMinWatts);
        segment.setCapacityHe16(pMinWatts);
        segment.setCapacityHe17(pMinWatts);
        segment.setCapacityHe18(pMinWatts);
        segment.setCapacityHe19(pMinWatts);
        segment.setCapacityHe20(pMinWatts);
        segment.setCapacityHe21(pMinWatts);
        segment.setCapacityHe22(pMinWatts);
        segment.setCapacityHe23(pMinWatts);
        segment.setCapacityHe24(pMinWatts);
    }
}