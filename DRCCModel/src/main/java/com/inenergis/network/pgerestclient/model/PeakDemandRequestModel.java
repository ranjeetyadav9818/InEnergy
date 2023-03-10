package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PeakDemandRequestModel extends RequestModel {

    private final static String URL_TOKEN = "pge.api.peakdemand.url";

    @SerializedName("arrayOfIds")
    private List<IdModel> ids = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private IdType idType;

    public PeakDemandRequestModel(List<String> ids, IdType idType) {
        this.responseModel = MeterDataAvailabilityResponseWrapper.class;
        this.ids = ids.stream().map(IdModel::new).collect(Collectors.toList());
        this.startDate = LocalDateTime.now().minusYears(1);
        this.endDate = LocalDateTime.now();
        this.urlToken = URL_TOKEN;
        this.idType = idType;
    }
}