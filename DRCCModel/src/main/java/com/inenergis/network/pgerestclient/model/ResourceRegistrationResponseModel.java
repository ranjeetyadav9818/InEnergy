package com.inenergis.network.pgerestclient.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResourceRegistrationResponseModel {

    private static final String SUCCESS_RESPONSE_CODE = "0: SUCCESS";

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ErrorList")
    private List<String> errors;

    public boolean isSuccess() {
        return SUCCESS_RESPONSE_CODE.equals(responseCode);
    }
}