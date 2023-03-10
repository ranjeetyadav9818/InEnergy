package com.inenergis.network.pgerestclient.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PeakDemandResponseWrapper {
    Map<String, List<PeakDemandResponseModel>> availabilities = new HashMap<>();
}