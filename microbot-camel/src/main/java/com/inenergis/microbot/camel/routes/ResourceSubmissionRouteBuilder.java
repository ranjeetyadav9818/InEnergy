package com.inenergis.microbot.camel.routes;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.microbot.camel.processors.RegistrationCreatedProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class ResourceSubmissionRouteBuilder extends RouteBuilder {

    @Value("${resource.route.enabled}")
    private boolean enabled;
    @Value("${caiso.mocked}")
    private boolean caisoMocked;

    @Autowired
    private RegistrationCreatedProcessor registrationCreatedProcessor;

    @Override
    public void configure() throws Exception {
        if (caisoMocked) {
            from("quartz2://registrationStatusCheckerMock?cron=30+*+*+*+*+?").id("registrationStatusCheckerMock")
                    .to("bean:resourceService?method=getPendingRegistrations")
                    .split().body()
                    .process(exchange -> {
                        RegistrationSubmissionStatus registration = ((RegistrationSubmissionStatus) exchange.getIn().getBody());
                        if(registration.getRegistrationStatus().equals(RegistrationSubmissionStatus.RegistrationStatus.PENDING)){
                            registration.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.REGISTERED);
                            registration.setIsoRegistrationId("DEMO-REG-"+ ThreadLocalRandom.current().nextInt(10000,100000));
                        } else {
                            registration.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.FINISHED);
                        }
                    })
                    .to("jpa:com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus")
                    .log("registration ${body.id} saved/updated");
        }
        if (enabled) {
            from("quartz2://locationAssignationScheduler?cron=0+0/2+*+*+*+?").id("locationAssignationScheduler")
                    .log("checking new locations to be registered into resources")
                    .transacted()
                    .to("bean:locationService?method=getInactiveLocations")
                    .to("bean:resourceService?method=getAvailableResources")
                    .to("bean:resourceService?method=assignLocationsToResources")
                    .split().body()
                    .to("bean:caisoService?method=createNewRegistrationForResource")
                    .to("jpa:com.inenergis.entity.locationRegistration.IsoResource")
                    .log("new registration created for resource ${body.id}");

            from("quartz2://registrationStatusChecker?cron=30+0/2+*+*+*+?").id("registrationStatusChecker")
                    .transacted()
                    .to("bean:resourceService?method=getPendingRegistrations")
                    .split().body()
                    .to("bean:caisoService?method=getRegistrationStatus")
                    .choice()
                    .when(simple("${in.header.newRegistration}"))
                    .process(registrationCreatedProcessor)
                    .log("registration ${body.id} sent to Layer 7 ")
                    .end()
                    .to("jpa:com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus")
                    .log("registration ${body.id} saved/updated");
        }
    }
}
