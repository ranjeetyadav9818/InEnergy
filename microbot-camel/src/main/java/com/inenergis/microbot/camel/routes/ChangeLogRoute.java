package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.processors.RegistrationCreatedProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChangeLogRoute extends RouteBuilder {

    @Value("${changelog.route.enabled}")
    private boolean enabled;

    @Override
    public void configure() throws Exception {
        if (enabled) {
            from("quartz2://syncChangeLog?cron=15+*+*+*+*+?").id("Apply_Changelogs")
                    .to("bean:registrationService?method=obtainResourcesWithChangelog")
                    .to("bean:registrationService?method=sendChangesToISO")
                    .to("bean:caisoService?method=submitRegistrations")
                    .to("bean:registrationService?method=updateRegistrations")
                    .to("bean:registrationService?method=markAllChangeLogsAsProcessed");

            from("quartz2://ChangeLogInconsistencySolver?cron=0+5,15,25,35,45,55+*+*+*+?").id("ChangeLogInconsistencySolver")
                    .to("bean:registrationService?method=resolveInconsistencies")
                    .split().body()
                    .to("bean:caisoService?method=getRegistrationStatus")
                    .choice()
                    .when(simple("${in.header.newRegistration}"))
                    .process(new RegistrationCreatedProcessor())
                    .log("registration ${body.id} sent to Layer 7 ")
                    .end()
                    .choice()
                    .when(simple("${in.header.killRegistration}"))
                    .to("bean:registrationService?method=deleteRegistration")
                    .otherwise()
                    .to("jpa:com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus")
                    .end();
        }
    }
}
