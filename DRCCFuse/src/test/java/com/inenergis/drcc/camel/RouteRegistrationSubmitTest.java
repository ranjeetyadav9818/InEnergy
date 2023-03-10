package com.inenergis.drcc.camel;

import com.caiso.soa.drregistrationdata_v1.AggregatedPnode;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.drregistrationdata_v1.DemandResponseProvider;
import com.caiso.soa.drregistrationdata_v1.DemandResponseRegistrationFull;
import com.caiso.soa.drregistrationdata_v1.DemandUtilityDistributionCompany;
import com.caiso.soa.drregistrationdata_v1.DistributedActivity;
import com.caiso.soa.drregistrationdata_v1.DistributedEnergyResourceContainerLocation;
import com.caiso.soa.drregistrationdata_v1.LoadAggregationPointDRRegistration;
import com.caiso.soa.drregistrationdata_v1.LoadServingEntity;
import com.caiso.soa.drregistrationdata_v1.MessagePayload;
import com.caiso.soa.drregistrationdata_v1.ObjectFactory;
import com.caiso.soa.drregistrationdata_v1.RegisteredGeneratorDRRegistration;
import com.caiso.soa.drregistrationdata_v1.SchedulingCoordinator;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.soap.CaisoObjectConverter;
import com.inenergis.util.soap.RegistrationAction;
import com.inenergis.util.soap.SoapRequestHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class RouteRegistrationSubmitTest //extends CamelBlueprintTestSupport
{


    //@Test
    public void testRoute() throws Exception {
//        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
//        String objectAsXML = getObjectAsXML2();
//        System.out.println(objectAsXML);
//        StandardOutput standardOutput = caisoRequestWrapper.submitRegistration(objectAsXML);
//        System.out.println(standardOutput);

        RegistrationSubmissionStatus registration = new RegistrationSubmissionStatus();
        registration.setIsoBatchId("isoBatchID");
        IsoResource isoResource = new IsoResource();
        isoResource.setName("resName");
        IsoProduct isoProduct = new IsoProduct();
        IsoProfile profile = new IsoProfile();
        profile.setUdcId("udc");
        profile.setDrpId("drp");
        isoProduct.setProfile( profile);
        isoResource.setIsoProduct(isoProduct);
        registration.setIsoResource(isoResource);
        registration.setActiveStartDate(new Date());
        registration.setActiveEndDate(new Date());
        RegistrationAction regAction = RegistrationAction.builder().action("SUBMIT").registration(registration).build();
        DRRegistrationData drRegistrationData = CaisoObjectConverter.convertToRegistrationData(Arrays.asList(regAction));
        System.out.println(drRegistrationData);
    }

    private String getObjectAsXML2() throws IOException {
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><DRRegistrationData xmlns=\"http://www.caiso.com/soa/DRRegistrationData_v1.xsd#\"><MessagePayload>" +
                "<DemandResponseRegistration_Full>" +
                "<mRID>PGSB_1_RDRR02 - 1</mRID><name>PGSB_1_RDRR02 - 1</name>" +
                "<DemandResponseProvider><mRID>DPGE</mRID></DemandResponseProvider>" +
                "<DistributedActivity><action>SUBMIT</action><submittedActiveEndDateTime>2018-02-15T08:00:00.000Z</submittedActiveEndDateTime><submittedActiveStartDateTime>2017-02-15T08:00:00.000Z</submittedActiveStartDateTime></DistributedActivity>" +
                "<RegisteredGenerator xsi:type=\"RegisteredGenerator_DRRegistration\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<LoadAggregationPoint><AggregatedPnode><mRID>SLAP_PGSB</mRID></AggregatedPnode></LoadAggregationPoint>" +
                "<LoadServingEntity><mRID>LPGE</mRID></LoadServingEntity>" +
                "<SchedulingCoordinator><mRID>PCG2</mRID></SchedulingCoordinator>" +
                "</RegisteredGenerator>" +
                "<terminateRegistrationFlag>false</terminateRegistrationFlag>" +
                "<DemandUtilityDistributionCompany><mRID>UPGE</mRID></DemandUtilityDistributionCompany>" +
                "<baselineMethod>10 in 10 with SMA</baselineMethod>" +
                "<program>Reliability DR</program>" +
                "<resourceType>Pre Defined</resourceType>" +
                "<DistributedEnergyResourceContainer><locationID>134310000001</locationID></DistributedEnergyResourceContainer>" +
                "</DemandResponseRegistration_Full></MessagePayload></DRRegistrationData>";
        return SoapRequestHelper.zipAndBase64AString(s);
    }

    private String getObjectAsXML() throws DatatypeConfigurationException, JAXBException, IOException, NoSuchAlgorithmException {
        DRRegistrationData submitRequest = new DRRegistrationData();
        MessagePayload payload = new MessagePayload();


        DemandResponseRegistrationFull demandResponseRegistrationFull = new DemandResponseRegistrationFull();
        demandResponseRegistrationFull.setName("Inenergis ws test2");
        DemandResponseProvider demandResponseProvider = new DemandResponseProvider();
        demandResponseProvider.setMRID("DPGE");
        demandResponseRegistrationFull.setDemandResponseProvider(demandResponseProvider);
        DistributedActivity distributedActivity = new DistributedActivity();
        distributedActivity.setAction("SUBMIT");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant()));
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveStartDateTime(xmlDate);
        Date to = Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusYears(1).toInstant());
        c = new GregorianCalendar();
        c.setTime(to);
        xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveEndDateTime(xmlDate);
        demandResponseRegistrationFull.setDistributedActivity(distributedActivity);
        RegisteredGeneratorDRRegistration registeredGenerator = new RegisteredGeneratorDRRegistration();
        registeredGenerator.setMRID("PGEB_2_RDRR02");
        LoadAggregationPointDRRegistration loadAggregationPoint = new LoadAggregationPointDRRegistration();
        loadAggregationPoint.setMRID("DLAP_PGAE_PCG2");
        AggregatedPnode aggregatedPnode = new AggregatedPnode();
        aggregatedPnode.setMRID("SLAP_PGSB");
        loadAggregationPoint.setAggregatedPnode(aggregatedPnode);
        registeredGenerator.setLoadAggregationPoint(loadAggregationPoint);
        LoadServingEntity loadServingEntity = new LoadServingEntity();
        loadServingEntity.setMRID("LPGE");
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        SchedulingCoordinator schedulingCoordinator = new SchedulingCoordinator();
        schedulingCoordinator.setMRID("PCG2");
        registeredGenerator.setSchedulingCoordinator(schedulingCoordinator);
        demandResponseRegistrationFull.setRegisteredGenerator(registeredGenerator);
        demandResponseRegistrationFull.setTerminateRegistrationFlag(false);
        DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID("UPGE");
        demandResponseRegistrationFull.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
        demandResponseRegistrationFull.setBaselineMethod("Meter Generation Output");
        demandResponseRegistrationFull.setProgram("Proxy DR");
        demandResponseRegistrationFull.setResourceType("Pre Defined");
        DistributedEnergyResourceContainerLocation distributedEnergyResourceContainerLocation = new DistributedEnergyResourceContainerLocation();
        distributedEnergyResourceContainerLocation.setLocationID(BigInteger.valueOf(134262000001l));
        demandResponseRegistrationFull.getDistributedEnergyResourceContainer().add(distributedEnergyResourceContainerLocation);
        payload.getDemandResponseRegistrationFull().add(demandResponseRegistrationFull);


        submitRequest.setMessagePayload(payload);


        JAXBContext jaxbContext = JAXBContext.newInstance(DRRegistrationData.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        ObjectFactory factory = new ObjectFactory();
        jaxbMarshaller.marshal(factory.createDRRegistrationData(submitRequest), sw);

        System.out.println(sw.toString());

        return SoapRequestHelper.zipAndBase64AString(sw.toString());
    }
}