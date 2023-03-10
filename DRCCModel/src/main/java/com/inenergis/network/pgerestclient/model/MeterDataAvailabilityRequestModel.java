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
public class MeterDataAvailabilityRequestModel extends RequestModel {

    private static final String DEFAULT_RESPONSE_LEVEL = "DETAILED";
    private static final String DEFAULT_DIRECTION = "DELIVERED";

    @SerializedName("arrayOfIds")
    private List<IdModel> ids = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private IdType idType;
    private String responseLevel = DEFAULT_RESPONSE_LEVEL;
    private String direction = DEFAULT_DIRECTION;

    public MeterDataAvailabilityRequestModel(List<String> ids, String urlToken, int daysBeforeToday, IdType idType) {
        this.responseModel = MeterDataAvailabilityResponseWrapper.class;
        this.ids = ids.stream().map(IdModel::new).collect(Collectors.toList());
        this.startDate = LocalDateTime.now().minusDays(daysBeforeToday);
        this.endDate = LocalDateTime.now();
        this.urlToken = urlToken;
        this.idType = idType;
    }
}