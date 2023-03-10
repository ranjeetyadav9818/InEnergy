package com.inenergis.model;

import com.inenergis.service.EventStatisticService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.timeline.TimelineModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EventNotificationSummary {
    private HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();
    private Map<String, EventStatisticService.VendorGraph> vendorsGraphs = new LinkedHashMap<>();
    private List<EventStatisticService.EventStatistic> eventStatistics = new ArrayList<>();
    private EventStatisticService.Totals totals;
    private TimelineModel timelineModel = new TimelineModel();
}
