package com.inenergis.network.pgerestclient.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MeterDataAvailabilityResponseWrapper {
    Map<String, List<MeterDataAvailabilityResponseModel>> availabilities = new HashMap<>();

    public List<MeterDataAvailabilityResponseModel> getByServiceAgreementId(String id) {
        return availabilities.get(id);
    }
}


