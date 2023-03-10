package com.inenergis.rest;

import com.inenergis.rest.model.eventOutage.EventsAndOutagesRequest;
import com.inenergis.rest.model.eventOutage.EventsAndOutagesResponse;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsRequest;
import com.inenergis.rest.model.registrationDetails.RegistrationDetailsResponse;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsRequest;
import com.inenergis.rest.model.resourceDetails.ResourceDetailsResponse;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorRequest;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorResponse;
import com.inenergis.rest.services.EventsAndOutagesRESTService;
import com.inenergis.rest.services.RegistrationDetailRESTService;
import com.inenergis.rest.services.ResourceDetailsRESTService;
import com.inenergis.rest.services.VoltageIndicatorRESTService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/MarketIntegration")
public class MarketIntegrationRestController {

    @Inject
    ResourceDetailsRESTService resourceDetailsRESTService;

    @Inject
    VoltageIndicatorRESTService voltageIndicatorRESTService;

    @Inject
    EventsAndOutagesRESTService eventsAndOutagesRESTService;

    @Inject
    RegistrationDetailRESTService registrationDetailRESTService;

    Logger log = LoggerFactory.getLogger(MarketIntegrationRestController.class);


    @POST
    @Path("/getResourceDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceDetailsResponse getResourceDetails(ResourceDetailsRequest registrationRequest){
        log.info("getResourceDetailsReceived: "+registrationRequest);
        //TO TEST {"ResourceDetailsRequest":{"resourceIds":[{"pgeResource":true,"resourceId":"1"}]}}
        return resourceDetailsRESTService.getRResourceDetails(registrationRequest.getResourceDetailsRequest());
    }

    @POST
    @Path("/getVoltageIndicator")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VoltageIndicatorResponse getVoltageIndicator(VoltageIndicatorRequest voltageIndicatorList){
        //TO TEST {"VoltageIndicatorRequest":{"spIds":[{"spId":"0000010229"},{"spId":"0000048275"},{"spId":"0000136401"}]}}
        return voltageIndicatorRESTService.getVoltageIndicator(voltageIndicatorList);
    }

    @POST
    @Path("/getEventsAndOutages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public EventsAndOutagesResponse getEventsAndOutages(EventsAndOutagesRequest input){
        //TO TEST {"EventsAndOutageDaysRequest":{"endDate":"2016-12-02T17:58:37","pgeResource":true,"resourceId":"1","startDate":"2016-12-02T17:58:37"}}
        return eventsAndOutagesRESTService.getEventsAndOutages(input);
    }

    @POST
    @Path("/getRegistrationDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RegistrationDetailsResponse getRegistrationDetails(RegistrationDetailsRequest input){
        //TO TEST {"RegistrationDetailsRequest":{"pgeResource":true,"registrationId":"1"}}
        return registrationDetailRESTService.getRegistrationDetails(input);
    }
}