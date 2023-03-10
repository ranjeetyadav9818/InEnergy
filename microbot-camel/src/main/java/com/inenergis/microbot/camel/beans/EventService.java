package com.inenergis.microbot.camel.beans;

import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.PdpSrParticipant;
import com.inenergis.entity.PdpSrVendor;
import com.inenergis.entity.VendorStatusMapping;
import com.inenergis.microbot.camel.csv.Customer;
import com.inenergis.microbot.camel.csv.Detail;
import com.inenergis.microbot.camel.csv.Notification;
import com.inenergis.microbot.camel.csv.Participant;
import com.inenergis.microbot.camel.csv.Postback;
import com.inenergis.microbot.camel.dao.PdpSrEventDao;
import com.inenergis.microbot.camel.dao.PdpSrParticipantDao;
import com.inenergis.microbot.camel.dao.PdpSrVendorDao;
import com.inenergis.microbot.camel.dao.VendorStatusMappingDao;
import com.inenergis.microbot.camel.routes.DrccRouteBuilder;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventService {

    private static final String COMBINED_ADDRESS = "COMBINED_ADDRESS";
    private static final String SECOND_ADDRESS = "SECOND_ADDRESS";
    private static final String ADDRESS_SIZE = "ADDRESS_SIZE";

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private PdpSrEventDao pdpSrEventDao;

    @Autowired
    private PdpSrVendorDao pdpSrVendorDao;

    @Autowired
    private PdpSrParticipantDao pdpSrParticipantDao;

    @Autowired
    private VendorStatusMappingDao vendorStatusMappingDao;

    private ConcurrentHashMap<String, Long> vendorLookup = new ConcurrentHashMap<>();
    private Map<Long, List<VendorConfig>> vendorName = new HashMap<>();
    private Map<String, VendorMapping> vendorMappingMap = new HashMap<>();

    private Long currentEventId;
    private String currentUniqueEventId;
    private String currentProgram;
    private String currentEventState;

    private long customerCount = 0;

    private LocalDateTime timeStamp = LocalDateTime.now();
    private LocalDateTime startTime = null;

    private final Object lock = new Object();

    private Random random = new Random();

    private final Object eventIdLock = new Object();

    public Detail processNewEvent(@Body Detail detail) {
        log.trace("Input " + detail);

        if (detail != null) {
            currentUniqueEventId = detail.getUniqueId();
            currentEventId = null;
            currentProgram = detail.getEventProgram();
            currentEventState = detail.getEventState();
            log.trace("Unique ID is " + currentUniqueEventId);
        }

        vendorLookup.clear();
        vendorName.clear();

        loadVendors();
        return detail;
    }

    public Participant retrieveEventId(@Body Participant participant) {
        Long eventId = getCurrentEventId();
        if (eventId == null) {
            eventId = getCurrentEventId(participant.getCreatetionTimestamp(), participant.getExpirationTimestamp(), participant.getPreferenceCategory());
        }
        log.trace("ID of event is " + eventId + " for DRUID " + participant.getMyEnergyId());
        participant.setEventId(eventId);
        return participant;
    }

    public Object setEventIdOnHeader(@Body Object body, @Headers Map<String, Object> headers) {
        headers.put("EVENT_ID", currentEventId);
        log.info("Using event ID " + currentEventId);
        String fileName = (String) headers.get("CamelFileName");
        if (fileName != null) {
            if (fileName.contains("fax")) {
                headers.put("NOTIFY_BY", "FAX");
            } else if (fileName.contains("text")) {
                headers.put("NOTIFY_BY", "SMS");
            } else if (fileName.contains("mail")) {
                headers.put("NOTIFY_BY", "EMAIL");
            } else if (fileName.contains("voice")) {
                headers.put("NOTIFY_BY", "PHONE");
            }
        } else {
            log.error("File name not found");
        }
        return body;
    }

    public Notification retrieveParticipantId(@Body Notification notification, @Headers Map<String, Object> headers) {
        log.trace("Processing Notification " + notification.getRecId() + " " + notification.getPrefCategory());
        notification.setEventId(currentEventId);
        Long participantId = getParticipantId(notification);
        log.trace("Participant ID is " + participantId + " even id " + currentEventId);
        if (participantId == null) {
            log.error("Cannot find participant");
        }
        notification.setParticipantId(participantId);
        findVendor(notification, headers);
        headers.put("EVENT_ID", currentEventId);
        headers.put("PARTICIPANT_ID", participantId);
        return notification;
    }

    public Postback createPostback(@Body Notification notification) {
        Postback postback = null;

        float percent = 0.5f;
        if (random.nextFloat() < percent) {
            postback = new Postback();
            postback.setDrccNotifRec(String.valueOf(notification.getRecId()));
            postback.setContactVendor("VENDOR");
            postback.setContactTimestamp(new Date());
            postback.setNotifyBy(notification.getNotifyBy());
            postback.setNotifyByValue(notification.getNotifyByValue());
            postback.setContactStatus("DELIVERED");
        }

        return postback;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> mergeAddress(@Body HashMap<String, Object> sqlResultMap, @Headers Map<String, Object> headers) {
        List addressList = (List) sqlResultMap.get(DrccRouteBuilder.ADDRESS_DETAILS);
        log.trace("Address list " + addressList);
        String secondAddress = "";
        if (!addressList.isEmpty()) {
            Map<String, Object> map = (Map) addressList.get(0);
            secondAddress = (String) map.get("EVENT_DISPLAY_PREMISE_ADDR");
            headers.put(SECOND_ADDRESS, secondAddress);
        } else {
            headers.put(SECOND_ADDRESS, null);
        }

        headers.put(ADDRESS_SIZE, addressList.size());
        String combinedAddress = (String) sqlResultMap.get("EVENT_DISPLAY_PREMISE_ADDR");
        if (addressList.size() == 1) {
            combinedAddress = String.format("%s and %s", combinedAddress, secondAddress);
        } else if (addressList.size() > 1) {
            combinedAddress = String.format("%s and your %d other locations", combinedAddress, addressList.size());
        }
        headers.put(COMBINED_ADDRESS, combinedAddress);
        return sqlResultMap;
    }

    public Notification updateCSV(@Body Notification notification, @Headers Map headers) {
        notification.setRecId((Long) headers.get("NOTIFICATION_ID"));
        Long dupCount = (Long) headers.get("DUP_COUNT");
        if ("PDP".equals(notification.getPrefCategory()) && dupCount > 1) {
            notification.setPdpReservationCapacity(null);
        }
        if ("SPANISH".equalsIgnoreCase(notification.getLanguage())) {
            if ("Monday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Lunes");
            } else if ("Tuesday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Martes");
            } else if ("Wednesday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Miercoles");
            } else if ("Thursday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Jueves");
            } else if ("Friday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Viernes");
            } else if ("Saturday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Sabado");
            } else if ("Sunday".equalsIgnoreCase(notification.getEventDisplayName())) {
                notification.setEventDisplayName("Domingo");
            }
        }
        notification.setNotifyByValue(String.format("\"%s\"", notification.getNotifyByValue()));
        notification.setEventDisplayPremiseAddress(String.format("\"%s\"", (String) headers.get(COMBINED_ADDRESS)));
        return notification;
    }

    public List<Long> createInList(@Body ArrayList<Map> list, @Headers Map<String, Object> headers) {
        List<Long> ids = new ArrayList<>();
        for (Map obj : list) {
            ids.add((Long) obj.get("PDP_SR_PARTICIPANTS_PARTICIPANT_ID"));
        }
        headers.put("PARTICIPANT_IDS", ids);

        return ids;
    }

    public Object loadVendorMapping(@Body Object body) {
        log.warn("Loading vendor mappings");
        vendorMappingMap.clear();

        for (VendorStatusMapping vendorStatusMapping : vendorStatusMappingDao.findAll()) {
            VendorMapping mapping = VendorMapping.builder()
                    .vendorId(vendorStatusMapping.getVendor().getVendorId())
                    .statusCode(vendorStatusMapping.getStatusCode())
                    .successfulNotification(vendorStatusMapping.getSuccessfulNotification().name())
                    .displayMessage(vendorStatusMapping.getDisplayMessage())
                    .build();

            vendorMappingMap.put(String.format("%d_%s", mapping.getVendorId(), mapping.getStatusCode()), mapping);
        }

        return body;
    }

    public Object compileDestinationURLs(@Body Object body, @Headers Map<String, Object> headers) {
        String key = String.format("%s_%s", headers.get("PREF_CATEGORY"), headers.get("NOTIFY_BY"));
        Long vendorId = vendorLookup.get(key);
        if (vendorId != null && vendorName.containsKey(vendorId)) {
            List<String> urls = new ArrayList<>();
            for (VendorConfig vendorConfig : vendorName.get(vendorId)) {
                log.info("key " + key + " vendorConfig " + vendorConfig.getKey());
                if (key.equals(vendorConfig.getKey())) {
                    urls.add(vendorConfig.getCamelEndpoint());
                }
            }
            headers.put("FTP_ENDPOINTS", urls);
            log.info("Endpoints " + urls);
        }
        headers.keySet().forEach(k -> log.info(k + ":" + headers.get(k)));
        return body;
    }

    public Object checkVendorMapping(@Body HashMap<String, String> body, @Headers Map<String, Object> headers) {
        String key = String.format("%d_%s", headers.get("PDP_SR_VENDOR_VENDOR_ID"), headers.get("CONTACT_STATUS"));
        VendorMapping mapping = vendorMappingMap.get(key);
        String successNotification;
        String displayMessage;
        if (mapping != null) {
            successNotification = mapping.getSuccessfulNotification();
            displayMessage = mapping.getDisplayMessage();
        } else {
            if ("SUCCESSFUL".equalsIgnoreCase((String) headers.get("CONTACT_STATUS"))) {
                successNotification = "DELIVERED";
            } else {
                successNotification = "UNSUCCESS";
            }
            displayMessage = String.format("%s", headers.get("CONTACT_STATUS"));
        }
        headers.put("SUCCESSFUL_NOTIFICATION", successNotification);
        headers.put("DISPLAY_MESSAGE", displayMessage);

        return body;
    }

    public Object checkCustomerCount(@Body Customer body, @Headers Map<String, Object> headers) {
        if (customerCount == 0) {
            timeStamp = LocalDateTime.now();
            startTime = timeStamp;
        }
        long cc;
        synchronized (lock) {
            customerCount++;
            cc = customerCount;
        }
        headers.put("CUSTOMER_COUNT", cc);

        long batch = 100000;
        if (cc % batch == 0) {
            LocalDateTime now = LocalDateTime.now();
            long p = ChronoUnit.SECONDS.between(timeStamp, now);
            long pt = ChronoUnit.SECONDS.between(startTime, now);
            long sec = ChronoUnit.SECONDS.between(timeStamp, now);
            double r = batch / (double) sec;

            log.info("Customer count " + NumberFormat.getIntegerInstance().format(cc) + " since last report "
                    + p + " total " + pt + " rate rows/sec " + (int) r);
            timeStamp = now;
        }
        return body;
    }

    private Long getParticipantId(Notification notification) {
        Long eventId = getCurrentEventId();
        if (eventId != null && notification != null && !notification.getDruid().isEmpty()) {
            PdpSrParticipant pdpSrParticipant = pdpSrParticipantDao.findFirstByPdpSrEventEventIdAndDruid(eventId, notification.getDruid());
            if (pdpSrParticipant != null) {
                Long participantId = pdpSrParticipant.getParticipantId();
                log.trace(String.format("Participant ID  for %d %s is %d", eventId, notification.getDruid(), participantId));
                return participantId;
            } else {
                log.error("No result set returned for event ID " + eventId + " DRUID " + notification.getDruid());
            }
        }
        return null;
    }

    private void findVendor(Notification notification, Map<String, Object> header) {
        if (vendorLookup.isEmpty()) {
            log.error("Vendor data should not be empty at this point");
            loadVendors();
        }
        Long eventId = getCurrentEventId();
        //we cache the lookup key is e.g. PDP_FAX
        String key = String.format("%s_%s", notification.getPrefCategory(), notification.getNotifyBy());
        Long vendorId = vendorLookup.get(key);
        if (vendorId != null && vendorName.containsKey(vendorId)) {
            VendorConfig vendorConfig = vendorName.get(vendorId).get(0);
            notification.setVendorId(vendorId);
            notification.setVendor(vendorConfig.getVendorName());
            header.put("VENDOR_ID", vendorId);
            header.put("VENDOR", vendorConfig.getVendorName());
            header.put("NOTIFY_BY", notification.getNotifyBy());
            header.put("SFTP_ADDRESS", vendorConfig.getAddress());
            header.put("SFTP_USERNAME", vendorConfig.getUser());
            header.put("SFTP_PASSWORD", vendorConfig.getPw());
            header.put("SFTP_DIRECTORY", vendorConfig.getDir());
        } else {
            log.error("No Vendor with key " + key + " found for notification " + notification);
            throw new RuntimeException("No Vendor found for key " + key);
        }
    }

    private void loadVendors() {
        log.info("Loading vendor data");

        for (PdpSrVendor vendor : pdpSrVendorDao.findAll()) {
            Long vendorId = vendor.getVendorId();
            String name = vendor.getVendor();
            String address = vendor.getVendorConfigs().get(0).getSftpAddress();
            String user = vendor.getVendorConfigs().get(0).getSftpUsername();
            String pw = vendor.getVendorConfigs().get(0).getSftpPassword();
            String dir = vendor.getVendorConfigs().get(0).getSftpDirectory();
            String program = vendor.getVendorConfigs().get(0).getProgram();
            String notBy = vendor.getVendorConfigs().get(0).getNotifyBy();

            log.info("Adding Vendor to map: " + vendorId + ", " + name + ", " + String.format("%s_%s", program, notBy));

            vendorLookup.put(String.format("%s_%s", program, notBy), vendorId);
            VendorConfig vc = VendorConfig.builder()
                    .address(address)
                    .vendorName(name)
                    .user(user)
                    .pw(pw)
                    .dir(dir)
                    .program(program)
                    .notifyBy(notBy)
                    .camelEndpoint(String.format("sftp://%s/?fileName=%s/${file:onlyname}&username=%s&password=%s&disconnect=true", address, dir, user, pw))
                    .build();

            List<VendorConfig> tmpList = vendorName.getOrDefault(vendorId, new ArrayList<>());
            tmpList.add(vc);
            vendorName.put(vendorId, tmpList);
        }

        log.info("Vendor size " + vendorName.size());
    }

    private Long getCurrentEventId() {
        if (currentEventId == null) {
            synchronized (eventIdLock) {
                PdpSrEvent pdpSrEvent = pdpSrEventDao.findFirstByEventUniqueIdAndEventProgramAndEventState(currentUniqueEventId, currentProgram, currentEventState);

                if (pdpSrEvent != null) {
                    currentEventId = pdpSrEvent.getEventId();
                    log.info("DBID for " + currentUniqueEventId + " is " + currentEventId);
                } else {
                    log.error("No result set returned");
                }
            }
        }
        return currentEventId;
    }

    private Long getCurrentEventId(Date creationTimestamp, Date expireDate, String preferenceCategory) {
        if (currentEventId == null) {
            synchronized (eventIdLock) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(creationTimestamp.toInstant(), ZoneId.systemDefault()).minusHours(2);
                LocalDateTime eventDate = LocalDateTime.ofInstant(creationTimestamp.toInstant(), ZoneId.systemDefault());
                LocalDateTime endDate = LocalDateTime.ofInstant(expireDate.toInstant(), ZoneId.systemDefault());
                log.info("Synchronized Lookup of event ID");
                PdpSrEvent pdpSrEvent = pdpSrEventDao.findFirstByEventProgramAndEventStartAfterAndEventStartLessThanEqualAndEventEnd(
                        preferenceCategory,
                        Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(eventDate.atZone(ZoneId.systemDefault()).toInstant()),
                        Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));

                if (pdpSrEvent != null) {
                    currentEventId = pdpSrEvent.getEventId();
                    log.trace("DBID for " + eventDate + " " + preferenceCategory + " is" + currentEventId);
                } else {
                    log.trace("No result set returned");
                }
            }
        }
        return currentEventId;
    }
}