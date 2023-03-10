package com.inenergis.microbot.camel.services;

import com.inenergis.entity.Event;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTime;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTimeHours;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.microbot.camel.dao.BidDao;
import com.inenergis.microbot.camel.dao.EventDao;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EventDurationValidator;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class BidService {

    private static final Logger logger = LoggerFactory.getLogger(BidService.class);

    @Autowired
    private IsoOutageService isoOutageService;

    @Autowired
    private BidDao bidDao;

    @Autowired
    private EventDao eventDao;

    @Transactional
    public void getNextBids(Exchange exchange) {
        List<RegistrationSubmissionStatus> registrations = (List<RegistrationSubmissionStatus>) exchange.getIn().getBody();
        if (!CollectionUtils.isEmpty(registrations)) {
            LocalDate from = ZonedDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
            LocalDate to = from.plusDays(1);
            List<IsoResource> resources = registrations.stream().map(RegistrationSubmissionStatus::getIsoResource).collect(Collectors.toList());
            List<Bid> bidList = bidDao.findAllByTradeDateBetweenAndIsoResourceIn(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()), resources);
            bidList.forEach(bid -> bid.getSegments().get(0).getProgram().getActiveProfile().getDispatchReasons().isEmpty());
            bidList.forEach(bid -> bid.getIsoResource().getIsoProduct().getProfile().getRiskConditions().isEmpty());
            exchange.getIn().setHeader("bids", bidList);
        } else {
            exchange.getIn().setHeader("bids", new ArrayList<>());
        }
    }

    @Transactional
    public void getPendingBids(Exchange exchange) {
        List<Bid> bids = bidDao.findAllByStatus(BidStatus.SUBMITTED);
        bids.forEach(bid -> bid.getSegments().isEmpty());
        exchange.getIn().setBody(bids);
    }

    @Transactional
    public void getBidsToSend(Exchange exchange) {
        List<BidStatus> statuses = Arrays.asList(BidStatus.AUTO_BID, BidStatus.ACTION_REQUIRED, BidStatus.READY_TO_SUBMIT, BidStatus.ACCEPTED, BidStatus.DELIVERY_ERROR, BidStatus.EXCEPTIONS);
        LocalDate now = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        List<Bid> bids = bidDao.findAllByTradeDateBetweenAndStatusIn(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()), statuses);
        bids.forEach(bid -> bid.getSegments().isEmpty());
        for (Bid bid : bids) {
            if (bid.getStatus().equals(BidStatus.AUTO_BID) || bid.getStatus().equals(BidStatus.ACTION_REQUIRED)) {
                List<Event> eventsThisYear = getAllEventsForCurrentYear(bid.getSegments().get(0).getProgram());
                BidHelper.assignRisks(bid, eventsThisYear);
                BidHelper.assignStatus(false, bid);
            }
        }
        logger.info("{} Potential bids", bids.size());
        bids.removeIf(bid -> !isBidSendable(bid.getRegistration(), now, bid));
        logger.info("{} bids after filtering", bids.size());
        exchange.getIn().setBody(bids);
    }

    public void filterBidsToSave(Exchange exchange) {
        List<RegistrationSubmissionStatus> registrations = (List<RegistrationSubmissionStatus>) exchange.getIn().getBody();
        List<Bid> bids = (List<Bid>) exchange.getIn().getHeader("bids");
        List<Bid> finalBids = new ArrayList<>();
        Map<IsoResource, List<Bid>> bidMap = bids.stream().collect(Collectors.groupingBy(Bid::getIsoResource));
        for (RegistrationSubmissionStatus registration : registrations) {
            List<Bid> registrationBids = bidMap.get(registration.getIsoResource());
            registrationBids = populatePendingBids(registration, registrationBids);
            if (!CollectionUtils.isEmpty(registrationBids)) {
                finalBids.addAll(registrationBids);
            }
        }
        exchange.getIn().setBody(finalBids);
    }

    @Transactional
    public void getAwaitingCreatedBids(Exchange exchange) {
        LocalDate now = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        List<BidStatus> statuses = Arrays.asList(BidStatus.AUTO_BID, BidStatus.ACTION_REQUIRED);

        List<Bid> bids = bidDao.findAllByTradeDateGreaterThanEqualAndStatusInAndDefaultSchedule(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()), statuses, true);

        for (Bid bid : bids) {
            bid.getIsoResource().getIsoProduct().getProfile().getHolidays().isEmpty();
            bid.getIsoResource().getIsoProduct().getProfile().getRiskConditions().isEmpty();
            bid.getIsoResource().getPmaxPminList().isEmpty();
            bid.getIsoResource().getImpactedResources().forEach(event -> event.getEvent().getImpactedCustomers().isEmpty());
            for (LocationSubmissionStatus location : bid.getIsoResource().getActiveRegistration().getLocations()) {
                ProgramProfile activeProfile = location.getProgram().getActiveProfile();
                activeProfile.getSafetyReductionFactors().isEmpty();
                activeProfile.getDispatchReasons().isEmpty();
                activeProfile.getEventDurations().isEmpty();
            }
        }
        exchange.getIn().setBody(bids);
    }

    @Transactional
    @Modifying
    public void resetBid(Exchange exchange) {
        Bid bid = (Bid) exchange.getIn().getBody();
        Long id = bid.getId();
        bid = createBid(bid.getIsoResource().getActiveRegistration(), bid.getTradeLocalDate());
        bid.setId(id);
        bid.setLastReset(new Date());
        bidDao.save(bid);
    }

    private List<Event> getAllEventsForCurrentYear(Program program) {
        return getAllEventsForCurrentYear(Collections.singletonList(program));
    }

    @Transactional
    private List<Event> getAllEventsForCurrentYear(List<Program> programs) {
        LocalDateTime localStartDate = LocalDate.now().withDayOfYear(1).atStartOfDay();
        LocalDateTime localEndDate = localStartDate.plus(1, ChronoUnit.YEARS).minus(1, ChronoUnit.MINUTES);

        Date startDate = Date.from(localStartDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        Date endDate = Date.from(localEndDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());

        return eventDao.findByProgramInAndStartDateGreaterThanEqualAndEndDateLessThanEqual(programs, startDate, endDate);
    }

    private boolean isBidSendable(RegistrationSubmissionStatus registration, LocalDate today, Bid bid) {
        if (registration == null) {
            return false;
        }
        boolean result;
        switch (bid.getStatus()) {
            case AUTO_BID:
                result = checkExpectedDate(bid, registration, today) && checkIsTimeToSend(bid, registration, today) && checkIsoStillAcceptBids(bid, registration, today);
                break;
            case ACCEPTED:
            case EXCEPTIONS:
                result = checkNotSentYet(bid) && checkIsoStillAcceptBids(bid, registration, today);
                break;
            case READY_TO_SUBMIT:
            case DELIVERY_ERROR:
                result = checkIsoStillAcceptBids(bid, registration, today);
                break;
            default:
                result = false;
        }
        logger.info("Bid {} acceptable = {}", bid.getId(), result);
        return result;
    }

    private List<Bid> populatePendingBids(RegistrationSubmissionStatus registration, List<Bid> registrationBids) {
        List<Bid> newBids = new ArrayList<>();
        LocalDate today = ZonedDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
        LocalDate tomorrow = today.plusDays(1);
        boolean todaysBidExist = false;
        boolean tomorrowsBidExist = false;
        if (registrationBids != null) {
            for (Bid bid : registrationBids) {
                if (bid.getTradeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(today)) {
                    todaysBidExist = true;
                }
                if (bid.getTradeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(tomorrow)) {
                    tomorrowsBidExist = true;
                }
            }
        }
        if (!todaysBidExist) {
            newBids.add(createBid(registration, today));
        }
        if (!tomorrowsBidExist && registration.getIsoResource().getIsoProduct().getAutoBidLowRiskOn().equals(BidSubmissionTradeTime.TRADE_DAY_MINUS_ONE)) {
            newBids.add(createBid(registration, tomorrow));
        }
        return newBids;
    }

    private Bid createBid(RegistrationSubmissionStatus registration, LocalDate date) {
        Date tradeDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        boolean hasOutage = isoOutageService.getByOrDefault(registration.getIsoResource(), tradeDate, new IsoOutage()).hasOutage();
        List<Program> programs = registration.getLocations().stream().map(LocationSubmissionStatus::getProgram).collect(Collectors.toList());
        List<Event> eventsThisYear = getAllEventsForCurrentYear(programs);

        EventDurationValidator validator = new EventDurationValidator(registration.getIsoResource(), tradeDate);

        registration.getLocations().forEach(
                location -> location.setExhausted(validator.isLocationExhausted(location))
        );

        return BidHelper.createBid(registration.getIsoResource(), tradeDate, hasOutage, eventsThisYear);
    }

    private boolean checkIsoStillAcceptBids(Bid bid, RegistrationSubmissionStatus registration, LocalDate today) {
        IsoProduct isoProduct = registration.getIsoResource().getIsoProduct();
        LocalTime localTime = LocalTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        if (isoProduct.getBidSubmissionIsoOn().equals(BidSubmissionTradeTimeHours.TRADE_HOUR)) {
            if (today.equals(bid.getTradeLocalDate())) {
                LocalTime isoDeadLine = LocalTime.of(0, 0).minusMinutes(isoProduct.getBidSubmissionIsoMinute());
                return localTime.isBefore(isoDeadLine);
            } else {
                return true;
            }
        } else if (isoProduct.getBidSubmissionIsoOn().equals(BidSubmissionTradeTimeHours.TRADE_DAY_MINUS_ONE)) {
            if (today.equals(bid.getTradeLocalDate())) {
                return true;
            }
        } else {
            if (!today.equals(bid.getTradeLocalDate())) {
                return false;
            }
        }
        LocalTime isoBidTime = LocalTime.of(isoProduct.getBidSubmissionIsoHour(), isoProduct.getBidSubmissionIsoMinute());
        return isoBidTime.isBefore(localTime);
    }

    private boolean checkIsTimeToSend(Bid bid, RegistrationSubmissionStatus registration, LocalDate today) {
        IsoProduct isoProduct = registration.getIsoResource().getIsoProduct();
        if (isoProduct.getAutoBidLowRiskOn().equals(BidSubmissionTradeTime.TRADE_DAY_MINUS_ONE)) {
            if (today.equals(bid.getTradeLocalDate())) {
                return true;
            }
        } else {
            if (!today.equals(bid.getTradeLocalDate())) {
                return false;
            }
        }
        LocalTime localTime = LocalTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        LocalTime autoBidTime = LocalTime.of(isoProduct.getAutoBidLowRiskHour(), isoProduct.getAutoBidLowRiskMinute());
        return autoBidTime.isBefore(localTime);
    }

    private boolean checkExpectedDate(Bid bid, RegistrationSubmissionStatus registration, LocalDate today) {
        return today.equals(bid.getTradeLocalDate()) || registration.getIsoResource().getIsoProduct().getAutoBidLowRiskOn().equals(BidSubmissionTradeTime.TRADE_DAY_MINUS_ONE);
    }

    private boolean checkNotSentYet(Bid bid) {
        return bid.getSubmittedTime() == null || bid.isScheduleModified();
    }
}