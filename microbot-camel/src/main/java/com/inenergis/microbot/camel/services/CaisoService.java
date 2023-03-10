package com.inenergis.microbot.camel.services;

import com.caiso.soa.batchvalidationstatus_v1.BatchValidationStatus;
import com.caiso.soa.batchvalidationstatus_v1.DemandResponseRegistration;
import com.caiso.soa.batchvalidationstatus_v1.ErrorLog;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.drregistrationdata_v1.DemandResponseRegistrationFull;
import com.caiso.soa.drregistrationdata_v1.DistributedEnergyResourceContainer;
import com.caiso.soa.standardoutput_v1.StandardOutput;
import com.caiso.soa.submitdrregistrations_v1_wsdl.FaultReturnType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionException;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus.RegistrationStatus;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.soap.CaisoObjectConverter;
import com.inenergis.util.soap.CaisoRequestWrapper;
import com.inenergis.util.soap.RegistrationAction;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.cxf.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.CANCELED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.NON_REGISTRABLE_CANCELED;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_REPROCESS;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_REVIEW;
import static com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus.PENDING_TO_UNREGISTER;


@Getter
@Setter
@Component
public class CaisoService {

    @Value("${caiso.mocked}")
    private boolean caisoMocked;

    public static final String SUBMIT = "SUBMIT";
    public static final String MODIFY = "MODIFY";
    private static final Logger logger = LoggerFactory.getLogger(CaisoService.class);

    private Properties properties;
    private LocationService locationService;
    private CaisoRequestWrapper caisoRequestWrapper;

    @Autowired
    public CaisoService(@Qualifier("appProperties") Properties properties, LocationService locationService) throws IOException {
        this.properties = properties;
        this.locationService = locationService;
        caisoRequestWrapper = new CaisoRequestWrapper(properties);

    }

    public void unregisterLocation(Exchange exchange) throws JAXBException, IOException, DatatypeConfigurationException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType, com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType {
        LocationSubmissionStatus location = ((LocationSubmissionStatus) exchange.getIn().getBody());
        unregisterLocation(location);
    }

    public void registerLocation(Exchange exchange) throws JAXBException, IOException, DatatypeConfigurationException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType {
        LocationSubmissionStatus location = ((LocationSubmissionStatus) exchange.getIn().getBody());
        registerLocation(location);
    }

    public void isLocationRegistered(Exchange exchange) throws com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType {
        if(caisoMocked){
            exchange.setProperty("isLocationRegistered", false);
            return;
        }
        LocationSubmissionStatus location = (LocationSubmissionStatus) exchange.getIn().getBody();
        if (location != null) {
            try {
                boolean isLocationRegistered = false;
                DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveLocation(CaisoObjectConverter.generateLocationRequest(location));
                List<DistributedEnergyResourceContainer> distributedEnergyResourceContainer = drRegistrationData.getMessagePayload().getDistributedEnergyResourceContainer();
                if (!CollectionUtils.isEmpty(distributedEnergyResourceContainer)) {
                    for (DistributedEnergyResourceContainer energyResourceContainer : distributedEnergyResourceContainer) {
                        switch (energyResourceContainer.getStatus().getValue()) {
                            case "END DATED":
                            case "WITHDRAWN": //it's considered not registered, otherwise...
                                break;
                            default:
                                isLocationRegistered = true;
                                location.setPreviousIsoId(energyResourceContainer.getLocationID().toString());
                        }
                    }
                }
                exchange.setProperty("isLocationRegistered", isLocationRegistered);
            } catch (com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType e) {
                if (!e.getFaultInfo().getMessagePayload().getEventLog().getEvent().getDescription().startsWith("No data found")) {
                    throw e;
                }
            }
        }
    }

    public void retrieveLocationStatus(Exchange exchange) throws com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.FaultReturnType, com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType, JAXBException, IOException, DatatypeConfigurationException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType {
        LocationSubmissionStatus location = (LocationSubmissionStatus) exchange.getIn().getBody();

        if (caisoMocked) {
            location.setIsoStatus("Inactive");
            location.setIsoResourceId("DEMO-"+ ThreadLocalRandom.current().nextInt(10000,100000));
            return;
        }

        boolean isLocationPendingToUnregister = location.getStatus() != null && location.isPendingToUnregister();
        if (isLocationPendingToUnregister || location.getIsoResourceId() == null) {
            BatchValidationStatus batchValidationStatus = caisoRequestWrapper.retrieveBatchStatus(CaisoObjectConverter.generateBatchValidationRequest(location.getIsoBatchId()));
            String result = batchValidationStatus.getMessagePayload().getBatchStatus().getDescription();
            if (result.equals("SUCCESS")) {
                if (isLocationPendingToUnregister) {
                    DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveLocation(CaisoObjectConverter.generateLocationRequest(location));
                    location.setIsoStatus(drRegistrationData.getMessagePayload().getDistributedEnergyResourceContainer().get(0).getStatus().getValue());
                } else {
                    location.setIsoResourceId(batchValidationStatus.getMessagePayload().getDistributedEnergyResourceContainer().get(0).getMRID());
                    location.setIsoStatus("PENDING");
                }
            } else if (result.equals("ERROR")) {
                if (isLocationPendingToUnregister) {
                    StandardOutput standardOutput = caisoRequestWrapper.submitLocation(CaisoObjectConverter.generateLocationInXML(location, MODIFY, location.getIsoStartDate(), getEndDate(location)));
                    location.setIsoBatchId(standardOutput.getMessagePayload().getEventLog().getBatch().getMRID());
                } else {
                    LocationSubmissionException exception = new LocationSubmissionException();
                    exception.setAccumulatedExceptions(new ArrayList<>());
                    for (ErrorLog errorLog : batchValidationStatus.getMessagePayload().getDistributedEnergyResourceContainer().get(0).getErrorLog()) {
                        exception.getAccumulatedExceptions().add(errorLog.getErrMessage());
                    }
                    exception.setLocationSubmissionStatus(location);
                    exception.onModifyflushAccumulatedExceptions();
                    exception.setDateAdded(new Date());
                    location.getExceptions().add(exception);
                    location.setIsoStatus("ERROR");
                }
            } //else in process we'll recheck afterward
        } else {
            DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveLocation(CaisoObjectConverter.generateLocationRequest(location));
            location.setIsoStatus(drRegistrationData.getMessagePayload().getDistributedEnergyResourceContainer().get(0).getStatus().getValue());
        }
    }

    public void createNewLocationFromExistingOne(Exchange exchange) throws IOException, DatatypeConfigurationException, JAXBException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType {
        LocationSubmissionStatus originalLocation = (LocationSubmissionStatus) exchange.getIn().getBody();
        LocationSubmissionStatus newLocation = new LocationSubmissionStatus();
        newLocation.setMeterDataRecheck(originalLocation.getMeterDataRecheck());
        newLocation.setIsoLse(originalLocation.getIsoLse());
        newLocation.setIsoSublap (originalLocation.getIsoSublap ());
        newLocation.setIso(originalLocation.getIso());
        newLocation.setProgramServiceAgreementEnrollment(originalLocation.getProgramServiceAgreementEnrollment());
        registerLocation(newLocation);
        newLocation.setStatus(LocationSubmissionStatus.LocationStatus.PENDING_APRPOVAL.getText());
        exchange.getIn().setBody(newLocation);
    }

    @SuppressWarnings("unchecked")
    public void submitRegistrations(Exchange exchange) throws FaultReturnType, IOException, DatatypeConfigurationException, JAXBException {
        List<RegistrationAction> registrationActions = (List<RegistrationAction>) exchange.getIn().getHeader("registrationActions");
        if (registrationActions != null) {
            submitRegistrations(registrationActions);
            logger.info("Registrations sent to ISO");
        }
    }

    private void submitRegistrations(List<RegistrationAction> registrationActions) throws JAXBException, IOException, DatatypeConfigurationException, FaultReturnType {
        if(caisoMocked){
            String mrId = UUID.randomUUID().toString();
            for (RegistrationAction registrationAction : registrationActions) {
                registrationAction.getRegistration().setIsoBatchId(mrId);
            }
        } else {
            StandardOutput standardOutput = caisoRequestWrapper.submitRegistration(CaisoObjectConverter.generateRegistrationsInXML(registrationActions));
            String mrid = standardOutput.getMessagePayload().getEventLog().getBatch().getMRID();
            for (RegistrationAction registrationAction : registrationActions) {
                registrationAction.getRegistration().setIsoBatchId(mrid);
            }
        }
    }

    public void createNewRegistrationForResource(Exchange exchange) throws JAXBException, IOException, DatatypeConfigurationException, FaultReturnType {
        ZonedDateTime localDateTomorrow = ZonedDateTime.now().plusDays(1).toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID);
        IsoResource isoResource = (IsoResource) exchange.getIn().getBody();
        RegistrationSubmissionStatus activeRegistration = isoResource.getActiveRegistration();

        RegistrationSubmissionStatus newRegistration = new RegistrationSubmissionStatus();
        newRegistration.setActiveStartDate(Date.from(localDateTomorrow.toInstant()));
        newRegistration.setActiveEndDate(Date.from(localDateTomorrow.plusYears(ConstantsProviderModel.DEFAULT_EXPIRING_YEARS_CAISO_REGISTRATION).toInstant()));
        newRegistration.setIsoResource(isoResource);
        newRegistration.setRegistrationStatus(RegistrationStatus.PENDING);
        newRegistration.setLocations(new ArrayList<>());
        newRegistration.setIsoName(RegistrationService.buildCaisoRegistrationName(newRegistration));
        newRegistration.getLocations().addAll(isoResource.getNewLocations());
        newRegistration.setEnoughDaysMeter("WAITING");
        isoResource.getRegistrations().add(newRegistration);

        List<RegistrationAction> registrationActions = new ArrayList<>();

        if (activeRegistration != null) {
            newRegistration.setLocations(new ArrayList<>(activeRegistration.getLocations()));
            activeRegistration.setActiveEndDate(Date.from(localDateTomorrow.toInstant()));
            activeRegistration.setRegistrationStatus(RegistrationStatus.FINISHED_PENDING_ISO);
            registrationActions.add(RegistrationAction.builder().action(MODIFY).registration(activeRegistration).build());
        }
        registrationActions.add(RegistrationAction.builder().action(SUBMIT).registration(newRegistration).build());
        submitRegistrations(registrationActions);
    }

    public void getRegistrationStatus(Exchange exchange) throws com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.FaultReturnType, com.caiso.soa.retrievedrregistrations_v1_wsdl.FaultReturnType {
        RegistrationSubmissionStatus registration = (RegistrationSubmissionStatus) exchange.getIn().getBody();
        if (registration.getIsoBatchId() == null) {
            retrieveRegistrationStatus(exchange, registration);
        } else {
            retrieveRegistrationBatchStatus(exchange, registration);
        }
    }

    private void retrieveRegistrationStatus(Exchange exchange, RegistrationSubmissionStatus registration) throws com.caiso.soa.retrievedrregistrations_v1_wsdl.FaultReturnType {
        try {
            DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveRegistration(CaisoObjectConverter.generateRegistrationRequest(registration));
            DemandResponseRegistrationFull registrationFromISO = drRegistrationData.getMessagePayload().getDemandResponseRegistrationFull().get(0);
            String statusFromISO = registrationFromISO.getStatus().getValue();
            switch (registration.getRegistrationStatus()) {
                case FINISHED_PENDING_ISO:
                case FINISHED:
                    Date endDateFromIso = registrationFromISO.getDistributedActivity().getSubmittedActiveEndDateTime().toGregorianCalendar().getTime();
                    if (statusFromISO.equals("TERMINATED") || (statusFromISO.equals("CONFIRMED") && registration.getActiveEndDate().toInstant().equals(endDateFromIso.toInstant()))) {
                        registration.setRegistrationStatus(RegistrationStatus.FINISHED);
                        registration.setActiveEndDate(endDateFromIso);
                        registration.setInconsistencySolved(true);
                    } else if (statusFromISO.equals("CONFIRMED")) {
                        registration.setRegistrationStatus(RegistrationStatus.REGISTERED);
                        registration.setActiveEndDate(endDateFromIso);
                        registration.setInconsistencySolved(true);
                    }
                    break;
                case PENDING:
                    if (statusFromISO.equals("CONFIRMED")) {
                        registration.setRegistrationStatus(RegistrationStatus.WAITING_FOR_SQMD);
                        registration.setInconsistencySolved(true);
                        registration.setIsoRegistrationId(registrationFromISO.getMRID());
                        exchange.getIn().setHeader("newRegistration", true);
                    }
                    break;
                default:
                    logger.error("Registration {} has an unexpected status {} while being inconsistent", registration.getId(), registration.getRegistrationStatus());
            }
        } catch (com.caiso.soa.retrievedrregistrations_v1_wsdl.FaultReturnType e) {
            if (e.getFaultInfo().getMessagePayload().getEventLog().getEvent().getDescription().startsWith("No data found")) {
                exchange.getIn().setHeader("killRegistration", true);
            } else {
                throw e;
            }
        }
    }

    private void retrieveRegistrationBatchStatus(Exchange exchange, RegistrationSubmissionStatus registration) throws com.caiso.soa.retrievebatchvalidationstatus_v1_wsdl.FaultReturnType {
        BatchValidationStatus batchValidationStatus = caisoRequestWrapper.retrieveBatchStatus(CaisoObjectConverter.generateBatchValidationRequest(registration.getIsoBatchId()));
        List<DemandResponseRegistration> caisoRegistrations = batchValidationStatus.getMessagePayload().getDemandResponseRegistration();
        String result = batchValidationStatus.getMessagePayload().getBatchStatus().getDescription();
        if (!CollectionUtils.isEmpty(caisoRegistrations)) {
            DemandResponseRegistration demandResponseRegistration = getDemandResponseRegistration(batchValidationStatus, registration);
            if (result.equals("SUCCESS")) {
                if (registration.getRegistrationStatus().equals(RegistrationStatus.FINISHED_PENDING_ISO)) {
                    registration.setRegistrationStatus(RegistrationStatus.FINISHED);
                } else {
                    registration.setIsoRegistrationId(demandResponseRegistration.getMRID());
                    registration.setRegistrationStatus(RegistrationStatus.WAITING_FOR_SQMD);
                    exchange.getIn().setHeader("newRegistration", true);
                }
            } else if (result.equals("ERROR")) {
                String error;
                if (demandResponseRegistration == null) {
                    if (registration.getRegistrationStatus().equals(RegistrationStatus.FINISHED_PENDING_ISO)) {
                        registration.setRegistrationStatus(RegistrationStatus.REGISTERED);
                        Date restoredEndDate = Date.from(registration.getActiveStartDate().toInstant().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID)
                                .plusYears(ConstantsProviderModel.DEFAULT_EXPIRING_YEARS_CAISO_REGISTRATION).toInstant());
                        registration.setActiveEndDate(restoredEndDate);
                        return;
                    } else {
                        error = "The batch sent to the ISO contained errors not related to this registration";
                    }
                } else {
                    List<ErrorLog> errorLogs = demandResponseRegistration.getErrorLog();
                    StringJoiner errorJoiner = new StringJoiner(", ");
                    errorLogs.forEach(e -> errorJoiner.add(e.getErrMessage()));
                    error = errorJoiner.toString();
                }
                createRegistrationException(registration, error);
            }
        } else {
            logger.warn("Batch Id {} is returning empty results", registration.getIsoBatchId());
        }
    }

    private void createRegistrationException(RegistrationSubmissionStatus registration, String error) {
        RegistrationSubmissionException exception = new RegistrationSubmissionException();
        exception.setRegistrationSubmissionStatus(registration);
        exception.setDateAdded(new Date());
        exception.setType(error);
        if (registration.getExceptions() == null) {
            registration.setExceptions(new ArrayList<>());
        }
        registration.getExceptions().add(exception);
        registration.setRegistrationStatus(RegistrationStatus.PROCESSING_ERROR);
    }

    private DemandResponseRegistration getDemandResponseRegistration(BatchValidationStatus batchValidationStatus, RegistrationSubmissionStatus registration) {
        for (DemandResponseRegistration demandResponseRegistration : batchValidationStatus.getMessagePayload().getDemandResponseRegistration()) {
            if (demandResponseRegistration.getName().equals(registration.getIsoName())) {
                return demandResponseRegistration;
            }
        }
        return null;
    }

    private void registerLocation(LocationSubmissionStatus location) throws JAXBException, IOException, DatatypeConfigurationException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType {
        Date from = Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        Date to = Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusYears(ConstantsProviderModel.DEFAULT_EXPIRING_YEARS_CAISO_LOCATIONS).toInstant());
        if(caisoMocked){
            location.setIsoStartDate(from);
            location.setIsoSubmissionDate(new Date());
            location.setIsoBatchId("DEMO-NO-BATCH");
            return;
        }
        StandardOutput standardOutput = caisoRequestWrapper.submitLocation(CaisoObjectConverter.generateLocationInXML(location, SUBMIT, from, to));
        location.setIsoBatchId(standardOutput.getMessagePayload().getEventLog().getBatch().getMRID());
        location.setIsoSubmissionDate(new Date());
        location.setIsoStartDate(from);
    }

    private void unregisterLocation(LocationSubmissionStatus location) throws JAXBException, IOException, DatatypeConfigurationException, com.caiso.soa.submitdrlocations_v1_wsdl.FaultReturnType, com.caiso.soa.retrievedrlocations_v1_wsdl.FaultReturnType {
        String previousStatus = location.getStatus();
        DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveLocation(CaisoObjectConverter.generateLocationRequest(location));
        if (drRegistrationData.getMessagePayload().getDistributedEnergyResourceContainer().get(0).getStatus().getValue().equalsIgnoreCase("END DATED")) {
            if (previousStatus.equals(NON_REGISTRABLE.getText())) {
                location.setStatus(NON_REGISTRABLE_CANCELED.getText());
            } else {
                location.setStatus(CANCELED.getText());
            }
        } else {
            StandardOutput standardOutput;
            if (previousStatus.equals(PENDING_REVIEW.getText()) || location.isPendingToUnregister() || previousStatus.equals(PENDING_REPROCESS.getText())) {
                standardOutput = caisoRequestWrapper.submitLocation(CaisoObjectConverter.generateLocationInXML(location, SUBMIT, location.getIsoStartDate(), location.getIsoStartDate()));
            } else {
                //TODO check this with an Inactive location
                Date to = getEndDate(location);
                standardOutput = caisoRequestWrapper.submitLocation(CaisoObjectConverter.generateLocationInXML(location, MODIFY, location.getIsoStartDate(), to));
            }
            location.setIsoBatchId(standardOutput.getMessagePayload().getEventLog().getBatch().getMRID());
        }
        if (previousStatus.equals(NON_REGISTRABLE.getText())) {
            location.setStatus(NON_REGISTRABLE_CANCELED.getText());
        } else {
            location.setStatus(PENDING_TO_UNREGISTER.getText());
        }
        locationStatusChanged(location, previousStatus);
    }

    private void locationStatusChanged(LocationSubmissionStatus location, String previousStatus) {
        if (!previousStatus.equals(location.getStatus())) {
            locationService.saveLocationHistoryChange(location, previousStatus);
        }
    }

    private Date getEndDate(LocationSubmissionStatus location) {
        Date endDate = location.getProgramServiceAgreementEnrollment().getEffectiveEndDate();
        if (endDate == null) {
            endDate = Date.from(LocalDateTime.now().atZone(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
        }
        return Date.from(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant());
    }
}