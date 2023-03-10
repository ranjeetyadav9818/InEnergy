package com.inenergis.drcc.camel;

import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import com.caiso.soa.requestdrregistrationdata_v1.DemandResponseProvider;
import com.caiso.soa.requestdrregistrationdata_v1.DemandResponseRegistrationFull;
import com.caiso.soa.requestdrregistrationdata_v1.DemandUtilityDistributionCompany;
import com.caiso.soa.requestdrregistrationdata_v1.MessagePayload;
import com.caiso.soa.requestdrregistrationdata_v1.RegistrationRequest;
import com.caiso.soa.requestdrregistrationdata_v1.RequestDRRegistrationData;
import com.inenergis.util.soap.CaisoRequestWrapper;

public class RouteRetrieveRegistrationTest //extends CamelBlueprintTestSupport
{


    // @Test
    public void testRoute() throws Exception {
        CaisoRequestWrapper caisoRequestWrapper = new CaisoRequestWrapper(CaisoTestProperties.getPropertiesForTestingCaiso());
        DRRegistrationData drRegistrationData = caisoRequestWrapper.retrieveRegistration(getRequestDRRegistrationData());
        System.out.println(drRegistrationData.getMessagePayload());
    }

    private RequestDRRegistrationData getRequestDRRegistrationData() {
        MessagePayload messagePayload = new MessagePayload();
        RegistrationRequest registrationRequest = new RegistrationRequest();

        registrationRequest.setRequestType("REGISTRATION_ONLY");
        DemandResponseRegistrationFull demandResponseRegistration = new DemandResponseRegistrationFull();
//        demandResponseRegistration.setMRID("133623");
        demandResponseRegistration.setMRID("142201");
//        demandResponseRegistration.setName("Antonio");
//        Status status = new Status();
//        status.setValue("NEW");
//        demandResponseRegistration.setStatus(status);
        DemandResponseProvider demandResponseProvider = new DemandResponseProvider();
        demandResponseProvider.setMRID("DPGE");
        demandResponseRegistration.setDemandResponseProvider(demandResponseProvider);
        DemandUtilityDistributionCompany demandUtilityDistributionCompany = new DemandUtilityDistributionCompany();
        demandUtilityDistributionCompany.setMRID("UPGE");
        demandResponseRegistration.setDemandUtilityDistributionCompany(demandUtilityDistributionCompany);
        registrationRequest.setDemandResponseRegistration(demandResponseRegistration);
//                .setDistributedEnergyResourceContainer(resourceContainer);

        messagePayload.setRegistrationRequest(registrationRequest);

        RequestDRRegistrationData request = new RequestDRRegistrationData();
        request.setMessagePayload(messagePayload);
        return request;
    }

}