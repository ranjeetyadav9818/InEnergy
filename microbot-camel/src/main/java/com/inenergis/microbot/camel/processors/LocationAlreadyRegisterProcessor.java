package com.inenergis.microbot.camel.processors;

import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;

public class LocationAlreadyRegisterProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(LocationAlreadyRegisterProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        LocationSubmissionStatus locationSubmissionStatus = ((LocationSubmissionStatus) exchange.getIn().getBody());
        LocationSubmissionException exception = new LocationSubmissionException();
        exception.onCreate();
        exception.setLocationSubmissionStatus(locationSubmissionStatus);
        exception.setDateAdded(new Date());
        exception.setType("Location is already registered with Market ID: "+locationSubmissionStatus.getPreviousIsoId());
        locationSubmissionStatus.setExceptions(Collections.singletonList(exception));
        log.error(exception.getType());
    }
}