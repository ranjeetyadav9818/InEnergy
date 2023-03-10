package com.inenergis.controller.events;


import com.inenergis.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.timeline.TimelineUpdater;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineGroup;
import org.primefaces.model.timeline.TimelineModel;

import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
public class TimelineHelper {

    public static void addPlanToTimeline(TimelineModel timelineModel, Event event, String group, TimelineUpdater timelineUpdater) {
        TimelineHelper.Task task = new TimelineHelper.Task(event.getName(), null, event);//
        final TimelineEvent timeline = new TimelineEvent(task, event.getStartDate(), null, false, group);
        setClassForStatus(event, timeline);
        timelineModel.add(timeline, timelineUpdater);
    }

    @Data
    @AllArgsConstructor
    public static class Task implements Serializable {

        private String title;
        private String imagePath;
        private Event event;

    }

    public static Date populateTimeline(TimelineModel timelineModel, Map<String, List<Event>> eventsByProgram, List<String> allPrograms) {

        Event maxEvent = null;
        final List<String> programsWithoutEvents = allPrograms.stream().filter(p -> !eventsByProgram.containsKey(p)).collect(Collectors.toList());
        for (String programWithoutEvents : programsWithoutEvents) {
            TimelineGroup programGroup = new TimelineGroup(programWithoutEvents, programWithoutEvents);
            timelineModel.addGroup(programGroup);
            final TimelineEvent fakeEvent = new TimelineEvent(null, getDateOutOfTheTimeline(), false, programWithoutEvents);
            timelineModel.add(fakeEvent);
        }
        for (Map.Entry<String, List<Event>> entry : eventsByProgram.entrySet()) {
            TimelineGroup programGroup = new TimelineGroup(entry.getKey(), entry.getKey());
            timelineModel.addGroup(programGroup);
            for (Event event : entry.getValue()) {
                if (maxEvent != null) {
                    if (event.getStartDate().compareTo(maxEvent.getStartDate()) > 0) {
                        maxEvent = event;
                    }
                } else {
                    maxEvent = event;
                }
                final String name = StringUtils.isEmpty(event.getName())? event.getDispatchLevel().getName() : event.getName();
                TimelineHelper.Task task = new TimelineHelper.Task(name, null, event);//<----put the images here
                final TimelineEvent timeline = new TimelineEvent(task, event.getStartDate(), null, false, entry.getKey());
                setClassForStatus(event, timeline);
                timelineModel.add(timeline);
            }
        }
        if (maxEvent != null) {
            return maxEvent.getStartDate();
        }

        return null;
    }

    private static Date getDateOutOfTheTimeline() {
        LocalDateTime localDate = LocalDateTime.of(100000,1,1,0,0);
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void setClassForStatus(Event event, TimelineEvent timeline) {
        if (event.getStatus() == null) {
            timeline.setStyleClass("event-timeline-error");
            return;
        }
        switch (event.getStatus()) {
            case SUBMITTED:
                timeline.setStyleClass("event-timeline-submitted");
                break;
            case ERROR:
                timeline.setStyleClass("event-timeline-error");
                break;
            case CANCELLED:
                timeline.setStyleClass("event-timeline-cancelled");
                break;
            case TERMINATED:
                timeline.setStyleClass("event-timeline-terminated");
                break;
            case PLANNED:
                timeline.setStyleClass("event-timeline-planned");
                break;
            case RESERVED:
                timeline.setStyleClass("event-timeline-reserved");
                break;
            default:
                timeline.setStyleClass("event-timeline-error");
        }
    }

}
