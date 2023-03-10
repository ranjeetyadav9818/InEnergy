package com.inenergis.microbot.camel.services;

import com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.FaultReturnType;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationChangelog;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.microbot.camel.dao.LocationChangelogDao;
import com.inenergis.microbot.camel.dao.RegistrationDao;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.soap.RegistrationAction;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus.RegistrationStatus.FINISHED_PENDING_ISO;
import static com.inenergis.util.ConstantsProviderModel.DEFAULT_EXPIRING_YEARS_CAISO_REGISTRATION;

@Getter
@Setter
@Component
public class RegistrationService {

    public static final String CHANGELOG_EFFECTIVE_DATE = "changelogEffectiveDate";
    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
    private static final String DEBUG_ID = "#CHANGELOG PROCESSING#";

    private CaisoService caisoService;

    @Autowired
    LocationChangelogDao locationChangelogDao;

    @Autowired
    RegistrationDao registrationDao;

    @Transactional
    public void obtainResourcesWithChangelog(Exchange exchange) {

        // Obtain all resources involved
        List<LocationChangelog> nextChangeLogs = getNextChangeLogs();

        if (!CollectionUtils.isEmpty(nextChangeLogs)) {
            // They all have the same date (see getNextChangeLogs)
            exchange.getIn().setHeader(CHANGELOG_EFFECTIVE_DATE, nextChangeLogs.get(0).getEffectiveDate());
        }

        // Build a map ordering change logs by resource
        Map<IsoResource, List<LocationChangelog>> changesOrderedByIso = nextChangeLogs.stream()
                .collect(Collectors.groupingBy(LocationChangelog::getIsoResource));

        if (anyIsoHasInconsistencies(changesOrderedByIso.keySet())) {
            changesOrderedByIso = new HashMap<>();
        }

        parseRegistrationChanges(changesOrderedByIso);

        exchange.getIn().setBody(changesOrderedByIso);
    }

    @Transactional
    @Modifying
    public void deleteRegistration(Exchange exchange) {
        RegistrationSubmissionStatus registration = (RegistrationSubmissionStatus) exchange.getIn().getBody();
        registrationDao.deleteById(registration.getId());
    }

    @Transactional
    public void resolveInconsistencies(Exchange exchange) {
        List<RegistrationSubmissionStatus> registrations = registrationDao.findAllByIsoBatchIdIsNullAndInconsistencySolvedIsFalse();
        exchange.getIn().setBody(registrations);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Modifying
    public void sendChangesToISO(Exchange exchange) throws FaultReturnType, IOException, DatatypeConfigurationException, JAXBException {
        Map<IsoResource, List<LocationChangelog>> processedChangesOrderedByIso = (Map<IsoResource, List<LocationChangelog>>) exchange.getIn().getBody();
        List<RegistrationAction> registrationsToSubmit = new ArrayList<>();
        List<IsoResource> resources = new ArrayList<>(processedChangesOrderedByIso.keySet());
        if (!CollectionUtils.isEmpty(resources)) {
            Date changelogEffectiveDate = (Date) exchange.getIn().getHeader(CHANGELOG_EFFECTIVE_DATE);
            Date effectiveDate = calculateAvailableEffectiveDate(changelogEffectiveDate);
            for (IsoResource resource : resources) {
                RegistrationSubmissionStatus registration = resource.getLastRegistrationInISO();
                if (registration != null) {
                    registration.setIsoBatchId(null);
                    registration.setInconsistencySolved(false);
                    registration.setActiveEndDate(effectiveDate);
                    registration.setRegistrationStatus(FINISHED_PENDING_ISO);
                    registrationsToSubmit.add(createRegistrationAction(registration, CaisoService.MODIFY));
                }
                RegistrationSubmissionStatus newRegistration = createRegistrationSubmissionStatus(resource, effectiveDate, calculateRegistrationEndDate(effectiveDate));
                resource.getRegistrations().add(newRegistration);
                registrationsToSubmit.add(createRegistrationAction(newRegistration, CaisoService.SUBMIT));
            }
        }
        if (!CollectionUtils.isEmpty(registrationsToSubmit)) {
            for (RegistrationAction registrationAction : registrationsToSubmit) {
                registrationDao.save(registrationAction.getRegistration());
                registrationAction.setRegistration(registrationAction.getRegistration());
                log.info(DEBUG_ID + "Registration pre-saved " + registrationAction.getRegistration().getId());
            }
            exchange.getIn().setHeader("registrationActions", registrationsToSubmit);
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Modifying
    public void updateRegistrations(Exchange exchange) {
        List<RegistrationAction> registrationActions = (List<RegistrationAction>) exchange.getIn().getHeader("registrationActions");
        if (!CollectionUtils.isEmpty(registrationActions)) {
            for (RegistrationAction registrationAction : registrationActions) {
                RegistrationSubmissionStatus registrationRefreshed = registrationDao.getById(registrationAction.getRegistration().getId());
                if (registrationRefreshed == null) {
                    continue;
                }

                registrationDao.updateIsoBatchId(registrationAction.getRegistration().getId(), registrationAction.getRegistration().getIsoBatchId());

                log.info(DEBUG_ID + "Batch id Updated for registration " + registrationRefreshed.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    @Modifying
    public void markAllChangeLogsAsProcessed(Exchange exchange) {
        Map<IsoResource, List<LocationChangelog>> processedChangesOrderedByIso = (Map<IsoResource, List<LocationChangelog>>) exchange.getIn().getBody();
        if (processedChangesOrderedByIso != null && !processedChangesOrderedByIso.isEmpty()) {
            List<Long> allIds = new ArrayList<>();
            for (List<LocationChangelog> changelogs : processedChangesOrderedByIso.values()) {
                allIds.addAll(changelogs.stream().map(IdentifiableEntity::getId).collect(Collectors.toList()));
            }
            allIds = allIds.stream().distinct().collect(Collectors.toList());
            locationChangelogDao.setToProcessed(allIds);
        }
    }

    private boolean anyIsoHasInconsistencies(Set<IsoResource> isoResources) {
        for (IsoResource isoResource : isoResources) {
            for (RegistrationSubmissionStatus registrationSubmissionStatus : isoResource.getRegistrations()) {
                if (registrationSubmissionStatus.getIsoBatchId() == null && !registrationSubmissionStatus.isInconsistencySolved()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Date calculateAvailableEffectiveDate(Date changelogEffectiveDate) {
        LocalDate nextAvailable = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusDays(1);
        LocalDate changelogLocalDate = changelogEffectiveDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toLocalDate();
        if (changelogLocalDate.isAfter(nextAvailable)) {
            nextAvailable = changelogLocalDate;
        }
        return Date.from(nextAvailable.atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
    }


    private Date calculateRegistrationEndDate(Date effectiveDate) {
        return Date.from(effectiveDate.toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusYears(DEFAULT_EXPIRING_YEARS_CAISO_REGISTRATION).toInstant());
    }

    private RegistrationAction createRegistrationAction(RegistrationSubmissionStatus registration, String action) {
        RegistrationAction registrationAction = new RegistrationAction();
        registrationAction.setAction(action);
        registrationAction.setRegistration(registration);
        return registrationAction;
    }

    private RegistrationSubmissionStatus createRegistrationSubmissionStatus(IsoResource isoResource, Date startDate, Date endDate) {
        RegistrationSubmissionStatus registration = new RegistrationSubmissionStatus();
        registration.setActiveStartDate(startDate);
        registration.setActiveEndDate(endDate);
        registration.setIsoResource(isoResource);
        registration.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.PENDING);
        registration.setLocations(isoResource.getNewLocations());
        registration.setIsoName(buildCaisoRegistrationName(registration));
        registration.setEnoughDaysMeter("WAITING");
        return registration;
    }

    @Transactional
    private List<LocationChangelog> getNextChangeLogs() {
        LocalDate maxChangelogsLocalDate = LocalDate.now(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)
                .plusDays(ConstantsProviderModel.LOCATION_CHANGELOG_DAYS_IN_ADVANCE_ALLOWED);
        Date maxChangelogDate = Date.from(maxChangelogsLocalDate.atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());

        List<LocationChangelog> changelogList = locationChangelogDao.getNextChangeLogs(maxChangelogDate);

        //Lazy loading coupled with parseRegistrationChanges
        changelogList.forEach(c -> c.getIsoResource().getActiveRegistration());
        return changelogList;
    }

    private void parseRegistrationChanges(Map<IsoResource, List<LocationChangelog>> changesOrderedByIso) {
//      build one registration per resource based on the current status + changelogs and save it into transient newLocations
        for (Map.Entry<IsoResource, List<LocationChangelog>> entry : changesOrderedByIso.entrySet()) {
            final IsoResource isoResource = entry.getKey();
            final List<LocationChangelog> changelogList = entry.getValue();

            RegistrationSubmissionStatus activeRegistration = isoResource.getActiveRegistration();
            List<LocationSubmissionStatus> newLocations = activeRegistration == null ? new ArrayList<>() : new ArrayList<>(activeRegistration.getLocations());
            for (LocationChangelog changelog : changelogList) {
                LocationSubmissionStatus location = changelog.getLocation();
                switch (changelog.getType()) {
                    case ADDED:
                    case MOVED:
                        if (!newLocations.contains(location)) {
                            newLocations.add(location);
                        }
                        break;
                    case REMOVED:
                    case UNENROLLED:
                        newLocations.remove(location);
                        break;
                    default: //case NONE: case MOVING: //no
                        log.debug(DEBUG_ID + "Unexpected operation iso " + isoResource.getId() + " changelog " + changelog.getId() + " operation " + changelog.getType().name());
                        break;
                }
            }
            isoResource.setNewLocations(newLocations);
        }
    }

    public static String buildCaisoRegistrationName(RegistrationSubmissionStatus registration) {
        if (!CollectionUtils.isEmpty(registration.getIsoResource().getRegistrations())) {
            int i = registration.getIsoResource().getRegistrations().indexOf(registration);
            if (i == -1) {
                i = registration.getIsoResource().getRegistrations().size() + 1;
            }
            return registration.getIsoResource().getName() + " - " + i;
        }
        return registration.getIsoResource().getName() + " - " + 1;
    }
}
