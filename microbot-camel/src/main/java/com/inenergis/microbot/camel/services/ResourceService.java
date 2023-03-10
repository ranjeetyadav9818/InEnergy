package com.inenergis.microbot.camel.services;

import com.inenergis.entity.ProductType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus.RegistrationStatus;
import com.inenergis.microbot.camel.dao.IsoResourceDao;
import com.inenergis.microbot.camel.dao.RegistrationSubmissionStatusDao;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private RegistrationSubmissionStatusDao registrationSubmissionStatusDao;

    @Autowired
    private IsoResourceDao isoResourceDao;

    @Transactional
    public void getAvailableResources(Exchange exchange) {
        Map<Pair<String, String>, List<LocationSubmissionStatus>> locationsMap = (Map<Pair<String, String>, List<LocationSubmissionStatus>>) exchange.getIn().getHeader("inactiveLocations");

        Map<Pair<String, String>, List<IsoResource>> resourcesMap = new HashMap<>();
        for (Pair<String, String> pair : locationsMap.keySet()) {
            List<IsoResource> isoResources = isoResourceDao.findByIsoLseAndIsoSublapAndType(pair.getLeft(), pair.getRight(), ProductType.RDRR);
            isoResources = isoResources.stream().filter(r -> CollectionUtils.isEmpty(r.getRegistrations()) || r.getActiveRegistration() != null
                    || allRegistrationsAreExceptions(r.getRegistrations())).collect(Collectors.toList());
            loadLazyFields(isoResources);
            resourcesMap.put(pair, isoResources);
        }
        exchange.getIn().setHeader("availableResources", resourcesMap);
        logger.info("{} resources available found for these sublaps and product types {}", resourcesMap.values().size(), resourcesMap.keySet().toArray());
    }

    private boolean allRegistrationsAreExceptions(List<RegistrationSubmissionStatus> registrations) {
        for (RegistrationSubmissionStatus registration : registrations) {
            if (!registration.getRegistrationStatus().equals(RegistrationStatus.PROCESSING_ERROR)) {
                return false;
            }
        }
        return true;
    }

    public void assignLocationsToResources(Exchange exchange) {
        Map<Pair<String, String>, List<LocationSubmissionStatus>> locationsMap = (Map<Pair<String, String>, List<LocationSubmissionStatus>>) exchange.getIn().getHeader("inactiveLocations");
        Map<Pair<String, String>, List<IsoResource>> resourcesMap = (Map<Pair<String, String>, List<IsoResource>>) exchange.getIn().getHeader("availableResources");
        List<IsoResource> modifiedResources = new ArrayList<>();
        for (Map.Entry<Pair<String, String>, List<LocationSubmissionStatus>> locationEntry : locationsMap.entrySet()) {
            List<IsoResource> resources = resourcesMap.get(locationEntry.getKey()).stream().sorted(capacityComparator).collect(Collectors.toList());
            for (LocationSubmissionStatus location : locationEntry.getValue()) {
                String previousStatus = location.getStatus();
                assignLocationToResources(location, resources);
                createHistoryForStatusChange(location, previousStatus);
            }
            for (IsoResource resource : resources) {
                if (!resource.getNewLocations().isEmpty()) {
                    modifiedResources.add(resource);
                }
            }
        }
        exchange.getIn().setBody(modifiedResources);
    }

    private void createHistoryForStatusChange(LocationSubmissionStatus location, String previousStatus) {
        if (!previousStatus.equals(location.getStatus())) {
            locationService.saveLocationHistoryChange(location, previousStatus);
        }
    }

    @Transactional
    public void getPendingRegistrations(Exchange exchange) {
        List<RegistrationSubmissionStatus> registrations = registrationSubmissionStatusDao.findByRegistrationStatusIn(Arrays.asList(RegistrationStatus.PENDING, RegistrationStatus.FINISHED_PENDING_ISO));
        registrations.forEach(reg -> reg.getExceptions().isEmpty());
        exchange.getIn().setBody(registrations);
    }

    @Transactional
    public void getRegistrationsWithAutobidEnabled(Exchange exchange) {
        List<RegistrationSubmissionStatus> byCriterion = registrationSubmissionStatusDao.findByRegistrationStatusIn(Collections.singletonList(RegistrationStatus.REGISTERED));
        List<RegistrationSubmissionStatus> result = byCriterion.stream()
                .filter(reg -> reg.getIsoResource().getIsoProduct().isAutoBidLowRisk())
                .collect(Collectors.toList());
        for (RegistrationSubmissionStatus registration : result) { // this is for loading lazy-initialized entities
            registration.getIsoResource().getActiveRegistration();
            registration.getIsoResource().getActivePmaxPmin();
            registration.getIsoResource().getIsoProduct().getProfile().getHolidays().isEmpty();
            registration.getIsoResource().getIsoProduct().getProfile().getRiskConditions().isEmpty();
            registration.getIsoResource().getImpactedResources().forEach(ir -> ir.getEvent().getImpactedCustomers().isEmpty());
            registration.getLocations().forEach(l -> l.getProgram().getActiveProfile().getSafetyReductionFactors().isEmpty());
            registration.getLocations().forEach(l -> l.getProgram().getActiveProfile().getDispatchReasons().isEmpty());
            registration.getLocations().forEach(l -> l.getProgram().getActiveProfile().getEventDurations().isEmpty());
            registration.getLocations().forEach(l -> l.getProgramServiceAgreementEnrollment().getServiceAgreement().getMeterForecasts().isEmpty());
        }
        exchange.getIn().setBody(result);
    }

    private void assignLocationToResources(LocationSubmissionStatus location, List<IsoResource> resources) {
        if (resources != null && !resources.isEmpty()) {
            IsoResource emptyResource = null;
            for (IsoResource resource : resources) {
                if (resource.getActiveRegistration() != null && !resource.getActiveRegistration().getLocations().isEmpty()) {
                    if (resource.isLocationAssignable(location)) {
                        location.setStatus(LocationSubmissionStatus.LocationStatus.INACTIVE_INFORMED_TO_ISO.getText());
                        logger.info("!!! {} assigned to resource", location);
                        resource.getNewLocations().add(location);
                        return;
                    }
                } else {
                    emptyResource = resource;
                }
            }
            if (emptyResource != null) {
                emptyResource.getNewLocations().add(location);
                location.setStatus(LocationSubmissionStatus.LocationStatus.INACTIVE_INFORMED_TO_ISO.getText());
                logger.info("!!! {} assigned to empty resource {}", location.getId(), emptyResource.getName());
            }
        }
    }

    private void loadLazyFields(List<IsoResource> isoResources) {
        if (isoResources != null) {
            for (IsoResource isoResource : isoResources) {
                if (isoResource.getActiveRegistration() != null) {
                    List<LocationSubmissionStatus> locations = isoResource.getActiveRegistration().getLocations();
                    locationService.loadLazyFieldsLocations(locations);
                }
            }
        }
    }

    private Comparator<IsoResource> capacityComparator = (o1, o2) -> {
        Long comparison = o1.getCalculatedCapacity() - o2.getCalculatedCapacity();
        return comparison.intValue();
    };
}