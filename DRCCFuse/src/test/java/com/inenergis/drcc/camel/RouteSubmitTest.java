package com.inenergis.drcc.camel;

import com.caiso.soa.drregistrationdata_v1.AggregatedPnode;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.drregistrationdata_v1.DemandResponseProvider;
import com.caiso.soa.drregistrationdata_v1.DemandUtilityDistributionCompany;
import com.caiso.soa.drregistrationdata_v1.DistributedActivity;
import com.caiso.soa.drregistrationdata_v1.DistributedEnergyResourceContainer;
import com.caiso.soa.drregistrationdata_v1.LoadAggregationPoint;
import com.caiso.soa.drregistrationdata_v1.LoadServingEntity;
import com.caiso.soa.drregistrationdata_v1.Location;
import com.caiso.soa.drregistrationdata_v1.MessagePayload;
import com.caiso.soa.drregistrationdata_v1.ObjectFactory;
import com.caiso.soa.drregistrationdata_v1.RegisteredGenerator;
import com.caiso.soa.drregistrationdata_v1.StreetAddress;
import com.caiso.soa.drregistrationdata_v1.StreetDetail;
import com.caiso.soa.drregistrationdata_v1.TownDetail;
import com.caiso.soa.standardoutput_v1.StandardOutput;
import com.inenergis.util.ConstantsProviderModel;
import com.inenergis.util.soap.CaisoRequestWrapper;
import com.inenergis.util.soap.SoapRequestHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

public class RouteSubmitTest //extends CamelBlueprintTestSupport
{


    //@Test
    public void testRoute() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        StandardOutput standardOutput = caisoRequestWrapper.submitLocation(generateObjectInXML());
        System.out.println(standardOutput);
        System.out.println(standardOutput.getMessagePayload().getEventLog().getBatch().getMRID());
    }

    private String generateObjectInXML() throws DatatypeConfigurationException, JAXBException, IOException, NoSuchAlgorithmException {

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).toInstant()));
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DRRegistrationData submitRequest = new DRRegistrationData();

        MessagePayload payload = new MessagePayload();
        DistributedEnergyResourceContainer resourceContainer = new DistributedEnergyResourceContainer();
        resourceContainer.setMRID("4455667789");
        resourceContainer.setName("Antonios test4");
        DemandResponseProvider demandResponseProvider = new DemandResponseProvider();
        demandResponseProvider.setMRID("DPGE");
        resourceContainer.setDemandResponseProvider(demandResponseProvider);
        DistributedActivity distributedActivity = new DistributedActivity();
        distributedActivity.setAction("SUBMIT");
        distributedActivity.setSubmittedActiveStartDateTime(xmlDate);
        Date to = Date.from(ZonedDateTime.now().toLocalDate().atStartOfDay(ConstantsProviderModel.CUSTOMER_TIMEZONE_ID).plusYears(2).toInstant());
        c = new GregorianCalendar();
        c.setTime(to);
        xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        distributedActivity.setSubmittedActiveEndDateTime(xmlDate);
        resourceContainer.setDistributedActivity(distributedActivity);
        Location location = new Location();
        StreetAddress mainAddress = new StreetAddress();
        StreetDetail streetDetail = new StreetDetail();
        streetDetail.setAddressGeneral("Test Street2");
        streetDetail.setAddressGeneral2("Test address");
        mainAddress.setStreetDetail(streetDetail);
        TownDetail townDetail = new TownDetail();
        townDetail.setCode("95631");
        townDetail.setName("Folsom");
        townDetail.setStateOrProvince("CA");
        mainAddress.setTownDetail(townDetail);
        location.setMainAddress(mainAddress);
        resourceContainer.setLocation(location);
        RegisteredGenerator registeredGenerator = new RegisteredGenerator();
        AggregatedPnode aggregatedPnode = new AggregatedPnode();
        aggregatedPnode.setMRID("SLAP_PGSB");
        LoadAggregationPoint value = new LoadAggregationPoint();
        value.setAggregatedPnode(aggregatedPnode);
        registeredGenerator.setLoadAggregationPoint(value);
        LoadServingEntity loadServingEntity = new LoadServingEntity();
        loadServingEntity.setMRID("LPGE");
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        resourceContainer.setRegisteredGenerator(registeredGenerator);
        DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID("UPGE");
        resourceContainer.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
//        DemandResponseRegistration demandResponseRegistration = new DemandResponseRegistration();
//        demandResponseRegistration.setMRID("2340");
//        resourceContainer.setDemandResponseRegistration(demandResponseRegistration);
        payload.getDistributedEnergyResourceContainer().add(resourceContainer);
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