package com.inenergis.network.pgerestclient;

import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityRequestModel;
import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityResponseModel;
import com.inenergis.network.pgerestclient.model.MeterDataAvailabilityResponseWrapper;
import com.inenergis.network.pgerestclient.model.RequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;


class MeterDataService {

    private static final Logger log = LoggerFactory.getLogger(MeterDataService.class);
    private Properties properties;

    MeterDataService(Properties properties) {
        this.properties = properties;
    }

    boolean validateMeterData(String serviceAgreementId, RequestModel.IdType idType) {
        List<MeterDataAvailabilityResponseModel> availabilityModel;

        try {
            availabilityModel = getMeterDataAvailability(serviceAgreementId, idType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

        return availabilityModel != null && availabilityModel.size() >= Integer.valueOf(properties.getProperty("pge.api.availableDaysToBeReady"));
    }

    private List<MeterDataAvailabilityResponseModel> getMeterDataAvailability(String serviceAgreementId, RequestModel.IdType idType) throws Exception {
        return getMeterDataAvailabilities(Collections.singletonList(serviceAgreementId), idType).getByServiceAgreementId(serviceAgreementId);
    }

    private MeterDataAvailabilityResponseWrapper getMeterDataAvailabilities(List<String> serviceAgreementIds, RequestModel.IdType idType) throws Exception {
        RequestModel requestModel = new MeterDataAvailabilityRequestModel(serviceAgreementIds, "pge.api.url",
                Integer.valueOf(properties.getProperty("pge.api.numberOfDaysToCheck")), idType);

        return new PgeHttpClient(properties).doPost(requestModel, MeterDataAvailabilityResponseWrapper.class);
    }
}