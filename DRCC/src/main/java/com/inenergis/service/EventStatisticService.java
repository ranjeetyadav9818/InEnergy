package com.inenergis.service;

import com.inenergis.dao.Layer7PeakDemandHistoryDao;
import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrParticipant;
import com.inenergis.entity.PdpSrVendor;
import com.inenergis.model.EventNotificationSummary;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Getter
public class EventStatisticService {

    @Inject
    Layer7PeakDemandHistoryDao layer7PeakDemandHistoryDao;

    @Inject
    EntityManager entityManager;

//    private List<EventStatistic> eventStatistics = new ArrayList<>();
//    private Map<String, VendorGraph> vendorsGraphs = new LinkedHashMap<>();
//    private PdpSrEvent pdpSrEvent;
//    private Totals totals;
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private Totals init(PdpSrEvent pdpSrEvent, List<EventStatistic> eventStatistics, Map<String, VendorGraph> vendorsGraphs ) {
        List<Object[]> stats = entityManager.createQuery("SELECT n.pdpSrEvent, n.pdpSrVendor, n.notifyBy, n.language, count(*) "
                + "FROM PdpSrNotification n "
                + "WHERE n.pdpSrEvent = :event "
                + "GROUP BY n.pdpSrEvent, n.pdpSrVendor, n.notifyBy, n.language")
                .setParameter("event", pdpSrEvent)
                .getResultList();
        for (Object[] obj : stats) {
            EventStatistic eventStat = new EventStatistic();
            PdpSrVendor pdpSrVendor = (PdpSrVendor) obj[1];
            eventStat.setVendor(pdpSrVendor.getVendor());
            eventStat.setChannel((String) obj[2]);
            eventStat.setLanguage((String) obj[3]);
            eventStat.setReceived((Long) obj[4]);
            Long l1 = (Long) entityManager.createQuery("SELECT count(*) "
                    + "FROM PdpSrNotification n "
                    + "WHERE n.pdpSrEvent = :event "
                    + "AND n.successfulNotification = :deliv "
                    + "AND n.vendorStatus != :duplicate "
                    + "AND n.pdpSrVendor = :vendor "
                    + "AND n.notifyBy = :notifyBy "
                    + "AND n.language = :language")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("vendor", pdpSrVendor)
                    .setParameter("notifyBy", eventStat.getChannel())
                    .setParameter("language", eventStat.getLanguage())
                    .setParameter("deliv", PdpSrParticipant.SuccessfulNotificationType.DELIVERED)
                    .setParameter("duplicate", "DUPLICATE")
                    .getSingleResult();
            Long l2 = (Long) entityManager.createQuery("SELECT count(*) "
                    + "FROM PdpSrNotification n "
                    + "WHERE n.pdpSrEvent = :event "
                    + "AND n.successfulNotification = :attempt "
                    + "AND n.vendorStatus != :duplicate "
                    + "AND n.pdpSrVendor = :vendor "
                    + "AND n.notifyBy = :notifyBy "
                    + "AND n.language = :language")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("vendor", pdpSrVendor)
                    .setParameter("notifyBy", eventStat.getChannel())
                    .setParameter("language", eventStat.getLanguage())
                    .setParameter("attempt", PdpSrParticipant.SuccessfulNotificationType.ATTEMPTED)
                    .setParameter("duplicate", "DUPLICATE")
                    .getSingleResult();
            Long l3 = (Long) entityManager.createQuery("SELECT count(*) "
                    + "FROM PdpSrNotification n "
                    + "WHERE n.pdpSrEvent = :event "
                    + "AND n.vendorStatus = :duplicate "
                    + "AND n.pdpSrVendor = :vendor "
                    + "AND n.notifyBy = :notifyBy "
                    + "AND n.language = :language")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("vendor", pdpSrVendor)
                    .setParameter("notifyBy", eventStat.getChannel())
                    .setParameter("language", eventStat.getLanguage())
                    .setParameter("duplicate", "DUPLICATE")
                    .getSingleResult();
            Long l4 = (Long) entityManager.createQuery("SELECT count(*) "
                    + "FROM PdpSrNotification n "
                    + "WHERE n.pdpSrEvent = :event "
                    + "AND n.vendorStatus = :sent "
                    + "AND n.pdpSrVendor = :vendor "
                    + "AND n.notifyBy = :notifyBy "
                    + "AND n.language = :language")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("vendor", pdpSrVendor)
                    .setParameter("notifyBy", eventStat.getChannel())
                    .setParameter("language", eventStat.getLanguage())
                    .setParameter("sent", "SENT TO VENDOR")
                    .getSingleResult();
            logger.info("1:{} 2:{} 3:{} 4:{}",l1,l2,l3,l4);
            if (eventStat.getReceived() != null && eventStat.getReceived() > 0) {
                if (l3 != null && l3 > 0) {
                    eventStat.setDispatched(eventStat.getReceived() - l3);
                } else {
                    eventStat.setDispatched(eventStat.getReceived());
                }
                if (l4 != null && l4 > 0) {
                    eventStat.setInProgress(l4);
                } else {
                    eventStat.setInProgress(l4);
                }
                eventStat.setDelivered(l1);
                if (eventStat.getDelivered() > 0) {
                    eventStat.setDeliveredPercent(new BigDecimal(l1 * 100).divide(new BigDecimal(eventStat.getDispatched()), 2, RoundingMode.HALF_UP));
                } else {
                    eventStat.setDeliveredPercent(new BigDecimal(0));
                }
                eventStat.setAttempted(l2);
                if (eventStat.getAttempted() > 0) {
                    eventStat.setAttemptedPercent(new BigDecimal(l2 * 100).divide(new BigDecimal(eventStat.getDispatched()), 2, RoundingMode.HALF_UP));
                } else {
                    eventStat.setAttemptedPercent(new BigDecimal(0));
                }
                eventStat.setDuplicates(l3);
                if (eventStat.getDuplicates() > 0) {
                    eventStat.setDuplicatePercent(new BigDecimal(l3 * 100).divide(new BigDecimal(eventStat.getReceived()), 2, RoundingMode.HALF_UP));
                } else {
                    eventStat.setDuplicatePercent(new BigDecimal(0));
                }
            } else {
                eventStat.setDelivered(0L);
                eventStat.setDeliveredPercent(new BigDecimal(0));
                eventStat.setAttempted(0L);
                eventStat.setAttemptedPercent(new BigDecimal(0));
                eventStat.setDuplicates(0L);
                eventStat.setDuplicatePercent(new BigDecimal(0));
                eventStat.setInProgress(0L);
            }
            eventStatistics.add(eventStat);
            addToVendorsGraphs(eventStat,vendorsGraphs);
        }



        printVendorsGraph(vendorsGraphs);
        Totals totals = new Totals(eventStatistics);

        logger.info("stats "+stats+" for event "+ (pdpSrEvent == null ? StringUtils.EMPTY: pdpSrEvent.getEventId()) +" totals "+totals.getNotificationsTotal());
        return totals;
    }

    private void addToVendorsGraphs(EventStatistic eventStat, Map<String, VendorGraph> vendorsGraphs) {
        if (!vendorsGraphs.containsKey(eventStat.getVendor())) {
            vendorsGraphs.put(eventStat.getVendor(), new VendorGraph());
        }
        final VendorGraph vendorGraph = vendorsGraphs.get(eventStat.getVendor());
        long number = 0L;
        if (vendorGraph.getDeliveredMap().containsKey(eventStat.getChannel())) {
            number = vendorGraph.getDeliveredMap().get(eventStat.getChannel());
        }
        vendorGraph.getDeliveredMap().put(eventStat.getChannel(), number + eventStat.getDelivered());
        number = 0L;
        if (vendorGraph.getFailedMap().containsKey(eventStat.getChannel())) {
            number = vendorGraph.getFailedMap().get(eventStat.getChannel());
        }
        vendorGraph.getFailedMap().put(eventStat.getChannel(), number + eventStat.getFailed());
        number = 0L;
        if (vendorGraph.getDuplicateMap().containsKey(eventStat.getChannel())) {
            number = vendorGraph.getDuplicateMap().get(eventStat.getChannel());
        }
        vendorGraph.getDuplicateMap().put(eventStat.getChannel(), number + eventStat.getDuplicates());
        number = 0L;
        if (vendorGraph.getInProgressMap().containsKey(eventStat.getChannel())) {
            number = vendorGraph.getInProgressMap().get(eventStat.getChannel());
        }
        vendorGraph.getInProgressMap().put(eventStat.getChannel(), number + eventStat.getInProgress());
    }

    @Getter
    @Setter
    public class VendorGraph {
        private MeterGaugeChartModel meterGaugeModel;
        private Map<String, Long> duplicateMap;
        private Map<String, Long> deliveredMap;
        private Map<String, Long> failedMap;
        private Map<String, Long> inProgressMap;
        private HorizontalBarChartModel barModel;

        VendorGraph() {
            duplicateMap = new HashMap<>();
            deliveredMap = new HashMap<>();
            failedMap = new HashMap<>();
            inProgressMap = new HashMap<>();
        }
    }

    @Getter
    @Setter
    @ToString
    public class Totals {
        private Long notificationsTotal;
        private Long duplicatesTotal;
        private Long dispatchedTotal;
        private Long inProgressTotal;
        private Long deliveredTotal;
        private Long attemptedTotal;
        private BigDecimal duplicatesTotalPercent;
        private BigDecimal deliveredTotalPercent;
        private BigDecimal attemptedTotalPercent;

        public Totals(List<EventStatistic> eventStatistics) {
            notificationsTotal = eventStatistics.stream().mapToLong(EventStatistic::getReceived).sum();
            duplicatesTotal = eventStatistics.stream().mapToLong(EventStatistic::getDuplicates).sum();
            dispatchedTotal = eventStatistics.stream().mapToLong(EventStatistic::getDispatched).sum();
            inProgressTotal = eventStatistics.stream().mapToLong(EventStatistic::getInProgress).sum();
            deliveredTotal = eventStatistics.stream().mapToLong(EventStatistic::getDelivered).sum();
            attemptedTotal = eventStatistics.stream().mapToLong(EventStatistic::getAttempted).sum();

            if (notificationsTotal == 0) {
                duplicatesTotalPercent = new BigDecimal(0);
            } else {
                duplicatesTotalPercent = new BigDecimal(duplicatesTotal * 100).divide(new BigDecimal(notificationsTotal), 2, RoundingMode.HALF_UP);
            }

            if (dispatchedTotal == 0) {
                deliveredTotalPercent = new BigDecimal(0);
            } else {
                deliveredTotalPercent = new BigDecimal(deliveredTotal * 100).divide(new BigDecimal(dispatchedTotal), 2, RoundingMode.HALF_UP);
            }

            if (dispatchedTotal == 0) {
                attemptedTotalPercent = new BigDecimal(0);
            } else {
                attemptedTotalPercent = new BigDecimal(attemptedTotal * 100).divide(new BigDecimal(dispatchedTotal), 2, RoundingMode.HALF_UP);
            }
        }
    }

    @Getter
    @Setter
    @ToString
    public class EventStatistic {
        private String vendor;
        private String channel;
        private String language;
        private Long received;
        private Long duplicates;
        private BigDecimal duplicatePercent;
        private Long dispatched;
        private Long inProgress;
        private Long delivered;
        private BigDecimal deliveredPercent;
        private Long attempted;
        private BigDecimal attemptedPercent;

        public long getFailed() {
            return dispatched - delivered - inProgress;
        }
    }

    private void printVendorsGraph(Map<String, VendorGraph> vendorsGraphs) {
        for (Map.Entry<String, VendorGraph> vendorEntry : vendorsGraphs.entrySet()) {
            final VendorGraph vendorGraph = vendorEntry.getValue();
            final Map<String, BigDecimal> successRates = getSuccessRates(vendorGraph);
            final MeterGaugeChartModel meterGaugeModel = new MeterGaugeChartModel(successRates.get("Total"), initMeterGaugeModel());
            meterGaugeModel.setTitle(vendorEntry.getKey() + " Performance");
            meterGaugeModel.setGaugeLabel("KPI success (%)");
            meterGaugeModel.setSeriesColors("cc6666,66cc66");
            meterGaugeModel.setMax(100);
            meterGaugeModel.setMin(0);
            meterGaugeModel.setExtender("mgExtender");
            vendorGraph.setMeterGaugeModel(meterGaugeModel);

            final HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();
            horizontalBarModel.setTitle(vendorEntry.getKey() + " KPI success by channel");
            horizontalBarModel.setLegendPosition("e");
            horizontalBarModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
            horizontalBarModel.setBarWidth(20);
            horizontalBarModel.getAxis(AxisType.X).setMax(100);
            horizontalBarModel.setDatatipFormat("<span>%s %</span><span style='display:none;'>%s</span>");
            vendorGraph.setBarModel(horizontalBarModel);
            ChartSeries chartSeries = new ChartSeries("KPI success (%)");
            for (Map.Entry<String, BigDecimal> channelSuccesRate : successRates.entrySet()) {
                if (!channelSuccesRate.getKey().equals("Total")) {
                    chartSeries.set(channelSuccesRate.getKey(), channelSuccesRate.getValue());
                }
            }
            horizontalBarModel.addSeries(chartSeries);
            horizontalBarModel.setSeriesColors("BBDEFB");
            horizontalBarModel.setExtender("transparentBackgroundChartExtender");
            vendorGraph.setBarModel(horizontalBarModel);
        }
    }

    private Map<String, BigDecimal> getSuccessRates(VendorGraph vendorGraph) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        Map<String, Long> totalsByChannel = new HashMap<>();
        long total = 0L;
        long delivered = 0L;
        for (Map.Entry<String, Long> deliveredByChannel : vendorGraph.getDeliveredMap().entrySet()) {
            delivered += deliveredByChannel.getValue();
            total += deliveredByChannel.getValue();
            result.put(deliveredByChannel.getKey(), new BigDecimal(deliveredByChannel.getValue()));
            totalsByChannel.put(deliveredByChannel.getKey(), deliveredByChannel.getValue());
        }
        for (Map.Entry<String, Long> failedByChannel : vendorGraph.getFailedMap().entrySet()) {
            total += failedByChannel.getValue();
            totalsByChannel.put(failedByChannel.getKey(), totalsByChannel.get(failedByChannel.getKey()) + failedByChannel.getValue());
        }
        for (Map.Entry<String, Long> inProgressByChannel : vendorGraph.getInProgressMap().entrySet()) {
            total += inProgressByChannel.getValue();
            totalsByChannel.put(inProgressByChannel.getKey(), totalsByChannel.get(inProgressByChannel.getKey()) + inProgressByChannel.getValue());
            final BigDecimal inProgress = result.get(inProgressByChannel.getKey());
            final Long totalByChannel = totalsByChannel.get(inProgressByChannel.getKey());
            result.put(inProgressByChannel.getKey(), inProgress.multiply(new BigDecimal(100)).divide(new BigDecimal(totalByChannel), 2, RoundingMode.HALF_UP));
        }
        result.put("Total", new BigDecimal(delivered * 100).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP));
        return result;
    }

    private List<Number> initMeterGaugeModel() {
        return new ArrayList<Number>() {{
            add(90);
            add(100);
        }};
    }

    @SuppressWarnings("unchecked")
    public List<PdpSrEvent> getEvents(boolean viewStatistics) {
        List<PdpSrEvent> events = (List<PdpSrEvent>) entityManager.createNamedQuery("PdpSrEvent.findAll").getResultList();
        if (viewStatistics) {
            for (PdpSrEvent event : events) {
                Long l1 = (Long) entityManager.createQuery("SELECT count(*) "
                        + "FROM PdpSrNotification n "
                        + "WHERE n.pdpSrEvent = :event "
                        + "AND n.successfulNotification = :deliv "
                        + "AND n.vendorStatus != :status")
                        .setParameter("event", event)
                        .setParameter("deliv", PdpSrParticipant.SuccessfulNotificationType.DELIVERED)
                        .setParameter("status", "DUPLICATE")
                        .getSingleResult();

                Long l2 = (Long) entityManager.createQuery("SELECT count(*) "
                        + "FROM PdpSrNotification n "
                        + "WHERE n.pdpSrEvent = :event "
                        + "AND n.successfulNotification = :attempt "
                        + "AND n.vendorStatus != :status")
                        .setParameter("event", event)
                        .setParameter("attempt", PdpSrParticipant.SuccessfulNotificationType.ATTEMPTED)
                        .setParameter("status", "DUPLICATE")
                        .getSingleResult();

                Long l3 = (Long) entityManager.createQuery("SELECT count(*) "
                        + "FROM PdpSrNotification n "
                        + "WHERE n.pdpSrEvent = :event "
                        + "AND n.vendorStatus != :status")
                        .setParameter("event", event)
                        .setParameter("status", "DUPLICATE")
                        .getSingleResult();

                if (l3 > 0) {
                    if (l1 > 0) {
                        event.setCountDelivered(new BigDecimal(l1 * 100).divide(new BigDecimal(l3), 2, RoundingMode.HALF_UP));
                    } else {
                        event.setCountDelivered(new BigDecimal(0));
                    }
                    if (l2 > 0) {
                        event.setCountAttempted(new BigDecimal(l2 * 100).divide(new BigDecimal(l3), 2, RoundingMode.HALF_UP));
                    } else {
                        event.setCountAttempted(new BigDecimal(0));
                    }
                } else {
                    event.setCountDelivered(new BigDecimal(0));
                    event.setCountAttempted(new BigDecimal(0));
                }
            }
        }

        return events;
    }

    @SuppressWarnings("unchecked")
    public List<PdpSrNotification> getParticipantVendorNotifications(PdpSrParticipant pdpSrParticipant, PdpSrEvent pdpSrEvent) {
        List<PdpSrNotification> pdpSrParticipantVendorNotifications = new ArrayList<>();

        if (pdpSrParticipant != null) {
            List<PdpSrNotification> notifications = entityManager.createQuery("SELECT psn FROM PdpSrNotification psn " +
                    "WHERE psn.pdpSrEvent = :event " +
                    "AND psn.pdpSrParticipant = :participant " +
                    "AND psn.vendorStatus != :duplicate")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("participant", pdpSrParticipant)
                    .setParameter("duplicate", "DUPLICATE")
                    .getResultList();

            if (!notifications.isEmpty()) {
                pdpSrParticipantVendorNotifications.addAll(notifications);
            }

            List<Long> notIds = entityManager.createQuery("SELECT psn.duplicateOf.notificationId FROM PdpSrNotification psn " +
                    "WHERE psn.pdpSrEvent = :event " +
                    "AND psn.pdpSrParticipant = :participant " +
                    "AND psn.vendorStatus = :duplicate")
                    .setParameter("event", pdpSrEvent)
                    .setParameter("participant", pdpSrParticipant)
                    .setParameter("duplicate", "DUPLICATE")
                    .getResultList();

            List<PdpSrNotification> dupedNotification = new ArrayList<>();

            if (!notIds.isEmpty()) {
                dupedNotification = entityManager.createQuery("SELECT psn FROM PdpSrNotification psn " +
                        "WHERE psn.pdpSrEvent = :event " +
                        "AND psn.notificationId in (:ids) " +
                        "AND psn.vendorStatus != :duplicate")
                        .setParameter("event", pdpSrEvent)
                        .setParameter("ids", notIds)
                        .setParameter("duplicate", "DUPLICATE")
                        .getResultList();
            }
            if (!dupedNotification.isEmpty()) {
                pdpSrParticipantVendorNotifications.addAll(dupedNotification);
            }
        }
        return pdpSrParticipantVendorNotifications;
    }

    private HorizontalBarChartModel getBarChart(List<EventStatistic> eventStatistics) {
        HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries deliveredSeries = new ChartSeries();
        deliveredSeries.setLabel("Delivered");
        ChartSeries inProgressSeries = new ChartSeries();
        inProgressSeries.setLabel("In Progress");
        ChartSeries failedSeries = new ChartSeries();
        failedSeries.setLabel("Failed");
        for (EventStatistic eventStatistic : eventStatistics) {
            String compositeName = eventStatistic.getChannel() + " (" + eventStatistic.getLanguage().substring(0, 2).toUpperCase() + ")";
            deliveredSeries.set(compositeName, new BigDecimal(eventStatistic.getDelivered() * 100).divide(new BigDecimal(eventStatistic.getDispatched()), 2, RoundingMode.HALF_UP));
            inProgressSeries.set(compositeName, new BigDecimal(eventStatistic.getInProgress() * 100).divide(new BigDecimal(eventStatistic.getDispatched()), 2, RoundingMode.HALF_UP));
            BigDecimal failed = new BigDecimal((eventStatistic.getDispatched() - eventStatistic.getDelivered() - eventStatistic.getInProgress()) * 100);
            failedSeries.set(compositeName, failed.divide(new BigDecimal(eventStatistic.getDispatched()), 2, RoundingMode.HALF_UP));
        }
        horizontalBarModel.addSeries(deliveredSeries);
        horizontalBarModel.addSeries(inProgressSeries);
        horizontalBarModel.addSeries(failedSeries);
        horizontalBarModel.setTitle("Comparison by vendor, channel and language");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        horizontalBarModel.getAxis(AxisType.X).setMax(100);
        horizontalBarModel.setDatatipFormat("<span>%s %</span><span style='display:none;'>%s</span>");
        horizontalBarModel.setSeriesColors("66cc66,BBDEFB,cc6666");
        horizontalBarModel.setExtender("transparentBackgroundChartExtender");

        return horizontalBarModel;
    }

    public EventNotificationSummary getEventNotificationSummary(PdpSrEvent pdpSrEvent) {
        EventNotificationSummary eventNotificationSummary = new EventNotificationSummary();
        Map<String, VendorGraph> vendorsGraphs = new HashMap<>();
        List<EventStatistic> eventStatistics = new ArrayList<>();
        Totals totals = init(pdpSrEvent,eventStatistics,vendorsGraphs);
        eventNotificationSummary.setTotals(totals);
        eventNotificationSummary.setVendorsGraphs(vendorsGraphs);
        eventNotificationSummary.setEventStatistics(eventStatistics);
        eventNotificationSummary.setHorizontalBarModel(getBarChart(eventStatistics));
        logger.info("returning event statistics {} and totals {}",eventStatistics,totals);
        return eventNotificationSummary;
    }
}