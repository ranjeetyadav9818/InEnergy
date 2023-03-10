package com.inenergis.microbot.camel.routes;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus.LocationStatus;
import com.inenergis.microbot.camel.beans.DeadletterQueueListener;
import com.inenergis.microbot.camel.beans.LocationQueueListener;
import com.inenergis.microbot.camel.processors.LocationAlreadyRegisterProcessor;
import com.inenergis.microbot.camel.processors.LocationEligibilityProcessor;
import com.inenergis.microbot.camel.processors.MeterDataProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationSubmissionRouteBuilder extends RouteBuilder {

    @Value("${location.route.enabled}")
    private boolean enabled;
    @Value("${caiso.mocked}")
    private boolean caisoMocked;

    @Autowired
    private DeadletterQueueListener deadletterQueueListener;
    @Autowired
    private LocationQueueListener locationQueueListener;
    @Autowired
    private MeterDataProcessor meterDataProcessor;

    @Override
    public void configure() throws Exception {

        configureDirects();

        if(caisoMocked){
            from("quartz2://locationStatusChecker?cron=0+*+*+*+*+?").id("locationStatusChecker")
                    .to("bean:locationService?method=getPendingStatuses")
                    .split().body()
                    .to("bean:caisoService?method=retrieveLocationStatus")
                    .to("bean:locationService?method=isoStatusChanged")
                    .end();
        }

        from("quartz2://reviewedExceptionsChecker?cron=0+*+*+*+*+?").id("reviewedExceptionsChecker")
                .to("bean:enrollmentService?method=getEnrollmentsWithReviewedExceptions")
                .split().body()
                .setProperty("exceptionProperty", simple("${body}"))
                .to("bean:enrollmentService?method=exceptionsForwarded")
                .choice()
                .when(simple("${property.enrollmentProperty.canContinue()}"))
                    .setBody(simple("${body.locationSubmissionStatus}"))
                    .to("direct:registerLocationTransaction")
                    .log("location status with id ${body} sent directly to location preparation")
                .otherwise()
                    .setBody(simple("${body.locationSubmissionStatus.programServiceAgreementEnrollment.id}"))
                    .bean(locationQueueListener, "sendToEnrollment")
                    .log("location status with id ${body} sent to enrollment queue")
                .end();

        if (!enabled) {
            return;
        }

        from("quartz2://enrollmentDeadletterQuartz?cron=0+*+*+*+*+?").id("enrollmentDeadletterQuartz")
                .bean(deadletterQueueListener, "retrieveEnrollmentDeadLetter");

        from("quartz2://unenrollmentDeadletterQuartz?cron=0+47+*+*+*+?").id("unenrollmentDeadletterQuartz")
                .bean(deadletterQueueListener, "retrieveUnenrollmentDeadLetter");

        from("quartz2://reenrollmentDeadletterQuartz?cron=0+52+*+*+*+?").id("reenrollmentDeadletterQuartz")
                .bean(deadletterQueueListener, "retrieveReenrollmentDeadLetter");

        String nonRegistrable = LocationStatus.NON_REGISTRABLE.getText();
        String pendingReprocess = LocationStatus.PENDING_REPROCESS.getText();
        from("quartz2://locationStatusChecker?cron=0+0+*+*+*+?").id("locationStatusChecker")
                .to("bean:locationService?method=getPendingStatuses")
                .split().body()
                .choice()
                .when(simple("${body.status} == '" + nonRegistrable + "'"))
                .to("bean:caisoService?method=unregisterLocation")
                .to("bean:locationService?method=cleanExceptions")
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .when(simple("${body.status} == '" + pendingReprocess + "'"))
                .to("bean:caisoService?method=unregisterLocation")
                .to("bean:locationService?method=cleanExceptions")
                .setHeader("location", simple("${body}"))
                .setBody(simple("${body.id}"))
                .bean(locationQueueListener, "sendToReenrollment")
                .setBody(header("location"))
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .otherwise()
                .to("bean:caisoService?method=retrieveLocationStatus")
                .to("bean:locationService?method=isoStatusChanged")
                .end();

        from("quartz2://meterDataRechecker?cron=0+0/3+*+*+*+?").id("meterDataRechecker")
                .to("bean:locationService?method=getLocationsWithPendingMeterData")
                .split().body()
                .process(meterDataProcessor)
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus");


        from("quartz2://reinstateEnrollments?cron=0+*+*+*+*+?").id("reinstateEnrollments")
                .to("bean:enrollmentService?method=getReinstateEnrollments")
                .split().body()
                .setBody(simple("${body.getLastLocation()}"))
                .to("bean:caisoService?method=createNewLocationFromExistingOne")
                .to("bean:locationService?method=markEnrollmentAsReinstated")
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .to("bean:locationService?method=generateHistoryForLocationCreation");


        from("quartz2://inactiveLocations?cron=0+0/20+*+*+*+?").id("inactiveLocations")
                .to("bean:locationService?method=getActiveLocationsWithAllRegistrationsFinished")
                .split().body()
                .to("bean:locationService?method=setLocationStatusToInactive");
    }

    private void configureDirects() {

        from("direct:locationEnrollment").id("EnrollmentEntryPoint")
                .onException(Throwable.class)
                    .log("Location enrollment failed for ${body} exception: ${exception.stacktrace}")
                    .maximumRedeliveries(5)
                    .redeliveryDelay(5000)
                    .useOriginalMessage()
                    .bean(locationQueueListener, "sendToEnrollmentDeadletter")
                    .handled(true)
                .end()
                .split()
                .tokenize("\n")
                .streaming()
                .log("new enrollment received through the queue: ${body}")
                .to("bean:enrollmentService?method=getEnrollment")
                .choice()
                .when(simple("${property.enrollment} == null"))
                .throwException(new IllegalArgumentException("enrollment ${body} doesn't exist in the database"))
                .otherwise()
                .log("enrollment ${body} sent to direct queue")
                .to("direct:locationEnrollmentAnalyse")
                .end();

        from("direct:locationEnrollmentAnalyse").id("locationEnrollmentAnalyse")
                .onException(Throwable.class)
                .log("Location enrollment failed for ${body} exception: ${exception.stacktrace}")
                .maximumRedeliveries(5)
                .redeliveryDelay(5000)
                .useOriginalMessage()
                .bean(locationQueueListener, "sendToEnrollmentDeadletter")
                .handled(true)
                .end()
                .log("enrollment ${body} received in direct queue")
                .to("bean:dataMappingService?method=getLseData")
                .to("bean:dataMappingService?method=getSubLapData")
                .process(new LocationEligibilityProcessor())
                .choice()
                .when(simple("${property.isLocationEligible}"))
                .to("bean:caisoService?method=isLocationRegistered")
                .choice()
                .when(simple("${property.isLocationRegistered}"))
                .process(new LocationAlreadyRegisterProcessor())
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .otherwise()
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .log("Location eligible, no exceptions found")
                .to("direct:meterDataChecker")
                .endChoice()
                .otherwise()
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .log("Location ineligible, some exceptions found")
                .end();

        from("direct:meterDataChecker").id("MeterDataAvailabilityCheck")
                .log("Starting Meter Data Availability check...")
                .process(meterDataProcessor)
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .to("direct:registerLocationTransaction");

        from("direct:registerLocationTransaction").id("registerLocationTransaction")
                .to("bean:caisoService?method=registerLocation")
                .to("bean:locationService?method=markAsProcessing");


        from("direct:locationUnenrollment?").id("UnenrollmentEntryPoint")
                .onException(Throwable.class)
                .log("Location unenrollment failed  ${exception.stacktrace}")
                .maximumRedeliveries(5)
                .redeliveryDelay(5000)
                .useOriginalMessage()
                .bean(locationQueueListener, "sendToUnenrollmentDeadletter")
                .handled(true)
                .end()
                .log("new unenrollment received through the queue: ${body}")
                .to("bean:enrollmentService?method=getEnrollment")
                .to("bean:enrollmentService?method=createUnenrollmentChangelog")
                .to("bean:locationService?method=getLocationByEnrollment")
                .to("bean:caisoService?method=isLocationRegistered")
                .choice()
                .when(simple("${header.locationNotExist}"))
                .setBody(simple("${header.enrollment}"))
                .to("jpa:com.inenergis.entity.program.ProgramServiceAgreementEnrollment")
                .log("enrollment cancelled ${body.id}")
                .when(simple("${property.isLocationRegistered}"))
                .to("bean:caisoService?method=unregisterLocation")
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .otherwise()
                .to("bean:caisoService?method=retrieveLocationStatus")
                .to("bean:locationService?method=isoStatusChanged")
                .end();


        from("direct:locationReenrollment").id("locationReenrollmentImplementation")
                .onException(Throwable.class)
                .log("Location reenrollment failed  ${exception.stacktrace}")
                .maximumRedeliveries(5)
                .redeliveryDelay(5000)
                .useOriginalMessage()
                .bean(locationQueueListener, "sendToReenrollmentDeadletter")
                .handled(true)
                .end()
                .log("new reenrollment received through the queue: ${body}")
                .to("bean:locationService?method=getLocation")
                .to("bean:caisoService?method=createNewLocationFromExistingOne")
                .to("jpa:com.inenergis.entity.locationRegistration.LocationSubmissionStatus")
                .to("bean:locationService?method=generateHistoryForLocationCreation");

    }
}