package com.inenergis.microbot.camel.services;

import com.caiso.ads.api.model.APIDispatchResponseType;
import com.caiso.ads.api.model.APITrajectoryResponseType;
import com.caiso.ads.api.model.DispatchBatchType;
import com.caiso.ads.api.model.InstructionType;
import com.caiso.ads.api.model.TrajectoryBatchType;
import com.caiso.ads.api.model.TrajectoryDopType;
import com.inenergis.entity.DRCCProperty;
import com.inenergis.entity.Event;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.AwardException;
import com.inenergis.entity.award.Trajectory;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.genericEnum.DispatchLevel;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.genericEnum.RetailDispatchScheduleType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.SublapProgramMapping;
import com.inenergis.microbot.camel.dao.AwardDao;
import com.inenergis.microbot.camel.dao.BidDao;
import com.inenergis.microbot.camel.dao.DRCCPropertyDao;
import com.inenergis.microbot.camel.dao.IsoResourceDao;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EnergyUtil;
import com.inenergis.util.TimeUtil;
import com.inenergis.util.soap.CaisoObjectConverter;
import com.inenergis.util.soap.CaisoRequestWrapper;
import com.inenergis.util.soap.ItronClient;
import com.inenergis.util.soap.ItronHelper;
import com.itron.mdm.curtailment._2008._05.IssueEventRequest;
import com.itron.mdm.curtailment._2008._05.IssueEventResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class AwardService {

    private static final Logger logger = LoggerFactory.getLogger(AwardService.class);
    public static final String LAST_AWARD_UUID = "LAST_AWARD_UUID";
    public static final String INITIAL_UUID = "initialUUID";
    private static final String TRAJECTORIES = "BATCH_TRAJECTORIES";
    private static final long MARGIN_OF_ERROR_FOR_AWARD = 2L;
    public static final String NEW_BATCH_DATA = "newBatchData";
    public static final String DISPATCH_BATCHES = "dispatchBatches";
    public static final String LAST_UUID_IN_BATCH = "LAST_UUID_IN_BATCH";
    public static final String MORE_CAPACITY_THAN_BIDDED = "It has been awarded more capacity than bidded";

    private final Properties properties;

    private CaisoRequestWrapper caisoRequestWrapper;
    private ItronClient itronClient;

    @Autowired
    private AwardDao awardDao;

    @Autowired
    private IsoResourceDao isoResourceDao;

    @Autowired
    private BidDao bidDao;

    @Autowired
    private DRCCPropertyDao drccPropertyDao;

    @Autowired
    public AwardService(@Qualifier("appProperties") Properties properties) throws IOException {
        this.properties = properties;
        caisoRequestWrapper = new CaisoRequestWrapper(properties);
        itronClient = new ItronClient(properties);
    }

    @Transactional
    @Modifying
    public void getLastUUID(Exchange exchange) throws Exception {
        DRCCProperty lastUUID = getLastUUIDFromDatabase();
        if (lastUUID == null) {
            lastUUID = getInitialUUID();
            drccPropertyDao.save(lastUUID);
        }
        exchange.getIn().setBody(lastUUID.getValue());
        exchange.getIn().setHeader(INITIAL_UUID, lastUUID.getValue());
    }

    public void getNewBatchesFromADS(Exchange exchange) throws Exception {
        String lastUUID = (String) exchange.getIn().getBody();
        APIDispatchResponseType batchesSinceUUID = caisoRequestWrapper.getBatchesSinceUUID(lastUUID);
        List<DispatchBatchType> dispatchBatch = batchesSinceUUID.getDispatchBatchList().getDispatchBatch();
        exchange.getIn().setBody(dispatchBatch);
        if (!CollectionUtils.isEmpty(dispatchBatch)) {
            exchange.getIn().setHeader(LAST_UUID_IN_BATCH, dispatchBatch.get(dispatchBatch.size() - 1).getBatchUID());
        }
        exchange.getIn().setHeader(DISPATCH_BATCHES, new ArrayList<>());
    }

    public void getBatchDetails(Exchange exchange) throws Exception {
        DispatchBatchType dispatchBatchType = ((DispatchBatchType) exchange.getIn().getBody());
        List<Pair<String, DispatchBatchType>> batches = (List<Pair<String, DispatchBatchType>>) exchange.getIn().getHeader(DISPATCH_BATCHES);
        logger.info("retrieving batchUID: {}", dispatchBatchType.getBatchUID());
        batches.add(caisoRequestWrapper.getDispatchBatch(dispatchBatchType.getBatchUID()));
    }

    public void loadTrajectories(Exchange exchange) throws Exception {
        String lastUUID = (String) exchange.getIn().getHeader(INITIAL_UUID);
        Pair<String, APITrajectoryResponseType> trajectoryData = null;
        if (caisoRequestWrapper.isNewTrajectoryData(lastUUID)) {
            trajectoryData = caisoRequestWrapper.getTrajectoryData(lastUUID);
            exchange.getIn().setHeader(TRAJECTORIES, trajectoryData);
        }
        List<Pair<String, DispatchBatchType>> batches = (List<Pair<String, DispatchBatchType>>) exchange.getIn().getHeader(DISPATCH_BATCHES);
        exchange.getIn().setBody(batches);
        if (!CollectionUtils.isEmpty(batches) || !CollectionUtils.isEmpty(trajectoryData.getRight().getTrajectoryBatchList().getTrajectoryBatch())) {
            exchange.getIn().setHeader(NEW_BATCH_DATA, true);
        }
    }

    @Transactional
    @Modifying
    public void saveTrajectories(Exchange exchange) throws DatatypeConfigurationException {
        Pair<String, APITrajectoryResponseType> trajectories = (Pair<String, APITrajectoryResponseType>) exchange.getIn().getHeader(TRAJECTORIES);
        List<Pair<TrajectoryBatchType, TrajectoryDopType>> dopTrajectories = new ArrayList<>();
        if (trajectories != null && !CollectionUtils.isEmpty(trajectories.getRight().getTrajectoryBatchList().getTrajectoryBatch())) {
            for (TrajectoryBatchType trajectoryBatchType : trajectories.getRight().getTrajectoryBatchList().getTrajectoryBatch()) {
                for (TrajectoryDopType trajectoryDopType : trajectoryBatchType.getDopList().getTrajectoryDop()) {
                    dopTrajectories.add(ImmutablePair.of(trajectoryBatchType, trajectoryDopType));
                }
            }
            Map<String, IsoResource> resourceMap = generateResourcesMap(dopTrajectories.stream().map(t -> t.getRight().getResourceId()).collect(Collectors.toList()));
            List<Award> awards = new ArrayList<>();
            for (Pair<TrajectoryBatchType, TrajectoryDopType> dopTrajectory : dopTrajectories) {
                if (resourceMap.containsKey(dopTrajectory.getRight().getResourceId())) {
                    IsoResource resource = resourceMap.get(dopTrajectory.getRight().getResourceId());
                    Award award = getAward(awards, dopTrajectory.getRight().getTargetTime().toGregorianCalendar().toInstant(), resource);
                    if (award == null && dopTrajectory.getRight().getDop() > 0) {
                        award = createAward(dopTrajectory.getRight(), resource);
                    } else {
                        continue;
                    }
                    Trajectory trajectory = CaisoObjectConverter.convertToTrajectory(award, dopTrajectory.getRight(), resource, trajectories.getLeft(), dopTrajectory.getLeft());
                    award.addTrajectory(trajectory);
                    checkAndAssignExceptionIfNecessaryToAwardDueToHighCapacity(award, trajectory);
                    awards.add(award);
                }
            }
            List<Award> awardsToBeTriggered = getAwardsThatNeedToBeTriggered(awards);
            List<Event> requestsToBeTriggered = createRequestsToBeTriggered(awardsToBeTriggered);
            requestsToBeTriggered.forEach(this::executeEvent);
            for (Award award : awards) {
                awardDao.save(award);
            }
        } else {
            logger.info("no new trajectories in the batches");
        }
    }

    public void saveInstructions(Exchange exchange) throws DatatypeConfigurationException {
        Pair<String, DispatchBatchType> dispatchBatchCaiso = (Pair<String, DispatchBatchType>) exchange.getIn().getBody();
        List<InstructionType> instructionWrapper = dispatchBatchCaiso.getRight().getInstructions().getInstruction();
        if (!CollectionUtils.isEmpty(instructionWrapper)) {
            Map<String, IsoResource> resourceMap = generateResourcesMap(instructionWrapper.stream().map(InstructionType::getResourceId).collect(Collectors.toList()));
            List<Award> awards = new ArrayList<>();
            for (InstructionType instructionType : instructionWrapper) {
                if (resourceMap.containsKey(instructionType.getResourceId())) {
                    IsoResource resource = resourceMap.get(instructionType.getResourceId());
                    Award award = getAward(awards, instructionType.getStartTime().toGregorianCalendar().toInstant(), resource);
                    if (award != null) {
                        award.addInstruction(CaisoObjectConverter.convertToInstruction(instructionType, award, resource, dispatchBatchCaiso.getLeft(), dispatchBatchCaiso.getRight()));
                        awards.add(award);
                    }
                }
            }
            for (Award award : awards) {
                awardDao.save(award);
            }
        }
    }

    @Transactional
    @Modifying
    public void saveLastUUID(Exchange exchange) {
        DRCCProperty lastUUID = getLastUUIDFromDatabase();
        String lastValue = ((String) exchange.getIn().getHeader(LAST_UUID_IN_BATCH));
        if (lastValue != null) {
            lastUUID.setValue(lastValue);
            drccPropertyDao.save(lastUUID);
        }
    }

    private void checkAndAssignExceptionIfNecessaryToAwardDueToHighCapacity(Award award, Trajectory trajectory) {
        int he = trajectory.getTargetTime().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).getHour();
        Long wattsInTrajectory = EnergyUtil.convertToWatts(trajectory.getDop(), ElectricalUnit.MW);
        Bid bid = getBidWithResourceAndTradeDate(award.getResource(), award.getTradeDate());
        if (bid != null) {
            boolean capacityIsOverAllSegments = true;
            for (Segment segment : bid.getSegments()) {
                Long wattsInBid = segment.getCapacityHe(he + 1);
                if (wattsInBid >= wattsInTrajectory) {
                    capacityIsOverAllSegments = false;
                }
            }
            if (capacityIsOverAllSegments && awardHasNoExceptionWithDescription(MORE_CAPACITY_THAN_BIDDED, award)) {
                createErrorInAwards(Collections.singletonList(award), null, MORE_CAPACITY_THAN_BIDDED);
            }
        }
    }

    private boolean awardHasNoExceptionWithDescription(String description, Award award) {
        if (award.getExceptions() != null) {
            for (AwardException awardException : award.getExceptions()) {
                if (description.equals(awardException.getDescription())) {
                    return false;
                }
            }
        }
        return true;
    }


    private List<Award> getAwardsThatNeedToBeTriggered(List<Award> awards) {
        List<Award> result = new ArrayList<>();
        for (Award award : awards) {
            long secondsToTrigger = award.getResource().calculateResourceCurtailmentTime();
            ZonedDateTime triggerTime = award.getStartTime().toInstant().atZone(ZoneId.systemDefault()).minusSeconds(secondsToTrigger);
            if (!triggerTime.isAfter(ZonedDateTime.now(ZoneId.systemDefault()))) {
                result.add(award);
            }
        }
        return result;
    }

    private Award createAward(TrajectoryDopType dopTrajectory, IsoResource resource) {
        Award award;
        award = new Award();
        award.setResource(resource);
        award.setStartTime(dopTrajectory.getTargetTime().toGregorianCalendar().getTime());
        award.setType(RetailDispatchScheduleType.AUTO_DISPATCH_SCHEDULED);
        award.setTradeDate(award.getStartTime());
        Program program = resource.getActiveRegistration().getLocations().get(0).getProgram();
        Long secondsToEnd = TimeUtil.convertToSeconds(program.getActiveProfile().getEventDurations().get(0).getMaxDuration().intValue(),
                program.getActiveProfile().getEventDurations().get(0).getMaxDurationUnits());
        award.setEndTime(Date.from(award.getStartTime().toInstant().plusSeconds(secondsToEnd)));
        return award;
    }

    private Award getAward(List<Award> awards, Instant instant, IsoResource resource) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                .atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        Date startTime = Date.from(zonedDateTime.toInstant());
        Date endTime = Date.from(zonedDateTime.plusDays(1).toInstant());
        return awards.stream().filter(a -> resource.equals(a.getResource()) && (startTime.before(a.getStartTime()) || startTime.equals(a.getStartTime())) &&
                endTime.after(a.getStartTime())).findFirst().orElse(getAward(startTime, endTime, resource));
    }

    private Map<String, IsoResource> generateResourcesMap(List<String> resourceNames) {
        Map<String, IsoResource> resourceMap = new LinkedHashMap<>();
        for (String resourceName : resourceNames) {
            if (!resourceMap.containsKey(resourceName)) {
                IsoResource resource = getResourceByResourceName(resourceName);
                if (resource != null) {
                    resourceMap.put(resourceName, resource);
                }
            }
        }
        return resourceMap;
    }

    private void executeEvent(Event event) {
        try {
            IssueEventRequest issueEventRequest = generateIssueEventRequest(event);
            IssueEventResponse response = itronClient.issueEvent(issueEventRequest);
            if (response.getFault() != null && response.getFault().getValue() != null && StringUtils.isNotEmpty(response.getFault().getValue().getMessage())) {
                event.setStatus(EventStatus.ERROR);
                event.setErrorReason(response.getFault().getValue().getMessage());
            }
            event.setStatus(EventStatus.SUBMITTED);
            event.setExternalEventId("" + response.getEventID());
        } catch (Exception e) {
            event.setStatus(EventStatus.ERROR);
            event.setErrorReason("Unexpected exception: " + e.getMessage());
        }
    }

    private List<Event> createRequestsToBeTriggered(List<Award> awards) throws DatatypeConfigurationException {
        Map<Pair<String, Date>, Pair<Event, List<Award>>> awardEventMap = new LinkedHashMap<>();
        for (Award award : awards) {
            Pair<String, Date> sublapDatePair = ImmutablePair.of(award.getResource().getIsoSublap (), award.getStartTime());
            if (!awardEventMap.containsKey(sublapDatePair)) {
                List<Award> list = new ArrayList<>();
                list.add(award);
                Pair<Event, List<Award>> eventAwardPair = MutablePair.of(null, list);
                awardEventMap.put(sublapDatePair, eventAwardPair);
            } else {
                awardEventMap.get(sublapDatePair).getRight().add(award);
            }
        }
        List<Event> result = new ArrayList<>();
        for (Pair<Event, List<Award>> eventAwardListPair : awardEventMap.values()) {
            Program program = awards.get(0).getResource().getActiveRegistration().getLocations().get(0).getProgram();

            Event event = new Event();
            event.setDispatchReason(DispatchReason.MARKET_AWARD);
            event.setStartDate(eventAwardListPair.getRight().get(0).getStartTime());
            event.setEndDate(eventAwardListPair.getRight().get(0).getEndTime());

            long secondsToTrigger = eventAwardListPair.getRight().get(0).getResource().calculateResourceCurtailmentTime();
            ZonedDateTime triggerTime = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).minusSeconds(secondsToTrigger);
            if (triggerTime.plusMinutes(MARGIN_OF_ERROR_FOR_AWARD).isBefore(ZonedDateTime.now(ZoneId.systemDefault()))) {
                event.setStartDate(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusSeconds(secondsToTrigger).toInstant()));
                Long secondsToEnd = TimeUtil.convertToSeconds(program.getActiveProfile().getEventDurations().get(0).getMaxDuration().intValue(),
                        program.getActiveProfile().getEventDurations().get(0).getMaxDurationUnits());
                event.setEndDate(Date.from(event.getStartDate().toInstant().plusSeconds(secondsToEnd)));
                createErrorInAwards(eventAwardListPair.getRight(), event, "Event was dispatched after the start time because the ISO didn't inform in advance");
            }

            String programId = program.getActiveProfile().getSublapProgramMappings().stream()
                    .filter(mapping -> mapping.getSubLap() == null || mapping.getSubLap().getCode().equals(event.getImpactedResources().get(0).getIsoResource().getIsoSublap ()))
                    .sorted(SublapProgramMapping::compareTo) // prioritise mapping with provided sublap code over "Any"
                    .findFirst()
                    .map(SublapProgramMapping::getDrmsProgramId)
                    .orElse(null);
            event.setProgramId(programId);
            event.setProgram(program);
            event.setImpactedCustomers(getAllCustomers(eventAwardListPair.getRight(), event));
            result.add(event);
        }

        return result;
    }

    private void createErrorInAwards(List<Award> awards, Event event, String description) {
        for (Award award : awards) {
            AwardException awardException = new AwardException();
            awardException.setDateAdded(new Date());
            awardException.setAward(award);
            awardException.setEvent(event);
            awardException.setDescription(description);
            award.getExceptions().add(awardException);
        }
    }

    private List<ImpactedCustomer> getAllCustomers(List<Award> awards, Event event) {
        List<ImpactedCustomer> result = new ArrayList<>();

        for (Award award : awards) {
            for (LocationSubmissionStatus location : award.getResource().getActiveRegistration().getLocations()) {
                ImpactedCustomer impactedCustomer = new ImpactedCustomer(location, event, location.getProgramServiceAgreementEnrollment().getDrmsId(), event.getProgramId());
                result.add(impactedCustomer);
            }
        }
        return result;
    }

    private IssueEventRequest generateIssueEventRequest(Event event) throws DatatypeConfigurationException {
        String correlationId = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        List<String> servicePointIds = null;

        if (!allResourcesOfSublapImpacted(event)) {
            event.setDispatchLevel(DispatchLevel.CUSTOMERS);
            servicePointIds = event.getImpactedCustomers().stream()
                    .map(ImpactedCustomer::getDrmsId)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            event.setDispatchLevel(DispatchLevel.SUBLAP);
        }
        return ItronHelper.issueEventRequestBuilder()
                .programId(event.getProgramId())
                .correlationId(correlationId)
                .issueEventServicePointIdList(servicePointIds)
                .startDate(LocalDateTime.from(event.getStartDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)))
                .endDate(LocalDateTime.from(event.getEndDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)))
                .eventAlternateId(event.getUuid())
                .build();
    }

    @Transactional
    private boolean allResourcesOfSublapImpacted(Event event) {
        IsoResource isoResource = event.getImpactedResources().get(0).getIsoResource();
        int count = isoResourceDao.findByIsoSublapAndIsoProduct(isoResource.getIsoSublap (), isoResource.getIsoProduct()).size();
        return count == event.getImpactedResources().size();
    }

    @Transactional
    private Award getAward(Date fromTradeDate, Date toTradeDate, IsoResource resource) {
        return awardDao.findFirstByTradeDateGreaterThanEqualAndTradeDateLessThanEqualAndResource(fromTradeDate, toTradeDate, resource);
    }

    @Transactional
    private IsoResource getResourceByResourceName(String resourceName) {
        return isoResourceDao.findByName(resourceName);
    }

    @Transactional
    private DRCCProperty getLastUUIDFromDatabase() {
        return drccPropertyDao.getByKey(LAST_AWARD_UUID);
    }

    @Transactional
    private Bid getBidWithResourceAndTradeDate(IsoResource resource, Date tradeDate) {
        Date startOfDay = Date.from(tradeDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return bidDao.findFirstByTradeDateEqualsAndIsoResource(startOfDay, resource);
    }

    private DRCCProperty getInitialUUID() throws Exception {
        DRCCProperty property = new DRCCProperty();
        property.setKey(LAST_AWARD_UUID);
        APIDispatchResponseType batchesSinceUUID = caisoRequestWrapper.getBatchesSinceUUID("-1");
        List<DispatchBatchType> dispatchBatch = batchesSinceUUID.getDispatchBatchList().getDispatchBatch();
        String batchUID = dispatchBatch.get(dispatchBatch.size() - 1).getBatchUID();
        property.setValue(batchUID);
        return property;
    }
}