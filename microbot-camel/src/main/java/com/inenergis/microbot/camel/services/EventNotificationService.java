package com.inenergis.microbot.camel.services;

import com.inenergis.entity.Event;
import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrVendor;
import com.inenergis.entity.program.NotificationDuplicationSource;
import com.inenergis.entity.program.ProgramCustomerNotification;
import com.inenergis.microbot.camel.dao.EventDao;
import com.inenergis.microbot.camel.dao.PdpSrVendorDao;
import com.inenergis.util.VendorFileCreator;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class EventNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EventNotificationService.class);

    @Autowired
    private EventDao eventDao;
    @Autowired
    private PdpSrVendorDao pdpSrVendorDao;

    private VendorFileCreator vendorFileCreator = new VendorFileCreator();

    @Transactional
    public void findAllPendingNotificationEvents(Exchange exchange) throws Exception {
        List<Event> list = eventDao.findByEventNotificationEventState("Scheduled");
        for (Event event : list) {
            Map<String, List<PdpSrNotification>> map = getNotifications(event);
            event.getEventNotification().setNotificationsByChannel(map);
            event.getProgram().getActiveProfile().getCustomerNotifications().forEach(n -> n.getNotificationDuplicationSources().size());
        }
        exchange.getIn().setBody(list);
    }

    private Map<String, List<PdpSrNotification>> getNotifications(Event event) {
        List<PdpSrNotification> allNotifications = event.getEventNotification().getPdpSrParticipants().stream()
                .flatMap(l -> l.getPdpSrNotifications().stream())
                .collect(Collectors.toList());
        return allNotifications.stream().collect(Collectors.groupingBy(PdpSrNotification::getNotifyBy));
    }

    public void assignVendorToNotifications(Exchange exchange) throws Exception {
        Event event = (Event) exchange.getIn().getBody();
        Map<String, List<PdpSrNotification>> notificationsByChannel = event.getEventNotification().getNotificationsByChannel();
        ProgramCustomerNotification programCustomerNotification = event.getProgram().getActiveProfile().getCustomerNotifications().get(0);
        //TODO consider options
        if (programCustomerNotification == null) {
            return;
        }
        List<PdpSrVendor> vendors = getAllVendors();
        for (Map.Entry<String, List<PdpSrNotification>> notificationEntry : notificationsByChannel.entrySet()) {
            PdpSrVendor vendor = findVendor(notificationEntry, programCustomerNotification, vendors);
            if (vendor != null) {
                notificationEntry.getValue().forEach(n -> n.setPdpSrVendor(vendor));
            }
        }
    }

    public void produceFilesToVendors(Exchange exchange) {
        Event event = (Event) exchange.getIn().getBody();
        Map<PdpSrVendor, List<PdpSrNotification>> vendorListMap = event.getEventNotification().getNotificationsByChannel().values().stream().flatMap(c -> c.stream()).filter(notification -> notification.getDuplicateOf() == null && notification.getPdpSrVendor() != null)
                .collect(Collectors.groupingBy(PdpSrNotification::getPdpSrVendor));
        List<Map.Entry<String, String>> vendorFiles = new ArrayList<>();
        for (Map.Entry<PdpSrVendor, List<PdpSrNotification>> vendorListEntry : vendorListMap.entrySet()) {
            String fileContent = vendorFileCreator.createFileContent(vendorListEntry.getKey(), vendorListEntry.getValue());
            String directory = vendorFileCreator.getVendorDirectory(vendorListEntry.getKey());
            vendorFiles.add(ImmutablePair.of(directory, fileContent));
            exchange.getIn().setHeader("VENDOR_FILES", vendorFiles);
        }
        event.getEventNotification().setVendorFilesSent(new Date());
        event.getEventNotification().setEventState("Committed");
    }

    @Transactional
    private List<PdpSrVendor> getAllVendors() {
        return pdpSrVendorDao.findAll();
    }

    private PdpSrVendor findVendor(Map.Entry<String, List<PdpSrNotification>> notificationEntry, ProgramCustomerNotification programCustomerNotification, List<PdpSrVendor> vendors) {
        String vendorName = null;
        for (NotificationDuplicationSource source : programCustomerNotification.getNotificationDuplicationSources()) {
            if (source.getNotificationType().getName().equalsIgnoreCase(notificationEntry.getKey())) {
                vendorName = source.getDistributedBy().getName();
            }
        }
        if (vendorName == null) {
            return null;
        }
        for (PdpSrVendor vendor : vendors) {
            if (vendor.getVendor().equalsIgnoreCase(vendorName)) {
                return vendor;
            }
        }
        return null;
    }
}