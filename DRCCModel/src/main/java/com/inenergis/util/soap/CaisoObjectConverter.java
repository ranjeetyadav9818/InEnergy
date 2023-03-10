package com.inenergis.util.soap;


import com.caiso.ads.api.model.DetailType;
import com.caiso.ads.api.model.DispatchBatchType;
import com.caiso.ads.api.model.InstructionDetailType;
import com.caiso.ads.api.model.InstructionType;
import com.caiso.ads.api.model.TrajectoryBatchType;
import com.caiso.ads.api.model.TrajectoryDopType;
import com.caiso.soa.batchvalidationstatus_v1.BatchStatus;
import com.caiso.soa.batchvalidationstatus_v1.BatchValidationStatus;
import com.caiso.soa.drregistrationdata_v1.AggregatedPnode;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.drregistrationdata_v1.DemandResponseProvider;
import com.caiso.soa.drregistrationdata_v1.DemandResponseRegistrationFull;
import com.caiso.soa.drregistrationdata_v1.DemandUtilityDistributionCompany;
import com.caiso.soa.drregistrationdata_v1.DistributedActivity;
import com.caiso.soa.drregistrationdata_v1.DistributedEnergyResourceContainer;
import com.caiso.soa.drregistrationdata_v1.DistributedEnergyResourceContainerLocation;
import com.caiso.soa.drregistrationdata_v1.LoadAggregationPoint;
import com.caiso.soa.drregistrationdata_v1.LoadAggregationPointDRRegistration;
import com.caiso.soa.drregistrationdata_v1.LoadServingEntity;
import com.caiso.soa.drregistrationdata_v1.Location;
import com.caiso.soa.drregistrationdata_v1.MessagePayload;
import com.caiso.soa.drregistrationdata_v1.ObjectFactory;
import com.caiso.soa.drregistrationdata_v1.RegisteredGenerator;
import com.caiso.soa.drregistrationdata_v1.RegisteredGeneratorDRRegistration;
import com.caiso.soa.drregistrationdata_v1.SchedulingCoordinator;
import com.caiso.soa.drregistrationdata_v1.StreetAddress;
import com.caiso.soa.drregistrationdata_v1.StreetDetail;
import com.caiso.soa.drregistrationdata_v1.TownDetail;
import com.caiso.soa.requestdrregistrationdata_v1.LocationRequest;
import com.caiso.soa.requestdrregistrationdata_v1.RegistrationRequest;
import com.caiso.soa.requestdrregistrationdata_v1.RequestDRRegistrationData;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Premise;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.award.Award;
import com.inenergis.entity.award.Instruction;
import com.inenergis.entity.award.InstructionDetail;
import com.inenergis.entity.award.Trajectory;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.util.EnergyUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public final class CaisoObjectConverter {

    private CaisoObjectConverter() {
    }

    public static final String ISO_SUBLAP_PREFIX = "SLAP_";

    public static RequestDRRegistrationData generateLocationRequest(LocationSubmissionStatus location) {
        RequestDRRegistrationData result = new RequestDRRegistrationData();
        IsoProfile activeProfile = location.getIso().getActiveProfile();
        com.caiso.soa.requestdrregistrationdata_v1.MessagePayload value = new com.caiso.soa.requestdrregistrationdata_v1.MessagePayload();
        LocationRequest locationRequest = new LocationRequest();
        com.caiso.soa.requestdrregistrationdata_v1.DistributedEnergyResourceContainer resourceContainer = new com.caiso.soa.requestdrregistrationdata_v1.DistributedEnergyResourceContainer();

        if (location.getIsoResourceId() != null) {
            resourceContainer.setMRID(location.getIsoResourceId());
        } else {
            com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany demandUtilityDistributionCompany = new com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany();
            demandUtilityDistributionCompany.setMRID(activeProfile.getUdcId());
            resourceContainer.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
            ServiceAgreement serviceAgreement = (ServiceAgreement)location.getProgramServiceAgreementEnrollment().getServiceAgreement();
            resourceContainer.setName(getLocationName(serviceAgreement.getSaUuid(), serviceAgreement.getAgreementPointMaps().get(0).getServicePoint().getPremise()));
        }

        com.caiso.soa.requestdrregistrationdata_v1.RegisteredGenerator registeredGenerator = new com.caiso.soa.requestdrregistrationdata_v1.RegisteredGenerator();
        com.caiso.soa.requestdrregistrationdata_v1.LoadServingEntity loadServingEntity = new com.caiso.soa.requestdrregistrationdata_v1.LoadServingEntity();
        loadServingEntity.setMRID(location.getIsoLse());
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        resourceContainer.setRegisteredGenerator(registeredGenerator);

        locationRequest.setDistributedEnergyResourceContainer(resourceContainer);
        value.setLocationRequest(locationRequest);
        result.setMessagePayload(value);
        return result;
    }

    public static RequestDRRegistrationData generateRegistrationRequest(RegistrationSubmissionStatus registration) {
        RequestDRRegistrationData result = new RequestDRRegistrationData();
        RegistrationRequest request = new RegistrationRequest();
        request.setRequestType("REGISTRATION_ONLY");
        com.caiso.soa.requestdrregistrationdata_v1.DemandResponseRegistrationFull demandResponseRegistration = new com.caiso.soa.requestdrregistrationdata_v1.DemandResponseRegistrationFull();
        if (registration.getIsoRegistrationId() != null) {
            demandResponseRegistration.setMRID(registration.getIsoRegistrationId());
        }
        demandResponseRegistration.setName(registration.getIsoName());
        com.caiso.soa.requestdrregistrationdata_v1.DemandResponseProvider demandResponseProvider = new com.caiso.soa.requestdrregistrationdata_v1.DemandResponseProvider();
        demandResponseProvider.setMRID(registration.getIsoResource().getIsoProduct().getProfile().getDrpId());
        demandResponseRegistration.setDemandResponseProvider(demandResponseProvider);
        com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany demandUtilityDistributionCompany = new com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID(registration.getIsoResource().getIsoProduct().getProfile().getUdcId());
        demandResponseRegistration.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
        request.setDemandResponseRegistration(demandResponseRegistration);
        com.caiso.soa.requestdrregistrationdata_v1.MessagePayload messagePayload = new com.caiso.soa.requestdrregistrationdata_v1.MessagePayload();
        messagePayload.setRegistrationRequest(request);
        result.setMessagePayload(messagePayload);
        return result;
    }

    public static String generateLocationInXML(LocationSubmissionStatus location, String action, Date from, Date to) throws JAXBException, IOException, DatatypeConfigurationException {

        DRRegistrationData submitRequest = new DRRegistrationData();
        MessagePayload payload = new MessagePayload();
        DistributedEnergyResourceContainer resourceContainer = new DistributedEnergyResourceContainer();

        Premise premise = populateResourceContanierIdName(location, resourceContainer);

        populateDemandResponseProvider(location, resourceContainer);

        populateDistributedActivity(resourceContainer, action, from, to);

        populateLocation(resourceContainer, premise);

        populateRegisteredGenerator(location, resourceContainer);

        populateDemandUtilityDistributionCompany(location, resourceContainer);

        populateDemandResponseRegistration(resourceContainer);

        if (location.getIsoResourceId() != null) {
            resourceContainer.setLocationID(new BigInteger(location.getIsoResourceId()));
        }

        payload.getDistributedEnergyResourceContainer().add(resourceContainer);
        submitRequest.setMessagePayload(payload);
        StringWriter sw = marshall(submitRequest);

        return SoapRequestHelper.zipAndBase64AString(sw.toString());
    }

    public static String generateRegistrationsInXML(List<RegistrationAction> registrationActions) throws JAXBException, IOException, DatatypeConfigurationException {

        DRRegistrationData submitRequest = convertToRegistrationData(registrationActions);
        StringWriter sw = marshall(submitRequest);
        return SoapRequestHelper.zipAndBase64AString(sw.toString());
    }

    public static DRRegistrationData convertToRegistrationData(List<RegistrationAction> registrationActions) throws DatatypeConfigurationException {
        DRRegistrationData submitRequest = new DRRegistrationData();
        MessagePayload payload = new MessagePayload();

        for (RegistrationAction registrationAction : registrationActions) {

            String action = registrationAction.getAction();

            RegistrationSubmissionStatus registration = registrationAction.getRegistration();

            DemandResponseRegistrationFull demandResponseRegistrationFull = new DemandResponseRegistrationFull();

            populateRootDRPAndUDP(registration, demandResponseRegistrationFull, action);

            populateDistributedActivity(registration, action, demandResponseRegistrationFull);

            populateRegisteredGenerator(registration, demandResponseRegistrationFull);

            populateLocations(registration, demandResponseRegistrationFull);

            demandResponseRegistrationFull.setName(registration.getIsoName());

            payload.getDemandResponseRegistrationFull().add(demandResponseRegistrationFull);
        }

        submitRequest.setMessagePayload(payload);
        return submitRequest;
    }

    public static BatchValidationStatus generateBatchValidationRequest(String isoBatchId) {
        BatchValidationStatus batchValidationStatus = new BatchValidationStatus();
        com.caiso.soa.batchvalidationstatus_v1.MessagePayload payload = new com.caiso.soa.batchvalidationstatus_v1.MessagePayload();
        BatchStatus batchStatus = new BatchStatus();
        batchStatus.setMRID(isoBatchId);
        payload.setBatchStatus(batchStatus);
        batchValidationStatus.setMessagePayload(payload);
        return batchValidationStatus;
    }

    private static void populateLocations(RegistrationSubmissionStatus registration, DemandResponseRegistrationFull demandResponseRegistrationFull) {
        for (LocationSubmissionStatus locationSubmissionStatus : registration.getLocations()) {
            DistributedEnergyResourceContainerLocation distributedEnergyResourceContainerLocation = new DistributedEnergyResourceContainerLocation();
            distributedEnergyResourceContainerLocation.setLocationID(new BigInteger(locationSubmissionStatus.getIsoResourceId()));
            demandResponseRegistrationFull.getDistributedEnergyResourceContainer().add(distributedEnergyResourceContainerLocation);
        }
    }

    private static void populateRegisteredGenerator(RegistrationSubmissionStatus registration, DemandResponseRegistrationFull demandResponseRegistrationFull) {
        RegisteredGeneratorDRRegistration registeredGenerator = new RegisteredGeneratorDRRegistration();
//        registeredGenerator.setMRID(registration.getIsoResource().getName());
        LoadAggregationPointDRRegistration loadAggregationPoint = new LoadAggregationPointDRRegistration();
        //loadAggregationPoint.setMRID("DLAP_PGAE_PCG2"); TODO
        AggregatedPnode aggregatedPnode = new AggregatedPnode();
        aggregatedPnode.setMRID(ISO_SUBLAP_PREFIX + registration.getIsoResource().getIsoSublap ());
        loadAggregationPoint.setAggregatedPnode(aggregatedPnode);
        registeredGenerator.setLoadAggregationPoint(loadAggregationPoint);
        LoadServingEntity loadServingEntity = new LoadServingEntity();
        loadServingEntity.setMRID(registration.getIsoResource().getIsoLse());
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        SchedulingCoordinator schedulingCoordinator = new SchedulingCoordinator();
        schedulingCoordinator.setMRID(registration.getIsoResource().getIsoProduct().getProfile().getScId());
        registeredGenerator.setSchedulingCoordinator(schedulingCoordinator);
        demandResponseRegistrationFull.setRegisteredGenerator(registeredGenerator);
    }

    private static void populateRootDRPAndUDP(RegistrationSubmissionStatus registration, DemandResponseRegistrationFull demandResponseRegistrationFull, String action) {
        if (registration.getIsoRegistrationId() != null) {
            demandResponseRegistrationFull.setMRID(registration.getIsoRegistrationId());
        }
        demandResponseRegistrationFull.setName(registration.getIsoName());
        demandResponseRegistrationFull.setTerminateRegistrationFlag(false);
        demandResponseRegistrationFull.setBaselineMethod("10 in 10 with SMA");
        demandResponseRegistrationFull.setProgram("Reliability DR");
        demandResponseRegistrationFull.setResourceType("Pre Defined");
        DemandResponseProvider demandResponseProvider = new DemandResponseProvider();
        demandResponseProvider.setMRID(registration.getIsoResource().getIsoProduct().getProfile().getDrpId());
        demandResponseRegistrationFull.setDemandResponseProvider(demandResponseProvider);
        DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID(registration.getIsoResource().getIsoProduct().getProfile().getUdcId());
        demandResponseRegistrationFull.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
    }

    private static void populateDistributedActivity(RegistrationSubmissionStatus registration, String action, DemandResponseRegistrationFull demandResponseRegistrationFull) throws DatatypeConfigurationException {
        DistributedActivity distributedActivity = new DistributedActivity();
        distributedActivity.setAction(action);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(registration.getActiveStartDate());
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveStartDateTime(xmlDate);
        c = new GregorianCalendar();
        c.setTime(registration.getActiveEndDate());
        xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveEndDateTime(xmlDate);
        demandResponseRegistrationFull.setDistributedActivity(distributedActivity);
    }

    private static void populateDemandResponseRegistration(DistributedEnergyResourceContainer resourceContainer) {
//        DemandResponseRegistration demandResponseRegistration = new DemandResponseRegistration();
//        demandResponseRegistration.setMRID("23409");
//        resourceContainer.setDemandResponseRegistration(demandResponseRegistration);
    }

    private static void populateDemandUtilityDistributionCompany(LocationSubmissionStatus location, DistributedEnergyResourceContainer resourceContainer) {
        if (location.getIso().getActiveProfile() != null) {
            DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
            demandUtilityDistributionCompany.setMRID(location.getIso().getActiveProfile().getUdcId());
            resourceContainer.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
        }
    }

    private static void populateRegisteredGenerator(LocationSubmissionStatus location, DistributedEnergyResourceContainer resourceContainer) {
        RegisteredGenerator registeredGenerator = new RegisteredGenerator();
        AggregatedPnode aggregatedPnode = new AggregatedPnode();
        aggregatedPnode.setMRID(ISO_SUBLAP_PREFIX + location.getIsoSublap ());
        LoadAggregationPoint value = new LoadAggregationPoint();
        value.setAggregatedPnode(aggregatedPnode);
        registeredGenerator.setLoadAggregationPoint(value);
        LoadServingEntity loadServingEntity = new LoadServingEntity();
        loadServingEntity.setMRID(location.getIsoLse());
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        resourceContainer.setRegisteredGenerator(registeredGenerator);
    }

    private static void populateLocation(DistributedEnergyResourceContainer resourceContainer, Premise premise) {
        if (premise != null) {
            Location locationCaiso = new Location();
            StreetAddress mainAddress = new StreetAddress();
            StreetDetail streetDetail = new StreetDetail();
            streetDetail.setAddressGeneral(premise.getServiceAddress1());
            streetDetail.setAddressGeneral2(premise.getServiceAddress2());
            mainAddress.setStreetDetail(streetDetail);
            TownDetail townDetail = new TownDetail();
            String code = StringUtils.rightPad(premise.getServicePostal(), 9, '0');
            townDetail.setCode(code.substring(0, 5) + "-" + code.substring(5));
            townDetail.setName(premise.getServiceCityUpr());
            townDetail.setStateOrProvince(premise.getServiceState());
            mainAddress.setTownDetail(townDetail);

            locationCaiso.setMainAddress(mainAddress);
            resourceContainer.setLocation(locationCaiso);
        }
    }

    private static void populateDistributedActivity(DistributedEnergyResourceContainer resourceContainer, String action, Date from, Date to) throws DatatypeConfigurationException {
        DistributedActivity distributedActivity = new DistributedActivity();
        distributedActivity.setAction(action);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(from);
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveStartDateTime(xmlDate);
        c = new GregorianCalendar();
        c.setTime(to);
        xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveEndDateTime(xmlDate);
        resourceContainer.setDistributedActivity(distributedActivity);
    }

    private static void populateDemandResponseProvider(LocationSubmissionStatus location, DistributedEnergyResourceContainer resourceContainer) {
        if (location.getIso().getActiveProfile().getIso().getActiveProfile() != null) {
            DemandResponseProvider demandResponseProvider = new DemandResponseProvider();
            demandResponseProvider.setMRID(location.getIso().getActiveProfile().getDrpId()); //LocationSubmissionStatus.iso.getActiveProfile.drpId (should be DPGE)
            resourceContainer.setDemandResponseProvider(demandResponseProvider);
        }
    }

    private static Premise populateResourceContanierIdName(LocationSubmissionStatus location, DistributedEnergyResourceContainer resourceContainer) {
        ServiceAgreement serviceAgreement = (ServiceAgreement)location.getProgramServiceAgreementEnrollment().getServiceAgreement();
        String mRID = location.getIso().getActiveProfile().getUdcId() + serviceAgreement.getSaUuid();
        resourceContainer.setMRID(mRID);
        List<AgreementPointMap> agreementPointMaps = serviceAgreement.getAgreementPointMaps();
        if (agreementPointMaps != null) {
            Premise premise = agreementPointMaps.get(0).getServicePoint().getPremise();
            resourceContainer.setName(getLocationName(serviceAgreement.getSaUuid(), premise));
            return premise;
        }
        return null;
    }

    private static String getLocationName(String saUuid, Premise premise) {
        StringBuilder sbName = new StringBuilder(saUuid);
        final String serviceAddress1 = premise.getServiceAddress1() == null ? StringUtils.EMPTY : premise.getServiceAddress1();
        sbName.append(serviceAddress1);
        return sbName.toString();
    }

    public static StringWriter marshall(DRRegistrationData submitRequest) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DRRegistrationData.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        ObjectFactory factory = new ObjectFactory();
        jaxbMarshaller.marshal(factory.createDRRegistrationData(submitRequest), sw);
        return sw;
    }

    public static Instruction convertToInstruction(InstructionType caisoInstruction, Award award, IsoResource resource, String xmlSource, DispatchBatchType batchType) throws DatatypeConfigurationException {
        Instruction internalInstruction = new Instruction();
        internalInstruction.setAward(award);
        internalInstruction.setStatusCode(caisoInstruction.getStatusCode());
        internalInstruction.setIsoResource(resource);
        internalInstruction.setIsoResourceId(caisoInstruction.getResourceId());
        internalInstruction.setConfigurationId(caisoInstruction.getConfigurationId());
        Date startTime = parseDateFromXmlDateTime(caisoInstruction.getStartTime());
        internalInstruction.setStartTime(startTime);
        Date endTime = parseDateFromXmlDateTime(caisoInstruction.getEndTime());
        internalInstruction.setEndTime(endTime);
        internalInstruction.setDOT(caisoInstruction.getDot());
        String instructionType = caisoInstruction.getInstructionType();
        if (!StringUtils.isEmpty(instructionType)) {
            internalInstruction.setType(Integer.parseInt(instructionType));
        }
        internalInstruction.setAgcFlag(caisoInstruction.getAgcFlag());
        internalInstruction.setRmrFlag(caisoInstruction.getRmrFlag());
        internalInstruction.setValidated(parseDateFromXmlDateTime(caisoInstruction.getValidated()));
        internalInstruction.setValidatedBy(caisoInstruction.getValidatedBy());
        internalInstruction.setApiValidated(parseDateFromXmlDateTime(caisoInstruction.getApiValidated()));
        internalInstruction.setValidatedBy(caisoInstruction.getValidatedBy());
        internalInstruction.setXmlSource(xmlSource);
        internalInstruction.setBatchReceivedTime(batchType.getBatchReceived().toGregorianCalendar().getTime());
        internalInstruction.setBatchSentTime(batchType.getBatchSent().toGregorianCalendar().getTime());
        String revisionNumber = caisoInstruction.getRevisionNumber();
        if (!StringUtils.isEmpty(revisionNumber)) {
            internalInstruction.setRevisionNumber(Integer.parseInt(revisionNumber));
        }
        String bidDelay = caisoInstruction.getBidDelay();
        if (!StringUtils.isEmpty(bidDelay)) {
            internalInstruction.setBidDelay(Integer.parseInt(bidDelay));
        }
        final com.inenergis.entity.genericEnum.InstructionType CAPACITY_AWARD = com.inenergis.entity.genericEnum.InstructionType.CAPACITY_AWARD;
        //if (CAPACITY_AWARD.equals(CAPACITY_AWARD.getType(i.getType()))) {// TODO not sure about that
        DetailType detail = caisoInstruction.getDetail();
        if (detail != null) {
            List<InstructionDetailType> isoInstructionDetails = detail.getInstructionDetail();
            if (isoInstructionDetails != null && !isoInstructionDetails.isEmpty()) {
                List<InstructionDetail> instructionDetails = new ArrayList<>(isoInstructionDetails.size());
                for (InstructionDetailType detailType : isoInstructionDetails) {
                    populateInstructionDetail(internalInstruction, detailType);
                }
            }
        }
        //}
        internalInstruction.setInstructionUID(caisoInstruction.getInstructionUID());
        return internalInstruction;
    }

    private static void populateInstructionDetail(Instruction internalInstruction, InstructionDetailType detailType) {
        InstructionDetail instructionDetail = new InstructionDetail();
        String mw = detailType.getMw();
        if (mw != null) {
            instructionDetail.setCapacity(EnergyUtil.parseToWatts(mw, ElectricalUnit.MW));
        }
        instructionDetail.setServiceType(detailType.getServiceType());
        instructionDetail.setSegNo(Integer.parseInt(detailType.getSegNo()));
        instructionDetail.setInstruction(internalInstruction);
    }

    private static Date parseDateFromXmlDateTime(String sDate) throws DatatypeConfigurationException {
        Date time = null;
        if (!StringUtils.isEmpty(sDate)) {
            time = DatatypeFactory.newInstance().newXMLGregorianCalendar(sDate).toGregorianCalendar().getTime();
        }
        return time;
    }

    private static Date parseDateFromXmlDateTime(XMLGregorianCalendar sDate) throws DatatypeConfigurationException {
        Date time = null;
        if (sDate != null) {
            time = sDate.toGregorianCalendar().getTime();
        }
        return time;
    }

    public static Trajectory convertToTrajectory(Award award, TrajectoryDopType trajectoryDopType, IsoResource resource, String xmlSource, TrajectoryBatchType batchType) throws DatatypeConfigurationException {
        Trajectory trajectory = new Trajectory();
        trajectory.setConfigurationId(trajectoryDopType.getConfigurationId());
        trajectory.setAward(award);
        final Long capacity = EnergyUtil.convertToWatts(trajectoryDopType.getDop(), ElectricalUnit.MW);
        trajectory.setDop(capacity);
        trajectory.setDopUID(trajectoryDopType.getDopUID());
        trajectory.setSequenceNumber(trajectoryDopType.getSequenceNumber());
        Date targetTime = parseDateFromXmlDateTime(trajectoryDopType.getTargetTime());
        trajectory.setTargetTime(targetTime);
        trajectory.setResource(resource);
        trajectory.setXmlSource(xmlSource);
        trajectory.setBatchReceivedTime(batchType.getBatchReceived().toGregorianCalendar().getTime());
        trajectory.setBatchSentTime(batchType.getBatchSent().toGregorianCalendar().getTime());
        return trajectory;
    }
}