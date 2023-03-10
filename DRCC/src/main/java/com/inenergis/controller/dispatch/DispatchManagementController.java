package com.inenergis.controller.dispatch;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.events.TimelineHelper;
import com.inenergis.controller.lazyDataModel.LazyEventModel;
import com.inenergis.entity.Event;
import com.inenergis.entity.genericEnum.DispatchLevel;
import com.inenergis.entity.genericEnum.EventStatus;
import com.inenergis.entity.genericEnum.EventType;
import com.inenergis.entity.program.ImpactedCustomer;
import com.inenergis.entity.program.ImpactedResource;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramOption;
import com.inenergis.service.EventService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.PickListElement;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.timeline.TimelineUpdater;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.inenergis.controller.dispatch.EventDetailsController.ERROR_SAVING_EVENT;

@Named
@ViewScoped
@Getter
@Setter
public class DispatchManagementController implements Serializable {



    @Inject
    private EventService eventService;

    @Inject
    private ProgramServiceContract programService;

    @Inject
    private UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(DispatchManagementController.class);

    @Inject
    EntityManager entityManager;

    private LazyEventModel eventLazyModel;

    private DualListModel<PickListElement> dispatchLevelPickList = new DualListModel<>();
    private List<PickListElement> dispatchLevelList = new ArrayList<>();
    private String dispatchLevelCommaSeparated;

    private List<DispatchLevel> programDispatchLevels;

    private List<ProgramOption> programOptionList = new ArrayList<>();

    private Long totalHours;
    private Long totalMinutes;

    private String password = "";

    private EventType eventTypeFilter;
    private Map<String, Object> filter = new HashMap<>();

    private List<ImpactedCustomer> impactedCustomers = new ArrayList<>();
    private Set<ImpactedResource> impactedResources = new HashSet<>();
    private List<Program> programList = new ArrayList<>();

    private TimelineModel timelineModel = new TimelineModel();

    private boolean showTimeline = true;

    private String newPlanName;
    private Date newPlanStartDate;
    private Date newPlanEndDate;
    private String newPlanProgramName;

    private long fixedZoom = 86400000L;

    // one day in milliseconds for zoomMin
    private long zoomMin = 1000L * 60 * 60 * 24;

    // about three months in milliseconds for zoomMax
    private long zoomMax = 1000L * 60 * 60 * 24 * 3;

    private Date timelineEndDate = new Date();
    private Date timelineStartDate = new Date();

    private Event selectedEvent;

    private boolean showNewPlanDialog = false;

    @PostConstruct
    public void init() {
        programList = programService.getAllWithActiveProfile();
        eventTypeFilter = EventType.SCHEDULED;
        initTimeLine();
        search();
    }

    private void initTimeLine() {
        final List<Program> programs = programService.getPrograms();
        if (CollectionUtils.isEmpty(programs)) {
            return;
        }
        final List<Event> events = eventService.getAllForYearInDate(programs, new Date());
        final Map<String, List<Event>> eventsByProgram = events.stream().collect(Collectors.groupingBy(e -> e.getProgram().getName()));
        final List<String> allProgramNames = programList.stream().map(p -> p.getName()).collect(Collectors.toList());
        Date maxEventDate = TimelineHelper.populateTimeline(timelineModel, eventsByProgram, allProgramNames);
        if (maxEventDate == null) {
            maxEventDate = new Date();
        }
        final LocalDateTime localMaxEventDate = LocalDateTime.from(maxEventDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID));
        timelineEndDate = Date.from(localMaxEventDate.plusDays(7).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        timelineStartDate = Date.from(localMaxEventDate.minusDays(7).atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
    }

    public void onSelectTimeline(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();
        selectedEvent = ((TimelineHelper.Task) timelineEvent.getData()).getEvent();
        filter.clear();
        filter.put("id", selectedEvent.getId());
        eventTypeFilter = selectedEvent.getEventType();
        eventLazyModel = new LazyEventModel(entityManager, filter);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('eventsWdgt').filter();");
    }

    public void search() {
        filter.put("TYPE", eventTypeFilter);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('eventsWdgt').filter();");
        filter.remove("id");
        eventLazyModel = new LazyEventModel(entityManager, filter);
    }

    public void addNewPlan() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String program = params.get("program");
        if (StringUtils.isNotEmpty(program)) {
            newPlanProgramName = programList.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(program))
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .getName();
        }
        showNewPlanDialog = true;
    }

    public void saveNewPlan() {
        TimelineUpdater timelineUpdater = TimelineUpdater.getCurrentInstance("form:eventsTimeLine");
        Event newPlan = new Event();

        newPlan.setStartDate(newPlanStartDate);
        newPlan.setEndDate(newPlanEndDate);
        newPlan.setName(newPlanName);
        newPlan.setStatus(EventStatus.PLANNED);
        newPlan.setProgram(programList.stream()
                .filter(p -> p.getName().equalsIgnoreCase(newPlanProgramName))
                .findFirst()
                .orElseThrow(RuntimeException::new));
        TimelineHelper.addPlanToTimeline(timelineModel, newPlan, newPlan.getProgram().getName(), timelineUpdater);
        try {
            eventService.saveOrUpdate(newPlan);
        } catch (IOException e) {
            uiMessage.addMessage(ERROR_SAVING_EVENT);
            log.warn(ERROR_SAVING_EVENT, e);
        }
        showNewPlanDialog = false;
    }

    public void addEventToReserve(Event event) throws IOException {
        event.setStatus(EventStatus.RESERVED);
        eventService.saveOrUpdate(event);
        FacesContext.getCurrentInstance().getExternalContext().redirect("ScheduleEvent.xhtml?o=" + ParameterEncoderService.encode(event.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void onSelectEvent(SelectEvent event) throws IOException {
        Event dispatchedEvent = (Event) event.getObject();
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("EventDispatchDetail.xhtml?o=" + ParameterEncoderService.encode(dispatchedEvent.getId()));
        FacesContext.getCurrentInstance().responseComplete();
    }
}