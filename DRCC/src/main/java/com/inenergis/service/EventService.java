package com.inenergis.service;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.CustomerNotificationPreferenceDao;
import com.inenergis.dao.EventDao;
import com.inenergis.dao.ImpactedCustomerDao;
import com.inenergis.entity.CustomerNotificationPreference;
import com.inenergis.entity.Event;
import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrParticipant;
import com.inenergis.entity.genericEnum.DispatchReason;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.Program;
import com.inenergis.model.ElasticEvent;
import com.inenergis.model.ElasticEventConverter;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.ElasticActionsUtil;
import com.inenergis.util.UIMessage;
import com.inenergis.util.soap.ItronClient;
import com.inenergis.util.soap.ItronHelper;
import com.itron.mdm.curtailment._2008._05.ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage;
import com.itron.mdm.curtailment._2008._05.IssueEventResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class EventService {

    @Inject
    EventDao eventDao;
    @Inject
    ImpactedCustomerDao impactedCustomerDao;
    @Inject
    CustomerNotificationPreferenceDao customerNotificationPreferenceDao;
    @Inject
    ElasticActionsUtil elasticActionsUtil;

    SimpleDateFormat dayOfTheWeekFormatter = new SimpleDateFormat("EEEE");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

    private Logger log = LoggerFactory.getLogger(EventService.class);

    @Transactional
    public Event saveOrUpdate(Event event) throws IOException {
        switch (event.getStatus()) {
            case RESERVED:
            case PLANNED:
            case CANCELLED:
                break;
            default:
                if(event.getEventNotification() == null){
                    event.setEventNotification(generateEventNotification(event));
                }
        }
        final Event eventSaved = eventDao.saveOrUpdate(event);
        elasticActionsUtil.indexDocument(eventSaved.getId().toString(), ElasticEventConverter.convert(eventSaved), ENERGY_ARRAY_INDEX, ElasticEvent.ELASTIC_TYPE);
        return event;
    }

    private PdpSrEvent generateEventNotification(Event event) {
        PdpSrEvent eventNotification = new PdpSrEvent();
        eventNotification.setFilesReceived(new Date());
        eventNotification.setCountAttempted(new BigDecimal(0));
        eventNotification.setCountDelivered(new BigDecimal(0));
        eventNotification.setEventName(event.getName());
        eventNotification.setEventProgram(event.getProgram().getName());
        eventNotification.setEventStart(Date.from(event.getStartDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        eventNotification.setEventEnd(Date.from(event.getEndDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()));
        eventNotification.setEventState("Scheduled");
        eventNotification.setEventType(event.getEventType().getName());
        if (event.getProgramOption() != null) {
            eventNotification.setEventOptions(event.getProgramOption().getName());
        }
        eventNotification.setPdpSrParticipants(generateParticipants(event, eventNotification));
        eventNotification.setNumParticipants(eventNotification.getPdpSrParticipants().size());
        eventNotification.setPdpSrNotifications(generateParticipantNotifications(event, eventNotification));
        logNotifications(eventNotification.getPdpSrNotifications());
        eventNotification.setNumNotifications(eventNotification.getPdpSrNotifications().size());
        eventNotification.setDedupCompleted(new Date());
        eventNotification.setEventUniqueId(UUID.randomUUID().toString());
        return eventNotification;
    }

    private void logNotifications(List<PdpSrNotification> pdpSrNotifications) {
        for (PdpSrNotification pdpSrNotification : pdpSrNotifications) {
            log.info(pdpSrNotification.getPersonId()+" participant!! "+pdpSrNotification.getPdpSrParticipant());
        }
    }

    private List<PdpSrNotification> generateParticipantNotifications(Event event, PdpSrEvent eventNotification) {
        List<PdpSrNotification> result = new ArrayList<>();
        for (PdpSrParticipant participant : eventNotification.getPdpSrParticipants()) {
            List<CustomerNotificationPreference> notificationPreferences = customerNotificationPreferenceDao.getBy(participant.getSaId(), event.getStartDate(), event.getEndDate());
            if (CollectionUtils.isNotEmpty(notificationPreferences)) {
                PdpSrNotification notification = new PdpSrNotification();
                notification.setCreationTimestamp(new Date());
                notification.setPdpSrEvent(eventNotification);
                notification.setPdpSrParticipant(participant);
                notification.setPersonId(participant.getServiceAgreement().getAccount().getPerson().getPersonId());
                assignNotificationValues(notification, notificationPreferences.get(0));
                notification.setEventDisplayDayname(dayOfTheWeekFormatter.format(event.getStartDate()));
                notification.setEventDisplayEventDate(dateFormatter.format(event.getStartDate()));
                notification.setEventDisplayPremiseAddr(participant.getServiceAddress());
                notification.setVendorStatusTimestamp(new Date());
                participant.addPdpSrNotification(notification);
                assignDuplication(notification, result);
                result.add(notification);
            }
        }
        return result;
    }

    private void assignDuplication(PdpSrNotification notification, List<PdpSrNotification> previousNotifications) {
        PdpSrNotification original = duplicatedNotification(notification, previousNotifications);
        if (original != null) {
            notification.setVendorStatus("DUPLICATE");
            notification.setVendorStatusDisplayMessage("DUPLICATE");
            notification.setDuplicateOf(original);
        }else{
            notification.setVendorStatus("SENT TO VENDOR");
            notification.setVendorStatusDisplayMessage("SENT TO VENDOR");
        }

    }

    private PdpSrNotification duplicatedNotification(PdpSrNotification notification, List<PdpSrNotification> previousNotifications) {
        return previousNotifications.stream().filter(pNotification -> pNotification.getLanguage().equals(notification.getLanguage())
                && pNotification.getNotifyBy().equals(notification.getNotifyBy()) && pNotification.getNotifyByValue().equals(notification.getNotifyByValue())).findFirst().orElse(null);
    }

    private void assignNotificationValues(PdpSrNotification notification, CustomerNotificationPreference customerNotificationPreference) {
        notification.setNotifyBy(customerNotificationPreference.getNotificationTypeId());
        notification.setLanguage(customerNotificationPreference.getNotificationLanguage());
        notification.setNotifyByValue(customerNotificationPreference.getNotificationValue());
        if (customerNotificationPreference.getPhoneExtension() != null) {
            notification.setNotifyByValue(notification.getNotifyByValue() + " (" + customerNotificationPreference.getPhoneExtension() + ")");
        }
    }

    private List<PdpSrParticipant> generateParticipants(Event event, PdpSrEvent eventNotification) {
        return event.getImpactedCustomers().stream().map(customer -> participantFromCustomer(customer, eventNotification)).collect(Collectors.toList());
    }

    private PdpSrParticipant participantFromCustomer(ImpactedCustomer customer, PdpSrEvent event) {
        PdpSrParticipant participant = new PdpSrParticipant();
        participant.setPdpSrEvent(event);
        participant.setAcctId(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getAccount().getAccountId());
        participant.setDruid(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getDruid());
        participant.setPremiseId(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint().getPremise().getPremiseId());
        participant.setSaId(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId());
        participant.setServiceAgreement(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement());
        participant.setServiceAddress(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint().getPremise().getServiceAddress1());
        participant.setServicePointId(customer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint().getServicePointId());
        return participant;
    }

    public List<Event> getAll() {
        return eventDao.getAll();
    }

    public Event getById(Long id) {
        return eventDao.getById(id);
    }

    public List<Event> getBy(Program program, LocalDate date, List<EventStatus> statuses) {
        Date startDate = Date.from(date.atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        Date endDate = Date.from(date.atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusDays(1).minusSeconds(1).toInstant());
        return eventDao.getBy(program, startDate, endDate, statuses);
    }

    public List<Event> getAllForYearInDate(List<Program> programs, Date date) {
        LocalDateTime localStartDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfYear(1).atStartOfDay();
        LocalDateTime localEndDate = localStartDate.plus(1, ChronoUnit.YEARS).minus(1, ChronoUnit.MINUTES);

        Date startDate = Date.from(localStartDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        Date endDate = Date.from(localEndDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        return eventDao.getAllForPeriod(programs, startDate, endDate);
    }

    public List<ImpactedCustomer> getAllImpactedCustomerBySAID(String serviceAgreementId) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("locationSubmissionStatus.programServiceAgreementEnrollment.serviceAgreement.serviceAgreementId")
                .value(serviceAgreementId).matchMode(MatchMode.EXACT).build());
        return impactedCustomerDao.getWithCriteria(conditions);
    }

    public List<ImpactedCustomer> getByLocationsYear(List<LocationSubmissionStatus> locations, int year, List<EventStatus> statuses, List<DispatchReason> dispatchReasons) {
        Date startDate = Date.from(LocalDate.ofYearDay(year, 1).atStartOfDay().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        return impactedCustomerDao.getByLocations(locations, startDate, statuses, dispatchReasons);
    }

    public void issueEventOnItron(ItronClient itronClient, Event event, UIMessage uiMessage) throws IOException {
        try {
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
            saveOrUpdate(event);
        } catch (ICurtailmentEventSaveIssueEventMdmServiceFaultFaultMessage e) {
            uiMessage.addMessage(ConstantsProvider.ERROR_CONNECTING_TO_ITRON, FacesMessage.SEVERITY_ERROR);
            log.error(ConstantsProvider.ERROR_CONNECTING_TO_ITRON, e);
        } catch (DatatypeConfigurationException e) {
            log.error("DatatypeConfigurationException",e);
        }
    }

    public void deleteEvent(Event event) {
        eventDao.delete(event);
    }
}