package com.inenergis.drcc.camel;

import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany;
import com.caiso.soa.requestdrregistrationdata_v1.DistributedEnergyResourceContainer;
import com.caiso.soa.requestdrregistrationdata_v1.LoadServingEntity;
import com.caiso.soa.requestdrregistrationdata_v1.LocationRequest;
import com.caiso.soa.requestdrregistrationdata_v1.MessagePayload;
import com.caiso.soa.requestdrregistrationdata_v1.RegisteredGenerator;
import com.caiso.soa.requestdrregistrationdata_v1.RequestDRRegistrationData;
import com.inenergis.util.soap.CaisoRequestWrapper;

public class RouteTest //extends CamelBlueprintTestSupport
{

    //@Test
    public void testRoute() throws Exception{
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveLocation(generateRequestDRRegistrationData());
        System.out.println(drRegistrationData);
    }

    private RequestDRRegistrationData generateRequestDRRegistrationData() {
        RequestDRRegistrationData request = new RequestDRRegistrationData();
        MessagePayload messagePayload = new MessagePayload();
        DistributedEnergyResourceContainer resourceContainer = new DistributedEnergyResourceContainer();
//        resourceContainer.setName("Antonios test");
//        resourceContainer.setMRID("134262000001");
//        resourceContainer.setMRID("4455667788");

        RegisteredGenerator registeredGenerator = new RegisteredGenerator();
        LoadServingEntity loadServingEntity = new LoadServingEntity();
        loadServingEntity.setMRID("LPGE");
        registeredGenerator.setLoadServingEntity(loadServingEntity);
        resourceContainer.setRegisteredGenerator(registeredGenerator);
        DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID("UPGE");
        resourceContainer.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setDistributedEnergyResourceContainer(resourceContainer);

        messagePayload.setLocationRequest(locationRequest);
        request.setMessagePayload(messagePayload);
        return request;
    }




}