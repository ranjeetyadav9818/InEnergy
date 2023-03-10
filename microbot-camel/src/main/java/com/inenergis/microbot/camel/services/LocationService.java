package com.inenergis.microbot.camel.services;

import com.inenergis.entity.History;
import com.inenergis.entity.ProductType;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.microbot.camel.dao.HistoryDao;
import com.inenergis.microbot.camel.dao.LocationSubmissionStatusDao;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.ASSIGNED_TO_RESOURCE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.CANCELED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.DISPUTED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.DUPLICATED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.EXCEPTIONS;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.INACTIVE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.INACTIVE_INFORMED_TO_ISO;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE_CANCELED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_APRPOVAL;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_REPROCESS;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_REVIEW;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_TO_UNREGISTER;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.WITHDRAWN;

@Service
public class LocationService {

    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private LocationSubmissionStatusDao locationSubmissionStatusDao;

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    public void markAsProcessing(Exchange exchange) {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) exchange.getIn().getBody();
        changeStatus(locationSubmissionStatus, PENDING_APRPOVAL.getText());
    }

    @Transactional
    @Modifying
    private void changeStatus(LocationSubmissionStatus location, String newStatus) {
        String previousStatus = location.getStatus();
        location.setStatus(newStatus);
        location = locationSubmissionStatusDao.save(location);
        saveLocationHistoryChange(location, previousStatus);
    }

    public void setLocationStatusToInactive(Exchange exchange) throws Exception {
        LocationSubmissionStatus location = (LocationSubmissionStatus) exchange.getIn().getBody();
        changeStatus(location, LocationSubmissionStatus.LocationStatus.INACTIVE.getText());
    }

    public void generateHistoryForLocationCreation(Exchange exchange) {
        LocationSubmissionStatus location = (LocationSubmissionStatus) exchange.getIn().getBody();
        saveLocationHistoryChange(location, null);
    }

    @Transactional
    @Modifying
    public void saveLocationHistoryChange(LocationSubmissionStatus location, String previousStatus) {
        History history = new History();
        history.setAuthor("System");
        history.setChangeType(History.HistoryChangeType.FIELD);
        history.setCreationDate(new Date());
        history.setEntity(location.getClass().getSimpleName());
        history.setEntityId(location.getId().toString());
        history.setField("status");
        history.setOldValue(previousStatus);
        history.setNewValue(location.getStatus());
        historyDao.save(history);
    }

    @Transactional
    public void getPendingStatuses(Exchange exchange) {
        List<String> statuses = Arrays.asList(PENDING_APRPOVAL.getText(), PENDING_REVIEW.getText(), PENDING_TO_UNREGISTER.getText(), NON_REGISTRABLE.getText(),
                PENDING_REPROCESS.getText(), INACTIVE_INFORMED_TO_ISO.getText(), INACTIVE.getText());
        List<LocationSubmissionStatus> locationSubmissions = locationSubmissionStatusDao.findByStatusIn(statuses);
        locationSubmissions.forEach(l -> l.getIso().getActiveProfile());
        locationSubmissions.forEach(l -> l.getProgramServiceAgreementEnrollment().getServiceAgreement().getAgreementPointMaps().isEmpty());
        exchange.getIn().setBody(locationSubmissions);
    }

    public void getLocationByEnrollment(Exchange exchange) {
        ProgramServiceAgreementEnrollment enrollment = null;
        if (exchange.getIn().getBody() != null && exchange.getIn().getBody() instanceof ProgramServiceAgreementEnrollment) {
            enrollment = (ProgramServiceAgreementEnrollment) exchange.getIn().getBody();
        } else {
            enrollment = ((ProgramServiceAgreementEnrollment) exchange.getProperty("enrollment"));
        }
        exchange.getIn().setBody(enrollment.getLastLocation());
    }

    @Transactional
    public void getLocation(Exchange exchange) {
        Long id;
        if (exchange.getIn().getBody() instanceof Long) {
            id = (Long) exchange.getIn().getBody();
        } else {
            id = Long.valueOf((String) exchange.getIn().getBody());
        }
        LocationSubmissionStatus location = locationSubmissionStatusDao.getById(id);
        location.getIso().getActiveProfile();
        location.getProgramServiceAgreementEnrollment().getServiceAgreement().getAgreementPointMaps().isEmpty();
        exchange.getIn().setBody(location);
    }

    @Transactional
    public void getLocationsWithPendingMeterData(Exchange exchange) {
        exchange.getIn().setBody(locationSubmissionStatusDao.findByMeterDataRecheck(true));
    }

    @Transactional
    public void getInactiveLocations(Exchange exchange) {
        List<LocationSubmissionStatus> locations = locationSubmissionStatusDao.findByStatusAndMeterDataRecheck(INACTIVE.getText(), false);

        locations.forEach(l -> l.getProgramServiceAgreementEnrollment().getCurrentFSL());
        locations.forEach(l -> l.getProgramServiceAgreementEnrollment().getServiceAgreement().getMeterForecasts().isEmpty());
        locations.forEach(l -> l.getProgram().getActiveProfile().getEventDurations().isEmpty());
        logger.info("Locations before filtering");
        for (LocationSubmissionStatus location : locations) {
            logger.info("{} {}", location.getId(), location.getProgram().getName());
        }
        Map<Pair<String, String>, List<LocationSubmissionStatus>> locationMap = locations.stream()
                .filter(l -> l.getProgram().getActiveProfile() != null)
                .filter(l -> l.getProgram().getActiveProfile().isWholesaleAutoresourceMaintenance())
                .filter(l -> l.getProgram().getActiveProfile().getWholesaleIsoProduct().getType().equals(ProductType.RDRR))
                .collect(Collectors.groupingBy(LocationSubmissionStatus::getLseAndSublapCombined));
        exchange.getIn().setHeader("inactiveLocations", locationMap);
        for (Map.Entry<Pair<String, String>, List<LocationSubmissionStatus>> entry : locationMap.entrySet()) {
            logger.info("{} -> {} locations", entry.getKey(), entry.getValue().size());
        }
    }

    public void isoStatusChanged(Exchange exchange) {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) exchange.getIn().getBody();
        String status = locationSubmissionStatus.getStatus();
        if ("DUPLICATE".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = DUPLICATED.getText();
        }
        if ("DISPUTED".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = DISPUTED.getText();
        }
        if ("PENDING".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = PENDING_REVIEW.getText();
        }
        if ("INACTIVE".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = INACTIVE.getText();
        }
        if ("ACTIVE".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = ASSIGNED_TO_RESOURCE.getText();
        }
        if ("END DATED".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            if (locationSubmissionStatus.getStatus().equals(NON_REGISTRABLE.getText())) {
                status = NON_REGISTRABLE_CANCELED.getText();
            } else {
                status = CANCELED.getText();
            }
        }
        if ("WITHDRAWN".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = WITHDRAWN.getText();
        }
        if ("ERROR".equalsIgnoreCase(locationSubmissionStatus.getIsoStatus())) {
            status = EXCEPTIONS.getText();
        }
        if (!status.equals(locationSubmissionStatus.getStatus())) {
            changeStatus(locationSubmissionStatus, status);
        }
    }

    public void loadLazyFieldsLocations(List<LocationSubmissionStatus> locations) {
        if (!locations.isEmpty()) {
            for (LocationSubmissionStatus location : locations) {
                ProgramServiceAgreementEnrollment programServiceAgreementEnrollment = location.getProgramServiceAgreementEnrollment();
                ServiceAgreement serviceAgreement = programServiceAgreementEnrollment.getServiceAgreement();
                List<MeterForecast> meterForecasts = serviceAgreement.getMeterForecasts();
                if (meterForecasts != null) {
                    for (MeterForecast meterForecast : meterForecasts) {
                        meterForecast.getId();
                    }
                }
                List<ProgramFirmServiceLevel> fsls = programServiceAgreementEnrollment.getFsls();
                if (fsls != null) {
                    for (ProgramFirmServiceLevel fsl : fsls) {
                        fsl.getId();
                    }
                }
            }
        }
    }

    public void cleanExceptions(Exchange exchange) {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) exchange.getIn().getBody();
        for (LocationSubmissionException exception : locationSubmissionStatus.getExceptions()) {
            exception.setResolved(true);
        }
    }

    public void markEnrollmentAsReinstated(Exchange exchange) {
        LocationSubmissionStatus location = (LocationSubmissionStatus) exchange.getIn().getBody();
        location.getProgramServiceAgreementEnrollment().setEnrollmentStatus(EnrolmentStatus.IN_PROGRESS.getName());
    }

    @Transactional
    public void getActiveLocationsWithAllRegistrationsFinished(Exchange exchange) {
        List<LocationSubmissionStatus> locations = locationSubmissionStatusDao.findByStatusIn(Collections.singletonList(LocationSubmissionStatus.LocationStatus.ASSIGNED_TO_RESOURCE.getText()));
        locations = locations.stream()
                .filter(l -> !atLeastOneRegistrationIsNotEnded(l.getRegistrations()))
                .collect(Collectors.toList());
        exchange.getIn().setBody(locations);
    }

    private boolean atLeastOneRegistrationIsNotEnded(List<RegistrationSubmissionStatus> registrations) {
        for (RegistrationSubmissionStatus registration : registrations) {
            switch (registration.getRegistrationStatus()) {
                case FINISHED:
                case PROCESSING_ERROR:
                    break;
                default:
                    return true;
            }
        }
        return false;
    }
}