package com.inenergis.controller.masterCalendar;

import com.inenergis.entity.genericEnum.TimeOfUseDayType;
import com.inenergis.entity.masterCalendar.TimeOfUse;
import com.inenergis.entity.masterCalendar.TimeOfUseCalendar;
import com.inenergis.entity.masterCalendar.TimeOfUseDay;
import com.inenergis.entity.masterCalendar.TimeOfUseHour;
import com.inenergis.service.TimeOfUseCalendarService;
import com.inenergis.service.TimeOfUseService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
@ViewScoped
@Getter
@Setter
public class SeasonCalendarController implements Serializable {

    @Inject
    TimeOfUseCalendarService timeOfUseCalendarService;

    @Inject
    TimeOfUseService timeOfUseService;

    @Inject
    UIMessage uiMessage;

    @Inject
    EntityManager entityManager;

    Logger log = LoggerFactory.getLogger(SeasonCalendarController.class);


    public final static int ONE_HOUR_IN_MILLIS = 3_600_000;
    public final static int EIGHT_MINUTES_GAP_IN_MILLIS = 480_000;
    public final static int MIN_HOUR = 0;
    public final static int MAX_HOUR = 23;

    private boolean editMode = false;
    private TimeOfUseCalendar timeOfUseCalendar;

    private List<TimeOfUseCalendar> timeOfUseCalendars;

    private TimelineModel timelineModel;
    private Date timelineStart;
    private Date timelineEnd;

    private boolean viewModeOn = false;

    private TimeOfUse timeOfUse;
    private List<Integer> hours = IntStream.range(1, 25).boxed().collect(Collectors.toList());
    private List<Integer> selectedHours = new ArrayList<>();
    private boolean weekdaysIncluded = false;
    private boolean weekendsIncluded = false;
    private boolean timeOfUseEditMode = false;

    @PostConstruct
    public void init() {
        timeOfUseCalendars = timeOfUseCalendarService.getAll();

        timelineStart = new Date(0);
        timelineEnd = new Date(ONE_HOUR_IN_MILLIS * 23);

        timelineModel = new TimelineModel();

        for (TimeOfUseCalendar timeOfUseCalendar : timeOfUseCalendars) {
            for (TimeOfUse timeOfUse : timeOfUseCalendar.getTimeOfUses()) {
                timelineModel.addAll(createTimelineEvent(timeOfUse, timeOfUseCalendar, TimeOfUseDayType.WEEK_DAYS));
                timelineModel.addAll(createTimelineEvent(timeOfUse, timeOfUseCalendar, TimeOfUseDayType.WEEK_ENDS));
            }
        }
    }

    private List<TimelineEvent> createTimelineEvent(TimeOfUse timeOfUse, TimeOfUseCalendar timeOfUseCalendar, TimeOfUseDayType timeOfUseDayType) {
        List<TimelineEvent> timelineEvents = new ArrayList<>();

        List<Integer> hours = timeOfUse.getTimeOfUseHours().stream()
                .filter(timeOfUseHour -> timeOfUseHour.getTimeOfUse().getTimeOfUseDays().stream().map(TimeOfUseDay::getDay).collect(Collectors.toList()).contains(timeOfUseDayType))
                .mapToInt(TimeOfUseHour::getHour)
                .boxed()
                .collect(Collectors.toList());

        int lastHour = hours.stream().mapToInt(i -> i).max().orElse(0);
        Integer minHour = null;
        Integer maxHour = null;

        for (int hour : hours) {
            if (minHour == null) {
                minHour = hour;
            }
            if (maxHour == null || hour == lastHour) {
                maxHour = hour;
            }

            if (hour == lastHour || hour > maxHour + 1) {
                Date start = new Date(ONE_HOUR_IN_MILLIS * (minHour - 1) + EIGHT_MINUTES_GAP_IN_MILLIS);
                Date end = new Date(ONE_HOUR_IN_MILLIS * (maxHour - 1) - EIGHT_MINUTES_GAP_IN_MILLIS);
                String label = timeOfUse.getTou().getName() + " " + minHour + " - " + maxHour;
                TimelineEvent event = new TimelineEvent(label, start, end, true, timeOfUseCalendar.getName() + " (" + timeOfUseDayType.getName() + ")", timeOfUse.getTou().toString());
                event.setStyleClass(timeOfUse.getId() + " " + timeOfUse.getTou().toString());
                timelineEvents.add(event);

                minHour = hour;
            }

            maxHour = hour;
        }

        return timelineEvents;
    }

    public void save() {
        timeOfUseCalendarService.saveOrUpdate(timeOfUseCalendar);
        init();
        clear();
    }

    public void clear() {
        editMode = false;
    }

    public void add() {
        timeOfUseCalendar = new TimeOfUseCalendar();
        editMode = true;
    }

    public void update(TimeOfUseCalendar timeOfUseCalendar) {
        this.timeOfUseCalendar = timeOfUseCalendar;
        editMode = true;
    }

    public void goToSeasons(TimeOfUseCalendar timeOfUseCalendar) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("Seasons.xhtml?o=" + timeOfUseCalendar.getId());
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void editTimeOfUse(TimeOfUse timeOfUse) {
        weekdaysIncluded = false;
        weekendsIncluded = false;
        this.timeOfUse = timeOfUse;
        selectedHours = timeOfUse.getTimeOfUseHours().stream().mapToInt(TimeOfUseHour::getHour).boxed().collect(Collectors.toList());
        timeOfUse.getTimeOfUseDays().forEach(timeOfUseDay -> {
            switch (timeOfUseDay.getDay()) {
                case WEEK_DAYS:
                    weekdaysIncluded = true;
                    break;
                case WEEK_ENDS:
                    weekendsIncluded = true;
            }
        });
        viewModeOn = false;
        timeOfUseEditMode = true;
    }

    public void saveTimeOfUse() {
        timeOfUse.setTimeOfUseHours(selectedHours.stream().map(hour -> new TimeOfUseHour(hour.shortValue(), timeOfUse)).collect(Collectors.toList()));
        timeOfUse.getTimeOfUseDays().clear();

        if (weekdaysIncluded) {
            timeOfUse.getTimeOfUseDays().add(new TimeOfUseDay(TimeOfUseDayType.WEEK_DAYS, timeOfUse));
        }
        if (weekendsIncluded) {
            timeOfUse.getTimeOfUseDays().add(new TimeOfUseDay(TimeOfUseDayType.WEEK_ENDS, timeOfUse));
        }

        if (timeOfUse.getId() == null) {
            timeOfUseCalendar.getTimeOfUses().add(timeOfUse);
        }

        timeOfUse = timeOfUseService.saveOrUpdate(timeOfUse);
        timeOfUseEditMode = false;
        init();
    }

    public void addTimeOfUse(TimeOfUseCalendar timeOfUseCalendar) {
        weekdaysIncluded = false;
        weekendsIncluded = false;
        selectedHours.clear();
        this.timeOfUseCalendar = timeOfUseCalendar;
        timeOfUse = new TimeOfUse(timeOfUseCalendar);
        timeOfUse.setTimeOfUseDays(new ArrayList<>());
        timeOfUseEditMode = true;
    }

    public void cancelTimeOfUse() {
        timeOfUseEditMode = false;
    }

    public void onTimelineClick(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();
        Long timeOfUseId = Long.valueOf(timelineEvent.getStyleClass().replaceAll("[^0-9]", ""));
        TimeOfUse timeOfUse = timeOfUseService.getById(timeOfUseId);

        for (TimeOfUseCalendar calendar : timeOfUseCalendars) {
            if (calendar.getTimeOfUses().contains(timeOfUse)) {
                calendar.setExpandedRow(true);
            } else {
                calendar.setExpandedRow(false);
            }
        }
    }
}