package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeterDataAvailabilityResponseModel {
    @SerializedName("SpId")
    private String servicePoint;

    @SerializedName("UsageDate")
    private String usageDate;

    @SerializedName("Direction")
    private String direction;

    @SerializedName("ActualMeterReadingPercent")
    private String actualMeterReadingPercent;

    @SerializedName("EstimatedMeterReadingPercent")
    private String estimatedMeterReadingPercent;
}