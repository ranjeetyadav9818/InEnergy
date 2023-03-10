package com.inenergis.controller.events;

import com.inenergis.controller.authentication.AuthorizationChecker;
import com.inenergis.controller.converter.PhoneConverter;
import com.inenergis.controller.events.LazyNotificationDataModel.NotificationType;
import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrParticipant;
import com.inenergis.model.EventNotificationSummary;
import com.inenergis.service.EventStatisticService;
import com.inenergis.service.ParameterEncoderService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Transactional
@Getter
@Setter
public class EventList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager entityManager;

    private Long eventId;

    @Inject
    EventStatisticService eventStatisticService;

    @Inject
    AuthorizationChecker authorizationChecker;

    Logger log = LoggerFactory.getLogger(EventList.class);

    private PdpSrEvent pdpSrEvent;
    private PdpSrNotification pdpSrNotification;
    private PdpSrParticipant pdpSrParticipant;
    private PdpSrNotification notificationForPdpSrParticipant;
    private int activeTab = 0;
    private PdpSrNotification manualNotification;

    private List<String> notificationMethod = new ArrayList<>();
    private List<String> successType = new ArrayList<>();
    private List<String> vendorsList = new ArrayList<>();

    private LazyDataModel<PdpSrParticipant> participantLazyModel;
    private LazyDataModel<PdpSrNotification> vendorNotificationLazyModel;
    private LazyDataModel<PdpSrNotification> unsuccessfulNotificationLazyModel;
    private LazyDataModel<PdpSrNotification> enpNotificationLazyModel;
    private LazyDataModel<PdpSrNotification> participantRequestedNotificationLazyModel;

    private List<PdpSrEvent> pdpSrEvents = new ArrayList<>();
    private List<PdpSrNotification> pdpSrParticipantVendorNotifications = new ArrayList<>();

    private List<EventInfo> eventDetails = new ArrayList<>();

    private EventNotificationSummary eventNotificationSummary;

    private PhoneConverter phoneConverter = new PhoneConverter();

    private void addEventInfo() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        eventDetails = new ArrayList<>();
        eventDetails.add(new EventInfo("Program", pdpSrEvent.getEventProgram(), "apps"));
        if (pdpSrEvent.getEventOptions() != null) {
            eventDetails.add(new EventInfo("Options", pdpSrEvent.getEventOptionsLabel(), "assistant"));
        }
        eventDetails.add(new EventInfo("Event Start", df.format(pdpSrEvent.getEventStart()), "play_circle_outline"));
        eventDetails.add(new EventInfo("Event End", df.format(pdpSrEvent.getEventEnd()), "stop"));
        if ("TEST".equals(pdpSrEvent.getEventType())) {
            eventDetails.add(new EventInfo("Event Type", pdpSrEvent.getEventType(), "folder_open"));
        }
    }

    private void addEventNotificationInfo() {
        eventDetails.add(new EventInfo("ENP", phoneConverter.convertToAmericanMaskIfPhoneNumber(pdpSrNotification.getNotifyByValue()), "subdirectory_arrow_right"));
    }

    private void addEventParticipantInfo() {
        if (pdpSrParticipant != null) {
            eventDetails.add(new EventInfo("ACCT ID", pdpSrParticipant.getAcctId(), "account_circle"));
            eventDetails.add(new EventInfo("SA ID", pdpSrParticipant.getSaId(), "person_outline"));
            eventDetails.add(new EventInfo("SP ID", pdpSrParticipant.getServicePointId(), "power"));
            eventDetails.add(new EventInfo("PREM ID", pdpSrParticipant.getPremiseId(), "home"));
            eventDetails.add(new EventInfo("Service Address", pdpSrParticipant.getServiceAddress(), "location_on"));
        }
    }

    public void addManualNotification() {
        manualNotification = new PdpSrNotification();
        if (getPdpSrParticipant().getPdpSrNotifications() != null && !getPdpSrParticipant().getPdpSrNotifications().isEmpty()) {
            PdpSrNotification not = getPdpSrParticipant().getPdpSrNotifications().get(0);
            manualNotification.setPdpSrParticipant(getPdpSrParticipant());
            manualNotification.setPdpSrEvent(getPdpSrEvent());
            manualNotification.setEventDisplayDayname(not.getEventDisplayDayname());
            manualNotification.setEventDisplayEventDate(not.getEventDisplayEventDate());
            manualNotification.setEventDisplayPremiseAddr(not.getEventDisplayPremiseAddr());
            manualNotification.setVendorStatusDisplayMessage("PGE Manual Notification");
            manualNotification.setVendorStatus("PGE Manual Notification");
            manualNotification.setCreationTimestamp(new Date());
        }
    }

    public void saveManualNotification() {
        entityManager.persist(manualNotification);
        getPdpSrParticipantVendorNotifications().add(manualNotification);
        manualNotification = null;
    }

    public void cancelManualNotification() {
        manualNotification = null;
    }

    @PostConstruct
    public void onCreate() {
        eventId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        pdpSrEvents = eventStatisticService.getEvents(authorizationChecker.getViewStatistics());
        log.info("eventId "+eventId);
        if (eventId != null) {
            setPdpSrEvent(entityManager.find(PdpSrEvent.class, eventId));
            loadEventData();
        }
        notificationMethod.add("PHONE");
        notificationMethod.add("EMAIL");
        notificationMethod.add("SMS");
        notificationMethod.add("OTHER");
        successType.add("Success");
        successType.add("Fail");
        //TODO take this from the database, it is static data so no problem for long living cache
        vendorsList.add("Genesys");
        vendorsList.add("Rightfax");
        vendorsList.add("Manual");
    }

    public void onRowSelectEvent(SelectEvent event) throws IOException {
        if (pdpSrEvent == null) {
            setPdpSrEvent((PdpSrEvent) event.getObject());
        }
        loadEventData();
    }

    private void loadEventData() {
        participantLazyModel = new LazyParticipantDataModel(this, pdpSrEvent);
        vendorNotificationLazyModel = new LazyNotificationDataModel(this, pdpSrEvent, NotificationType.VENDOR);
        unsuccessfulNotificationLazyModel = new LazyNotificationDataModel(this, pdpSrEvent, NotificationType.UNSUCCESSFUL);
        addEventInfo();
        if (authorizationChecker.getViewStatistics()) {
            log.info("loading event data");
            eventNotificationSummary = eventStatisticService.getEventNotificationSummary(pdpSrEvent);
        }
    }

    public void onRowSelectParticipant(SelectEvent event) throws IOException {
        pdpSrEvent = entityManager.find(PdpSrEvent.class, pdpSrEvent.getEventId());
        if (pdpSrParticipant == null) {
            setPdpSrParticipant((PdpSrParticipant) event.getObject());
        }
        participantRequestedNotificationLazyModel = new LazyNotificationDataModel(this, pdpSrEvent, NotificationType.PARTICIPANT_REQUESTED, pdpSrParticipant);

        pdpSrParticipantVendorNotifications = eventStatisticService.getParticipantVendorNotifications(pdpSrParticipant, pdpSrEvent);

        addEventInfo();
        addEventParticipantInfo();
    }

    public void onRowSelectNotificationParticipant(SelectEvent event) throws IOException {
        pdpSrEvent = entityManager.find(PdpSrEvent.class, pdpSrEvent.getEventId());
        PdpSrNotification not = (PdpSrNotification) event.getObject();
        if (not != null) {
            setPdpSrParticipant(not.getPdpSrParticipant());
        }
        setPdpSrNotification(null);
        participantRequestedNotificationLazyModel = new LazyNotificationDataModel(this, pdpSrEvent, NotificationType.PARTICIPANT_REQUESTED, pdpSrParticipant);
        pdpSrParticipantVendorNotifications = eventStatisticService.getParticipantVendorNotifications(pdpSrParticipant, pdpSrEvent);
        addEventInfo();
        addEventParticipantInfo();
    }

    public void onRowSelectNotification(SelectEvent event) throws IOException {
        pdpSrParticipant = null;
        pdpSrEvent = entityManager.find(PdpSrEvent.class, pdpSrEvent.getEventId());
        if (pdpSrNotification == null) {
            setPdpSrNotification((PdpSrNotification) event.getObject());
        }
        enpNotificationLazyModel = new LazyNotificationDataModel(this, pdpSrEvent, NotificationType.ENP, pdpSrNotification.getNotifyByValue());
        addEventInfo();
        addEventNotificationInfo();
    }

    public void handleNotificationRender() {
        clearParticipant();
        setPdpSrNotification(null);
        if (authorizationChecker.getViewStatistics()) {
            setActiveTab(2);
        } else {
            setActiveTab(1);
        }
    }

    /**
     * Used for debugging reRenders. If time changes it got re-rendered
     */
    public String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        return sdf.format(new Date());
    }

    public List<PdpSrEvent> getPdpSrEvents() {
        if (pdpSrEvents == null || pdpSrEvents.isEmpty()) {
            pdpSrEvents = eventStatisticService.getEvents(authorizationChecker.getViewStatistics());
        }
        return pdpSrEvents;
    }

    public void setPdpSrEvents(List<PdpSrEvent> pdpSrEvents) {
        this.pdpSrEvents = pdpSrEvents;
    }

    public void clearParticipant() {
        setPdpSrParticipant(null);
        addEventInfo();
    }

    public void setPdpSrParticipant(PdpSrParticipant pdpSrParticipant) {
        if (pdpSrParticipant != null) {
            this.pdpSrParticipant = entityManager.find(PdpSrParticipant.class, pdpSrParticipant.getParticipantId());
        } else {
            this.pdpSrParticipant = null;
        }
    }

    public void setNotificationForPdpSrParticipant(PdpSrNotification notificationForPdpSrParticipant) {
        this.notificationForPdpSrParticipant = notificationForPdpSrParticipant;
        setPdpSrParticipant(null);
    }

    @Getter
    public class EventInfo {
        private String label;
        private String value;
        private String icon;

        EventInfo(String label, String value, String icon) {
            this.label = label;
            this.value = value;
            this.icon = icon;
        }
    }
}