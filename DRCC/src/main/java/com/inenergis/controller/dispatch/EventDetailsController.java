package com.inenergis.controller.dispatch;

import com.inenergis.entity.Event;
import com.inenergis.entity.IsoOutage;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.genericEnum.BidStatus;
import com.inenergis.entity.genericEnum.MinutesOrHours;
import com.inenergis.entity.genericEnum.MinutesOrHoursOrDays;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.ProgramCustomerNotification;
import com.inenergis.entity.program.ProgramEventDuration;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.model.EventNotificationSummary;
import com.inenergis.service.BidService;
import com.inenergis.service.EventService;
import com.inenergis.service.EventStatisticService;
import com.inenergis.service.IsoOutageService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.UIMessage;
import com.inenergis.util.soap.ItronClient;
import lombok.Getter;
import lombok.Setter;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.User;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.inenergis.entity.genericEnum.EventStatus.CANCELLED;

@Named
@ViewScoped
@Getter
@Setter
public class EventDetailsController implements Serializable {

    public static final String ERROR_SAVING_EVENT = "Error saving event";
    @Inject
    private EventService eventService;

    @Inject
    private IsoOutageService isoOutageService;

    @Inject
    private BidService bidService;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private PropertyAccessor propertyAccessor;

    @Inject
    private EventStatisticService eventStatisticService;

    private Event event;

    private String cancelPassword;

    private TimelineModel timelineModel = new TimelineModel();

    private Logger log = LoggerFactory.getLogger(EventDetailsController.class);

    private boolean showEventDateChangedDialog = false;

    private Date newStartDate;
    private Date newEndDate;

    private EventNotificationSummary eventNotificationSummary;

    private EventStatisticService.Totals totals;

    @PostConstruct
    public void init() {
        Long eventId = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        event = eventService.getById(eventId);
        if (event.getScheduledDate() != null) {
            timelineModel.add(new TimelineEvent("Scheduled date", event.getScheduledDate()));
        }
        if (event.getStartDate() != null) {
            timelineModel.add(new TimelineEvent("Start date", event.getStartDate()));
        }
        if (event.getEndDate() != null) {
            timelineModel.add(new TimelineEvent("End date", event.getEndDate()));
        }

        List<IsoResource> resources = event.getImpactedResources().stream()
                .map(ImpactedResource::getIsoResource)
                .collect(Collectors.toList());

        Map<IsoResource, IsoOutage> isoOutages = isoOutageService.getByResources(resources, event.getStartDate()).stream()
                .collect(Collectors.toMap(IsoOutage::getIsoResource, isoOutage -> isoOutage));

        event.getImpactedResources().forEach(ir -> ir.setIsoOutage(isoOutages.getOrDefault(ir.getIsoResource(), new IsoOutage())));

        eventNotificationSummary = eventStatisticService.getEventNotificationSummary(event.getEventNotification());
        eventNotificationSummary.setTimelineModel(timelineModel);
    }

    public void cancelDispatch() {
        if (event.getCancelReason() == null) {
            uiMessage.addMessage("Please provide a cancel reason", FacesMessage.SEVERITY_ERROR);
            return;
        }
        String userName = ((User) identity.getAccount()).getLoginName();
        Credentials credentials = new UsernamePasswordCredentials(userName, new Password(cancelPassword));
        identityManager.validateCredentials(credentials);
        if (!Credentials.Status.VALID.equals(credentials.getStatus())) {
            uiMessage.addMessage("Invalid Password", FacesMessage.SEVERITY_ERROR);
            event.setCancelReason(null);
            return;
        }
        event.setStatus(CANCELLED);
        try {
            eventService.saveOrUpdate(event);
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_SAVING_EVENT);
            log.warn(ERROR_SAVING_EVENT, e);
        }
    }

    public void reDispatch() {
        String userName = ((User) identity.getAccount()).getLoginName();
        Credentials credentials = new UsernamePasswordCredentials(userName, new Password(cancelPassword));
        identityManager.validateCredentials(credentials);
        if (!Credentials.Status.VALID.equals(credentials.getStatus())) {
            uiMessage.addMessage("Invalid Password", FacesMessage.SEVERITY_ERROR);
            event.setCancelReason(null);
            return;
        }
        calculateNewEventDates(event);
        if (!validateEventDates(event)) {
            showEventDateChangedDialog = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('eventDateChanged').show();");
            return;
        }
        ItronClient itronClient = new ItronClient(propertyAccessor.getProperties());
        try {
            eventService.issueEventOnItron(itronClient, event, uiMessage);
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_SAVING_EVENT);
            log.warn(ERROR_SAVING_EVENT, e);
        }
    }

    private void calculateNewEventDates(Event event) {
        ProgramProfile activeProfile = event.getProgram().getActiveProfile();
        List<ProgramEventDuration> eventDurations = activeProfile.getEventDurations();
        List<ProgramCustomerNotification> customerNotifications = activeProfile.getCustomerNotifications();

        final LocalDateTime now = LocalDateTime.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        if (customerNotifications != null && !customerNotifications.isEmpty()) {
            ProgramCustomerNotification customerNotification = customerNotifications.get(0);
            Short notificationLeadTime = customerNotification.getNotificationLeadTime();
            MinutesOrHoursOrDays notificationLeadTimeUnits = customerNotification.getNotificationLeadTimeUnits();
            final LocalDateTime localStartDate = now.plus(notificationLeadTime, notificationLeadTimeUnits.getChronoUnit());
            newStartDate = Date.from(localStartDate.atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).withSecond(0).withNano(0).toInstant());
        }
        if (eventDurations != null && !eventDurations.isEmpty()) {
            ProgramEventDuration programEventDuration = eventDurations.get(0);
            final Short maxDuration = programEventDuration.getMaxDuration();
            final MinutesOrHours maxDurationUnits = programEventDuration.getMaxDurationUnits();
            final LocalDateTime localStartDate = LocalDateTime.from(newStartDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
            newEndDate = Date.from(localStartDate.plus(maxDuration, maxDurationUnits.getChronoUnit()).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).withSecond(0).withNano(0).toInstant());
        }
    }

    private boolean validateEventDates(Event event) {
        LocalDateTime eventStartDate = LocalDateTime.from(event.getStartDate().toInstant().atZone(ZoneId.systemDefault()));
        LocalDateTime eventEndDate = LocalDateTime.from(event.getEndDate().toInstant().atZone(ZoneId.systemDefault()));
        LocalDateTime localStartDate = LocalDateTime.from(newStartDate.toInstant().atZone(ZoneId.systemDefault()));
        LocalDateTime localEndDate = LocalDateTime.from(newEndDate.toInstant().atZone(ZoneId.systemDefault()));
        return !(eventStartDate.isBefore(localStartDate) || eventEndDate.isAfter(localEndDate));
    }

    public void acceptNewDatesForReDispatch() {
        showEventDateChangedDialog = false;
        event.setStartDate(newStartDate);
        event.setEndDate(newEndDate);
        ItronClient itronClient = new ItronClient(propertyAccessor.getProperties());
        try {
            eventService.issueEventOnItron(itronClient, event, uiMessage);
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_SAVING_EVENT);
            log.warn(ERROR_SAVING_EVENT, e);
        }
    }

    public void cancelReDispatch() {
        showEventDateChangedDialog = false;
    }

    public void redirectToSelectedResource(IsoResource isoResource) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("ResourceDetails.xhtml?o=" + ParameterEncoderService.encode(isoResource.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void redirectToSelectedServiceAgreement(SelectEvent event) throws IOException {
        ImpactedCustomer impactedCustomer = (ImpactedCustomer) event.getObject();
        String saId = impactedCustomer.getLocationSubmissionStatus().getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId();

        FacesContext.getCurrentInstance().getExternalContext().redirect("CustomerList.xhtml?o=" + ParameterEncoderService.encode(saId));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void updateIsoOutage(CellEditEvent event) throws IOException {
        if (event.getOldValue() != null && event.getNewValue() != null && event.getOldValue().equals(event.getNewValue())) {
            return;
        }

        ImpactedResource impactedResource = this.event.getImpactedResources().stream()
                .filter(resource -> Objects.equals(resource.getId().toString(), event.getRowKey()))
                .findFirst()
                .orElse(null);

        if (impactedResource == null) {
            return;
        }

        IsoOutage isoOutage = isoOutageService.getByImpactedResourceOrDefault(impactedResource, new IsoOutage(impactedResource));

        if (event.getNewValue() == null) {
            isoOutage.setOutageId(null);
            isoOutageService.delete(isoOutage);
        } else {
            isoOutage.setOutageId(event.getNewValue().toString());
            isoOutageService.saveOrUpdate(isoOutage);
        }

        updateBidStatus(isoOutage);
    }

    private void updateBidStatus(IsoOutage isoOutage) {
        Bid bid = bidService.getBy(isoOutage.getIsoResource().getId(), isoOutage.getDate());
        if (bid == null) {
            return;
        }

        if (isoOutage.hasOutage()) {
            bid.setStatus(BidStatus.OUTAGE);
        } else {
            bid.setStatus(BidStatus.ACTION_REQUIRED);
        }

        bidService.save(bid);
    }
}