package com.inenergis.controller.dispatch;

import com.inenergis.commonServices.EnrollmentServiceContract;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.entity.Event;
import com.inenergis.entity.EventDispatchLevel;
import com.inenergis.entity.genericEnum.DispatchLevel;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.genericEnum.EventType;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.DRMSProgramMapping;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramDefinedDispatchLevel;
import com.inenergis.entity.program.ProgramDefinedDispatchReason;
import com.inenergis.entity.program.ProgramEventDuration;
import com.inenergis.entity.program.ProgramOption;
import com.inenergis.entity.program.ProgramSeason;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.SublapProgramMapping;
import com.inenergis.service.EventService;
import com.inenergis.service.LocationSubmissionStatusService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.ServicePointService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.BundleAccessor;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.EventDurationValidator;
import com.inenergis.util.PickListElement;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.UIMessage;
import com.inenergis.util.soap.ItronClient;
import com.inenergis.util.soap.ItronHelper;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.IssueEventResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.User;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.inenergis.entity.genericEnum.DispatchLevel.ENTIRE_PROGRAM;

@Named
@ViewScoped
@Getter
@Setter
public class ScheduleEventController implements Serializable {

    @Inject
    private EventService eventService;

    @Inject
    private ProgramServiceContract programService;

    @Inject
    private SubLapService subLapService;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private ServicePointService servicePointService;

    @Inject
    private LocationSubmissionStatusService locationSubmissionStatusService;

    @Inject
    private EnrollmentServiceContract enrollmentServiceContract;

    @Inject
    private PropertyAccessor propertyAccessor;

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private BundleAccessor bundleAccessor;

    @Inject
    EntityManager entityManager;

    private static final String ERROR_PROGRAM_ID_MISSING = "It was not possible to retrieve the Program ID. Please check DRMS Program ID Mapping settings.";
    private static final String ERROR_DRMS_ID_MISSING = "The service agreement with id {0} does not have a DRMS ID";
    private static final String ERROR_EMPTY_ELIGIBLE_SUBLAP_LIST = "Eligible Sublap list cannot be empty";
    private static final String THERE_ARE_NO_VALID_SA_TO_DISPATCH = "There are no valid SAs to dispatch";
    private static final String DISPATCH_MANAGEMENT_PAGE = "DispatchManagement.xhtml";

    private Logger log = LoggerFactory.getLogger(ScheduleEventController.class);

    private boolean newEventFormVisible;
    private boolean datesAreValid;
    private boolean newEventFormValid;
    private boolean showDispatchLevelPickList;
    private boolean showDispatchLevelCommaSeparated;
    private boolean showReview;
    private boolean showEvents = true;

    private Event newEvent;

    private DualListModel<PickListElement> dispatchLevelPickList = new DualListModel<>();
    private List<PickListElement> dispatchLevelList = new ArrayList<>();
    private String dispatchLevelCommaSeparated;

    private List<DispatchLevel> programDispatchLevels;
    private List<Program> programList = new ArrayList<>();
    private List<ProgramOption> programOptionList = new ArrayList<>();

    private Long totalHours;
    private Long totalMinutes;

    private String password = "";

    private EventType eventTypeFilter;

    private List<Event> newEvents = new ArrayList<>();
    private List<ImpactedCustomer> impactedCustomers = new ArrayList<>();
    private Set<ImpactedResource> impactedResources = new HashSet<>();

    @PostConstruct
    public void init() {
        programList = programService.getAllWithActiveProfile();
        final Long eventId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (eventId != null) {
            newEvent = eventService.getById(eventId);
            loadOptions();
        } else {
            newEvent = new Event();
        }
        eventTypeFilter = EventType.SCHEDULED;
    }

    public void clear() {
        newEvent = new Event();
        newEventFormVisible = false;
        showDispatchLevelPickList = false;
        showDispatchLevelCommaSeparated = false;
        showReview = false;
        dispatchLevelCommaSeparated = "";
        dispatchLevelPickList = new DualListModel<>();
        dispatchLevelList = new ArrayList<>();
        totalHours = null;
        totalMinutes = null;
        datesAreValid = false;
        newEventFormValid = false;
        showEvents = true;
    }

    public void prepareEventSchedule() throws DatatypeConfigurationException {
        validateDates();

        List<String> servicePointIds;

        String programId = newEvent.getProgram().getActiveProfile().getDrmsProgramMappings().stream()
                .filter(mapping -> newEvent.getDispatchLevel().equals(mapping.getDispatchLevel()) || mapping.getDispatchLevel() == null)
                .filter(mapping -> newEvent.getDispatchReason().equals(mapping.getDispatchReason()) || mapping.getDispatchReason() == null)
                .findFirst()
                .map(DRMSProgramMapping::getDrmsProgramId)
                .orElse(null);
        if (programId == null && ENTIRE_PROGRAM.equals(newEvent.getDispatchLevel())) {
            uiMessage.addMessage(ERROR_PROGRAM_ID_MISSING, FacesMessage.SEVERITY_ERROR);
            return;
        }

        impactedCustomers.clear();
        impactedResources.clear();
        newEvents.clear();

        switch (newEvent.getDispatchLevel()) {
            case SUBLAP:
                List<String> pickedValues = dispatchLevelPickList.getTarget().stream()
                        .map(PickListElement::getCode)
                        .collect(Collectors.toList());

                if (pickedValues.isEmpty()) {
                    uiMessage.addMessage(ERROR_EMPTY_ELIGIBLE_SUBLAP_LIST, FacesMessage.SEVERITY_ERROR);
                    return;
                }

                for (String sublap : pickedValues) {
                    String subLapProgramId = getSublapProgramId(null, sublap);

                    if (subLapProgramId == null) {
                        uiMessage.addMessage(ERROR_PROGRAM_ID_MISSING, FacesMessage.SEVERITY_ERROR);
                        return;
                    }

                    List<LocationSubmissionStatus> locationsBySubLaps = locationSubmissionStatusService.getBySubLapsAndProgram(Collections.singletonList(sublap), newEvent.getProgram())
                            .stream().filter(l -> l.getProgramServiceAgreementEnrollment().isActivelyEnrolled()).collect(Collectors.toList());

                    Event event = getNewEventInstance();
                    List<ImpactedCustomer> sublapImpactedCustomers = locationsBySubLaps.stream()
                            .map(LocationSubmissionStatus::getProgramServiceAgreementEnrollment)
                            .distinct()
                            .map(enrollment -> new ImpactedCustomer(enrollment.getLastLocation(), event, enrollment.getDrmsId(), subLapProgramId))
                            .collect(Collectors.toList());

                    if (sublapImpactedCustomers.isEmpty()) {
                        uiMessage.addMessage(bundleAccessor.getPropertyResourceBundle().getString("data.mapping.sublap") + sublap + " cannot be empty", FacesMessage.SEVERITY_ERROR);
                        return;
                    }

                    event.setProgramId(subLapProgramId);
                    event.setEventDispatchLevels(Collections.singletonList(new EventDispatchLevel(sublap, event)));
                    event.setImpactedCustomers(sublapImpactedCustomers);

                    newEvents.add(event);
                    impactedCustomers.addAll(sublapImpactedCustomers);
                    impactedResources.addAll(event.getImpactedResources());
                }
                break;

            case CUSTOMERS:
                List<String> saIds = Arrays.stream(dispatchLevelCommaSeparated.split(","))
                        .map(String::trim).collect(Collectors.toList());
                List<ProgramServiceAgreementEnrollment> agreementEnrollments = enrollmentServiceContract.getBySaIdsAndProgram(saIds, EnrolmentStatus.ENROLLED, newEvent.getProgram())
                        .stream().filter(e -> e.getLastLocation() != null && e.isActivelyEnrolled()).collect(Collectors.toList());
                if (programId != null) {
                    Event event = getNewEventInstance();

                    impactedCustomers.addAll(agreementEnrollments.stream()
                            .map(enrollment -> new ImpactedCustomer(enrollment.getLastLocation(), event, enrollment.getDrmsId(), programId))
                            .collect(Collectors.toList()));
                    servicePointIds = impactedCustomers.stream()
                            .map(ImpactedCustomer::getDrmsId)
                            .collect(Collectors.toList());

                    event.setProgramId(programId);
                    event.setServicePointIds(servicePointIds);
                    event.setImpactedCustomers(impactedCustomers);

                    newEvents.add(event);
                    impactedResources.addAll(event.getImpactedResources());
                } else {
                    impactedCustomers.addAll(agreementEnrollments.stream()
                            .map(enrollment -> new ImpactedCustomer(enrollment.getLastLocation(), newEvent, enrollment.getDrmsId(), programId))
                            .collect(Collectors.toList()));
                    Map<String, List<ProgramServiceAgreementEnrollment>> enrollmentsBySublap = agreementEnrollments.stream().collect(Collectors.groupingBy(e -> e.getLastLocation().getIsoSublap ()));
                    for (String sublap : enrollmentsBySublap.keySet()) {
                        servicePointIds = enrollmentsBySublap.get(sublap).stream()
                                .map(ProgramServiceAgreementEnrollment::getDrmsId)
                                .collect(Collectors.toList());
                        String subLapProgramId = getSublapProgramId(programId, sublap);
                        if (subLapProgramId == null) {
                            uiMessage.addMessage(ERROR_PROGRAM_ID_MISSING, FacesMessage.SEVERITY_ERROR);
                            return;
                        }

                        Event event = getNewEventInstance();

                        List<ImpactedCustomer> sublapImpactedCustomers = agreementEnrollments.stream()
                                .filter(c -> c.getLastLocation().getIsoSublap ().equals(sublap))
                                .map(enrollment -> new ImpactedCustomer(enrollment.getLastLocation(), event, enrollment.getDrmsId(), subLapProgramId))
                                .collect(Collectors.toList());

                        event.setProgramId(subLapProgramId);
                        event.setServicePointIds(servicePointIds);
                        event.setEventDispatchLevels(Collections.singletonList(new EventDispatchLevel(sublap, event)));
                        event.setImpactedCustomers(sublapImpactedCustomers);

                        newEvents.add(event);
                        impactedResources.addAll(event.getImpactedResources());
                    }
                }
                break;

            case ENTIRE_PROGRAM:
                Event event = getNewEventInstance();

                impactedCustomers.addAll(programService.getActiveSAs(newEvent.getProgram()).stream()
                        .filter(e -> e.getLastLocation() != null && e.isActivelyEnrolled())
                        .map(enrollment -> new ImpactedCustomer(enrollment.getLastLocation(), event, enrollment.getDrmsId(), programId))
                        .collect(Collectors.toList()));

                event.setProgramId(programId);
                event.setImpactedCustomers(impactedCustomers);
                event.setSaCount(impactedCustomers.size());

                newEvents.add(event);
                impactedResources.addAll(event.getImpactedResources());
                break;
        }

        LocalDate tradeDate = LocalDate.from(newEvent.getStartDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));

        List<Event> scheduledEvents = eventService.getBy(newEvent.getProgram(), tradeDate, Arrays.asList(EventStatus.SUBMITTED, EventStatus.TERMINATED));

        List<LocationSubmissionStatus> scheduledLocationsForTradeDate = scheduledEvents.stream()
                .map(Event::getImpactedCustomers)
                .flatMap(Collection::stream)
                .map(ImpactedCustomer::getLocationSubmissionStatus)
                .collect(Collectors.toList());

        for (ImpactedCustomer impactedCustomer : impactedCustomers) {
            if (scheduledLocationsForTradeDate.contains(impactedCustomer.getLocationSubmissionStatus())) {
                uiMessage.addMessage("A dispatch is already scheduled for the selected date please select another date", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }

        if (CollectionUtils.isEmpty(impactedCustomers)) {
            uiMessage.addMessage(THERE_ARE_NO_VALID_SA_TO_DISPATCH, FacesMessage.SEVERITY_ERROR);
            return;
        }

        ImpactedCustomer impactedCustomerWithNoDRMSId = impactedCustomers.stream()
                .filter(c -> StringUtils.isEmpty(c.getDrmsId()))
                .findFirst()
                .orElse(null);
        if (impactedCustomerWithNoDRMSId != null) {
            uiMessage.addMessage(ERROR_DRMS_ID_MISSING, FacesMessage.SEVERITY_ERROR,
                    impactedCustomerWithNoDRMSId.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId());
            return;
        }

        List<DispatchReason> countTowardsDispatchReasons = newEvent.getProgram().getActiveProfile().getDispatchReasons().stream()
                .map(ProgramDefinedDispatchReason::getDispatchReason)
                .collect(Collectors.toList());

        if (countTowardsDispatchReasons.contains(newEvent.getDispatchReason())) {
            List<LocationSubmissionStatus> impactedLocations = impactedCustomers.stream()
                    .map(ImpactedCustomer::getLocationSubmissionStatus)
                    .collect(Collectors.toList());

            List<ImpactedCustomer> impactedCustomersTradeYear = eventService.getByLocationsYear(impactedLocations, tradeDate.getYear(),
                    Arrays.asList(EventStatus.SUBMITTED, EventStatus.TERMINATED), countTowardsDispatchReasons);

            // Check Event Hours
            EventDurationValidator.EventHistoryHours eventHistoryHours = new EventDurationValidator.EventHistoryHours(impactedCustomersTradeYear, tradeDate);
            for (ImpactedCustomer impactedCustomer : impactedCustomers) {
                LocationSubmissionStatus locationSubmissionStatus = impactedCustomer.getLocationSubmissionStatus();
                long eventHours = impactedCustomer.getEvent().getDuration().toHours();

                if (!eventHistoryHours.validateWithAdditionalHours(eventHours, locationSubmissionStatus)) {
                    uiMessage.addMessage(eventHistoryHours.getLastError(), FacesMessage.SEVERITY_ERROR);
                    return;
                }
            }
        }

        newEventFormVisible = false;
        showEvents = false;
        showReview = true;
    }

    private String getSublapProgramId(String programId, String sublap) {
        return newEvent.getProgram().getActiveProfile().getSublapProgramMappings().stream()
                .filter(mapping -> mapping.getSubLap() == null || mapping.getSubLap().getCode().equals(sublap))
                .sorted(SublapProgramMapping::compareTo) // prioritise mapping with provided sublap code over "Any"
                .findFirst()
                .map(SublapProgramMapping::getDrmsProgramId)
                .orElse(programId);
    }

    public void submitEvents() {
        String userName = ((User) identity.getAccount()).getLoginName();
        Credentials credentials = new UsernamePasswordCredentials(userName, new Password(password));
        identityManager.validateCredentials(credentials);
        if (!Credentials.Status.VALID.equals(credentials.getStatus())) {
            uiMessage.addMessage("Invalid Password", FacesMessage.SEVERITY_ERROR);
            return;
        }

        ItronClient itronClient = new ItronClient(propertyAccessor.getProperties());
        try {
            for (Event event : newEvents) {
                IssueEventResponse response = itronClient.issueEvent(ItronHelper.issueEventRequestBuilder()
                        .programId(event.getProgramId())
                        .issueEventServicePointIdList(event.getServicePointIds())
                        .startDate(LocalDateTime.from(event.getStartDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)))
                        .endDate(LocalDateTime.from(event.getEndDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)))
                        .eventAlternateId(event.getUuid())
                        .build());
                if (response.getFault() != null && response.getFault().getValue() != null && StringUtils.isNotEmpty(response.getFault().getValue().getMessage())) {
                    uiMessage.addMessage(ConstantsProvider.FAULT_FROM_ITRON, FacesMessage.SEVERITY_ERROR, response.getFault().getValue().getMessage());
                    return;
                }
                event.setExternalEventId(Integer.toString(response.getEventID()));
                event.setStatus(EventStatus.SUBMITTED);
                event.setCreatedBy(userName);
                event.setCreatedOn(new Date());
                eventService.saveOrUpdate(event);
                eventService.deleteEvent(newEvent);
                FacesContext.getCurrentInstance().getExternalContext().redirect(DISPATCH_MANAGEMENT_PAGE);
                FacesContext.getCurrentInstance().responseComplete();
            }
        } catch (ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage e) {
            uiMessage.addMessage(ConstantsProvider.ERROR_CONNECTING_TO_ITRON, FacesMessage.SEVERITY_ERROR);
            log.error(ConstantsProvider.ERROR_CONNECTING_TO_ITRON, e);
        } catch (DatatypeConfigurationException | IOException e) {
            log.error("Unexpected exception", e);
        }
    }

    public void cancel() {
        clear();
    }

    public void loadOptions() {
        if (newEvent.getProgram() == null || newEvent.getProgram().getActiveProfile() == null) {
            programOptionList.clear();
            return;
        }

        programOptionList = newEvent.getProgram().getActiveProfile().getOptions();
        programDispatchLevels = newEvent.getProgram().getActiveProfile().getDispatchLevels().stream()
                .map(ProgramDefinedDispatchLevel::getDispatchLevel)
                .filter(dispatchLevel -> Arrays.asList("ENTIRE_PROGRAM", "SUBLAP", "CUSTOMERS").contains(dispatchLevel.toString()))
                .collect(Collectors.toList());
    }

    public void selectDispatchLevel() {
        validateNewEventForm();

        if (newEvent.getDispatchLevel() == null) {
            return;
        }

        dispatchLevelPickList.getSource().clear();
        dispatchLevelPickList.getTarget().clear();
        dispatchLevelList.clear();
        showDispatchLevelPickList = false;
        showDispatchLevelCommaSeparated = false;
        switch (newEvent.getDispatchLevel()) {
            case ENTIRE_PROGRAM:
                break;
            case SUBSTATION:
                dispatchLevelPickList.setSource(servicePointService.getSubstations().stream()
                        .map(substation -> new PickListElement(substation.getName()))
                        .collect(Collectors.toList()));
                showDispatchLevelPickList = true;
                break;
            case CUSTOMERS:
                showDispatchLevelCommaSeparated = true;
                break;
            case SUBLAP:
                dispatchLevelPickList.setSource(subLapService.getAll().stream()
                        .map(subLap -> new PickListElement(subLap.getName(), subLap.getCode()))
                        .collect(Collectors.toList()));
                showDispatchLevelPickList = true;
                break;
            case CIRCUIT:
                dispatchLevelPickList.setSource(servicePointService.getDistinctCircuits().stream()
                        .map(PickListElement::new)
                        .collect(Collectors.toList()));
                showDispatchLevelPickList = true;
        }
        if (!dispatchLevelPickList.getSource().isEmpty()) {
            dispatchLevelList = new ArrayList<>(dispatchLevelPickList.getSource());
        }
    }

    public void validateNewEventForm() {
        validateDates();
        newEventFormValid = datesAreValid && newEvent.getDispatchReason() != null && newEvent.getDispatchLevel() != null;
    }

    private void validateDates() {
        datesAreValid = false;

        if (newEvent.getStartDate() == null || newEvent.getEndDate() == null) {
            totalHours = null;
            return;
        }

        Instant instantStartDate = newEvent.getStartDate().toInstant();
        Instant instantEndDate = newEvent.getEndDate().toInstant();
        LocalDateTime localStartDate = LocalDateTime.ofInstant(instantStartDate, ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        LocalDateTime localEndDate = LocalDateTime.ofInstant(instantEndDate, ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);

        if (ChronoUnit.DAYS.between(instantStartDate, instantEndDate) > 0) {
            uiMessage.addMessage("Event Start Date and Event End Date must be the same", FacesMessage.SEVERITY_ERROR);
            return;
        }

        totalHours = ChronoUnit.HOURS.between(instantStartDate, instantEndDate);
        totalMinutes = ChronoUnit.MINUTES.between(instantStartDate, instantEndDate);

        if (totalHours > 0) {
            totalMinutes = totalMinutes % (totalHours * 60);
        }

        if (totalHours < 0) {
            uiMessage.addMessage("Event Start Date must be before Event End Date", FacesMessage.SEVERITY_ERROR);
            totalHours = null;
            return;
        }

        Duration duration = newEvent.getDuration();
        final List<ProgramEventDuration> eventDurations = newEvent.getProgram().getActiveProfile().getEventDurations();
        if (CollectionUtils.isEmpty(eventDurations)) {
            uiMessage.addMessage("Program Profile does not have durations", FacesMessage.SEVERITY_ERROR);
            return;
        }
        ProgramEventDuration programEventDuration = eventDurations.get(0);
        Long minDurationSeconds = Duration.of(programEventDuration.getMinDuration(), programEventDuration.getMinDurationUnits().getChronoUnit()).getSeconds();
        Long maxDurationSeconds = Duration.of(programEventDuration.getMaxDuration(), programEventDuration.getMaxDurationUnits().getChronoUnit()).getSeconds();
        if (duration.getSeconds() < minDurationSeconds) {
            uiMessage.addMessage("Duration time is too short", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (duration.getSeconds() > maxDurationSeconds) {
            uiMessage.addMessage("Duration time is too long", FacesMessage.SEVERITY_ERROR);
            return;
        }

        // Check Notification Lead Time
        Short leadTime = newEvent.getProgram().getActiveProfile().getDefaultNotification().getNotificationLeadTime();
        ChronoUnit chronoUnit = newEvent.getProgram().getActiveProfile().getDefaultNotification().getNotificationLeadTimeUnits().getChronoUnit();
        Instant notificationLeadTime = Instant.now().plus(leadTime, chronoUnit);
        if (!instantStartDate.isAfter(notificationLeadTime)) {
            uiMessage.addMessage("Start Time must be greater than Notification Lead Time", FacesMessage.SEVERITY_ERROR);
            return;
        }

        // Check Seasons Ranges
        List<ProgramSeason> seasons = newEvent.getProgram().getActiveProfile().getSeasons();
        boolean isStartDateInRange = seasons.isEmpty();
        boolean isEndDateInRange = seasons.isEmpty();
        for (ProgramSeason season : newEvent.getProgram().getActiveProfile().getSeasons()) {
            if (!instantStartDate.isBefore(season.getStartDate().toInstant()) && !instantStartDate.isAfter(season.getEndDate().toInstant())) {
                if (season.isDispatchOn(localStartDate.getDayOfWeek())) {
                    isStartDateInRange = true;
                }
            }

            if (!instantEndDate.isBefore(season.getStartDate().toInstant()) && !instantStartDate.isAfter(season.getEndDate().toInstant())) {
                if (season.isDispatchOn(localEndDate.getDayOfWeek())) {
                    isEndDateInRange = true;
                }
            }

            if (isStartDateInRange && isEndDateInRange) {
                break;
            }
        }
        if (!isStartDateInRange || !isEndDateInRange) {
            uiMessage.addMessage("Start and End times must be within Season Open Hours range", FacesMessage.SEVERITY_ERROR);
            return;
        }

        datesAreValid = true;
    }

    public void showNewEventForm() {
        newEventFormVisible = true;
        showEvents = false;
        showReview = false;
    }

    private Event getNewEventInstance() {
        Event event = new Event();
        event.setDispatchLevel(newEvent.getDispatchLevel());
        event.setDispatchReason(newEvent.getDispatchReason());
        event.setProgram(newEvent.getProgram());
        event.setProgramOption(newEvent.getProgramOption());
        event.setStartDate(newEvent.getStartDate());
        event.setEndDate(newEvent.getEndDate());
        event.setSaCount(newEvent.getSaCount());
        event.setName(newEvent.getName());
        return event;
    }

}