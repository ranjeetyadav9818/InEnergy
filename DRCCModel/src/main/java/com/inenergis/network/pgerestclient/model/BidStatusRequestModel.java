package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BidStatusRequestModel extends RequestModel {

    private final static String URL_TOKEN = "pge.api.bidstatus.url";

    @SuppressWarnings("unused")
    private class Resource {
        private String resourceId;
    }

    @SerializedName("resources")
    Map<String, List<Resource>> resources = new HashMap<>();

    @SerializedName("bidDate")
    String bidDate;

    public BidStatusRequestModel(String resourceId, String bidDate) throws IOException {
        urlToken = URL_TOKEN;

        this.bidDate = bidDate;

        Resource resource = new Resource();
        resource.resourceId = resourceId;

        resources.put("resource", Collections.singletonList(resource));
    }
}