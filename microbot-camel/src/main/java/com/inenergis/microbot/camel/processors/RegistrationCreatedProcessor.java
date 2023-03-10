package com.inenergis.microbot.camel.processors;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.network.pgerestclient.PgeLayer7;
import com.inenergis.network.pgerestclient.PgeLayer7Emulator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class RegistrationCreatedProcessor implements Processor {


    @Value("${layer7.mocked}")
    private boolean layer7Mocked;

    @Autowired
    @Qualifier("appProperties")
    Properties properties;

    @Override
    public void process(Exchange exchange) throws Exception {
        RegistrationSubmissionStatus registration = (RegistrationSubmissionStatus) exchange.getIn().getBody();
        boolean valid = getPgeLayer7().registerRegistration(registration);
        if (!valid) {
            throw new IllegalStateException("Impossible to inform Layer 7 about new registration: " + registration.getId());
        }
    }

    public PgeLayer7 getPgeLayer7() throws IOException {
        if (layer7Mocked) {
            return new PgeLayer7Emulator(properties);
        } else {
            return new PgeLayer7(properties);
        }
    }
}