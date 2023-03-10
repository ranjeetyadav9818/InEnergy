package com.inenergis.microbot.camel.beans;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class VendorMapping {
    private long vendorId;
    private String statusCode;
    private String displayMessage;
    private String successfulNotification;
}