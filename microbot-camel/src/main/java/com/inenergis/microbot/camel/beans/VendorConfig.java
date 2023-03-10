package com.inenergis.microbot.camel.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VendorConfig {
    private String vendorName;
    private String address;
    private String user;
    private String pw;
    private String dir;
    private String notifyBy;
    private String program;
    private String camelEndpoint;

    public String getKey() {
        return String.format("%s_%s", program, notifyBy);
    }
}