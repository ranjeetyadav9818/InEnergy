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
public class BidResponseModel {

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

        @SerializedName("Segment")
        private Integer segment;

        @SerializedName("Status")
        private String status;

        @SerializedName("ErrorMessage")
        private String errorMessage;
    }

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMessage")
    private String responseMessage;

    @SerializedName("FbsResponseStatus")
    private String fbsResponseStatus;

    @SerializedName("BidPackages")
    private Map<String, List<BidResponseModel.Bid>> bids = new HashMap<>();

    public boolean isSuccess() {
        List<Bid> aPackage = getPackage();
        if (aPackage == null) {
            return false;
        }
        boolean anyError = aPackage.stream().anyMatch(p -> p.getStatus().equalsIgnoreCase("error"));
        return ! anyError;
    }

    public List<Bid> getPackage() {
        if(getBids()==null){
            return null;
        }
        return getBids().get("BidPackage");
    }
}