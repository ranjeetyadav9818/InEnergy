package com.inenergis.service;

import com.inenergis.dao.BidDao;
import com.inenergis.dao.BidSegmentDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.Event;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.BidHelper;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.genericEnum.BidSubmissionIsoInterval;
import com.inenergis.entity.genericEnum.BidSubmissionTradeTimeHours;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.program.HourEnd;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EventDurationValidator;
import com.inenergis.util.TimeUtil;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Getter
public class BidService {

    private static final Logger log = LoggerFactory.getLogger(AgreementPointMapService.class);

    @Inject
    BidDao bidDao;

    @Inject
    BidSegmentDao bidSegmentDao;

    @Inject
    IsoResourceService isoResourceService;

    @Inject
    EventService eventService;

    @Inject
    MeterForecastService meterForecastService;

    @Inject
    IsoOutageService isoOutageService;

    public Bid getById(Long id) {
        return bidDao.getById(id);
    }

    public List<Segment> getAll() {
        return bidSegmentDao.getAll();
    }

    public Bid getBy(Long resourceId, Date tradeDate) {
        return bidDao.getRecentBidResource(resourceId, TimeUtil.getStartOfDay(tradeDate), null);
    }

    public Bid getRecentBidResource(Long isoResourceId, Date tradeDate) {
        return bidDao.getRecentBidResource(isoResourceId, tradeDate, Arrays.asList(BidStatus.ACCEPTED, BidStatus.SUBMITTED));
    }

    public Bid getByResourceIdTradeDateCreated(Long id, Date tradeDate) {
        Bid bid = searchByResourceIdTradeDateStatus(id, tradeDate, null);
        if (bid == null) {
            bid = createBid(id, tradeDate);
            this.save(bid);
        } else {
            HashMap<Program, MeterForecast> programMeterForecastMap = new HashMap<>();
            //update rows with forecast
            EventDurationValidator validator = new EventDurationValidator(bid.getIsoResource(), tradeDate);

            bid.getRegistration().getLocations().forEach(
                    location -> location.setExhausted(validator.isLocationExhausted(location))
            );

            BidHelper.buildForecastByProgram(tradeDate, bid, programMeterForecastMap);
            for (Segment segment : bid.getSegments()) {
                assignCalculatedValuesToSegment(programMeterForecastMap, segment);
            }
            bid.getProperties().reCalculateResourceAdequacyPotential(tradeDate);
        }
        return bid;
    }

    public Bid createBid(Long isoResourceId, Date tradeDate) {
        IsoResource isoResource = isoResourceService.getById(isoResourceId);
        boolean hasOutage = isoOutageService.getByOrDefault(isoResource, tradeDate, new IsoOutage()).hasOutage();
        List<Event> eventsThisYear = eventService.getAllForYearInDate(isoResource.getActiveRegistration().getLocations().stream()
                .map(LocationSubmissionStatus::getProgram).distinct().collect(Collectors.toList()), tradeDate);

        EventDurationValidator validator = new EventDurationValidator(isoResource, tradeDate);

        isoResource.getActiveRegistration().getLocations().forEach(
                location -> location.setExhausted(validator.isLocationExhausted(location))
        );

        Bid bid = BidHelper.createBid(isoResource, tradeDate, hasOutage, eventsThisYear);
        bid.getProperties().reCalculateResourceAdequacyPotential(tradeDate);
        return bid;
    }

    public List<Bid> searchBy(Date tradeDate, ProductType resourceType, SubLap subLap, BidStatus bidStatus, Iso iso) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        if (tradeDate != null) {
            conditions.add(CriteriaCondition.builder().key("tradeDate").value(tradeDate).matchMode(MatchMode.EXACT).build());
        }

        if (resourceType != null) {
            conditions.add(CriteriaCondition.builder().key("isoResource.type").value(resourceType).matchMode(MatchMode.EXACT).build());
        }

        if (subLap != null) {
            conditions.add(CriteriaCondition.builder().key("isoResource.isoSublap").value(subLap.getCode()).matchMode(MatchMode.EXACT).build());
        }

        if (bidStatus != null) {
            conditions.add(CriteriaCondition.builder().key("status").value(bidStatus).matchMode(MatchMode.EXACT).build());
        }
        if (iso != null) {
            conditions.add(CriteriaCondition.builder().key("isoResource.isoProduct.profile.iso.id").value(iso.getId()).matchMode(MatchMode.EXACT).build());
        }
        return bidDao.getWithCriteria(conditions);
    }

    public List<Bid> searchByResourcesAndTradeDate(List<IsoResource> resources, Date tradeDate) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(resources)) {
            conditions.add(CriteriaCondition.builder().key("isoResource").value(resources).matchMode(MatchMode.EXACT).build());
        }
        if (tradeDate != null) {
            conditions.add(CriteriaCondition.builder().key("tradeDate").value(tradeDate).matchMode(MatchMode.EXACT).build());
        }
        List<Bid> withCriteria = bidDao.getWithCriteria(conditions);
        for (Bid bid : withCriteria) {
            EventDurationValidator validator = new EventDurationValidator(bid.getIsoResource(), tradeDate);

            bid.getRegistration().getLocations().forEach(
                    location -> location.setExhausted(validator.isLocationExhausted(location))
            );

            HashMap<Program, MeterForecast> programMeterForecastMap = new HashMap<>();
            BidHelper.buildForecastByProgram(tradeDate, bid, programMeterForecastMap);
            for (Segment segment : bid.getSegments()) {
                assignCalculatedValuesToSegment(programMeterForecastMap, segment);
            }
        }
        return withCriteria;
    }

    public Bid searchByResourceIdTradeDateStatus(Long id, Date tradeDate, BidStatus status) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        if (id != null) {
            conditions.add(CriteriaCondition.builder().key("isoResource.id").value(id).matchMode(MatchMode.EXACT).build());
        }
        if (tradeDate != null) {
            conditions.add(CriteriaCondition.builder().key("tradeDate").value(tradeDate).matchMode(MatchMode.EXACT).build());
        }
        if (status != null) {
            conditions.add(CriteriaCondition.builder().key("status").value(status).matchMode(MatchMode.EXACT).build());
        }
        return bidDao.getUniqueResultWithCriteria(conditions);
    }

    public boolean isHeEditable(Bid bid, int hourEnd) {
        if (bid == null || bid.getIsoResource() ==null || bid.getIsoResource().getIsoProduct() == null ||  bid.getTradeDate() == null) {
            return false;
        }
        IsoProduct isoProduct = bid.getIsoResource().getIsoProduct();
        LocalDate tradeDate = bid.getTradeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();//This date comes as a parameter. Whe should not convert
        LocalDate nowDate = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        LocalDateTime heDatetimeStart = LocalDateTime.of(tradeDate, LocalTime.of(hourEnd - 1, 0));
        LocalDateTime nowDateTime = LocalDateTime.of(nowDate, LocalTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        if (!bid.isBiddableOnDayOfWeek(tradeDate.getDayOfWeek())) {
            return false;
        }
        LocalDateTime cuttingWindow;
        if (BidSubmissionIsoInterval.HourBid_75.getMinutes() == isoProduct.getBidSubmissionIsoMinute()) {
            cuttingWindow = heDatetimeStart.minusMinutes(isoProduct.getBidSubmissionIsoMinute());
            if (cuttingWindow.isBefore(nowDateTime)) {
                return false;
            } else {
                return true;
            }
        } else {
            cuttingWindow = calculateDateFromTradeTimeType(tradeDate, isoProduct.getBidSubmissionIsoHour(), isoProduct.getBidSubmissionIsoMinute(), isoProduct.getBidSubmissionIsoOn());
            if (heDatetimeStart.isBefore(nowDateTime)) {
                return false;
            }
            return nowDateTime.isBefore(cuttingWindow);
        }
    }

    public boolean existsHeEditable(Bid bid) {
        for (HourEnd hourEnd : HourEnd.values()) {
            if (isHeEditable(bid, hourEnd.getHourNumber())) {
                return true;
            }
        }
        return false;
    }

    public void save(Bid bid) {
        log.info("saving bid " + bid.toString());
        bidDao.saveOrUpdate(bid);
    }

    public boolean allHesEditable(Bid bid) {
        for (HourEnd hourEnd : HourEnd.values()) {
            if (!isHeEditable(bid, hourEnd.getHourNumber())) {
                return false;
            }
        }
        return true;
    }

    public Date calculateDefaultTradeDate() {
        LocalDate nowDate = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plus(1, ChronoUnit.DAYS);
        LocalDateTime datetime = nowDate.atStartOfDay();
        return Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getSameDayFromPreviousWeek(Date tradeDate) {
        LocalDate localTradeDate = tradeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate previousWeekDate = localTradeDate.minus(7, ChronoUnit.DAYS);
        LocalDateTime datetime = previousWeekDate.atStartOfDay();
        return Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void assignCalculatedValuesToSegment(HashMap<Program, MeterForecast> programs, Segment segment) {
        MeterForecast programForecast = programs.get(segment.getProgram());
        BidHelper.assignCalculatedValues(programForecast, segment);
    }

    private LocalDateTime calculateDateFromTradeTimeType(LocalDate tradeDate, Integer hour, Integer minutes, BidSubmissionTradeTimeHours interval) {
        LocalDate referenceDate;
        int daysToSubtract = BidSubmissionTradeTimeHours.TRADE_DAY_MINUS_ONE.equals(interval) ? 1 : 0;
        referenceDate = LocalDate.from(tradeDate).minus(daysToSubtract, ChronoUnit.DAYS);
        return LocalDateTime.of(referenceDate, LocalTime.of(hour, minutes));
    }
}
