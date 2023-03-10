package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BidStatusResponseModel {

    private static final String SUCCESS_RESPONSE_CODE = "0";
    private static final String SUCCESS_RESPONSE_MESSAGE = "SUCCESS";

    @Getter
    public static class Bid {
        @SerializedName("ResourceId")
        private String resourceId;

        @SerializedName("BidDate")
        private String bidDate;

        @SerializedName("Market")
        private String market;

        @SerializedName("Product")
        private String product;

        @SerializedName("He")
        private String he;

        @SerializedName("Mw")
        private String mw;

        @SerializedName("Price")
        private String price;

        @SerializedName("Status")
        private String status;

        @SerializedName("CaisoMessage")
        private String caisoMessage;
    }

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMessage")
    private String responseMessage;

    @SerializedName("Bids")
    private Map<String, List<Bid>> bids = new HashMap<>();

    {
        bids = new HashMap<>();
        bids.put("Bid", Collections.singletonList(new Bid()));
    }
}