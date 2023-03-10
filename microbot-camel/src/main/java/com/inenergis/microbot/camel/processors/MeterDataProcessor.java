package com.inenergis.microbot.camel.processors;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.network.pgerestclient.PgeLayer7;
import com.inenergis.network.pgerestclient.PgeLayer7Emulator;
import com.inenergis.network.pgerestclient.model.RequestModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class MeterDataProcessor implements Processor {

    private final Logger log = LoggerFactory.getLogger(MeterDataProcessor.class);


    @Value("${layer7.mocked}")
    private boolean layer7Mocked;

    @Autowired
    @Qualifier("appProperties")
    private Properties properties;

    @Override
    public void process(Exchange exchange) throws Exception {
        LocationSubmissionStatus locationSubmissionStatus = (LocationSubmissionStatus) exchange.getIn().getBody();

        String id = locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement().getSaUuid();
        RequestModel.IdType idType = RequestModel.IdType.UUID;

        if (StringUtils.isEmpty(id)) {
            id = locationSubmissionStatus.getProgramServiceAgreementEnrollment().getServiceAgreement().getServiceAgreementId();
            idType = RequestModel.IdType.SAID;
        }
        log.info("Checking Meter Data Availability for ID " + id + "(" + idType + ")");

        boolean validateMeterData = getPgeLayer7().validateMeterData(id, idType);
        if (validateMeterData) {
            log.info("Meter data check is ok for location: " + locationSubmissionStatus.getId());
        } else {
            log.warn("Meter data check failed for location: " + locationSubmissionStatus.getId());
        }
        locationSubmissionStatus.setMeterDataRecheck(!validateMeterData);
    }

    public PgeLayer7 getPgeLayer7() throws IOException {
        if (layer7Mocked) {
            return new PgeLayer7Emulator(properties);
        } else {
            return new PgeLayer7(properties);
        }
    }
}