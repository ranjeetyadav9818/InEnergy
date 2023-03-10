package com.inenergis.util;


import com.inenergis.entity.PdpSrNotification;
import com.inenergis.entity.PdpSrVendor;

import java.util.List;

public class VendorFileCreator {

    public String createFileContent(PdpSrVendor vendor, List<PdpSrNotification> notifications){
        StringBuilder sb = new StringBuilder();
        notifications.forEach(n -> sb.append("call to ").append(n.getPersonId()).append(" in ").append(n.getLanguage()).append(" to ").append(n.getNotifyByValue()).append(","));
        return sb.toString();
    }

    public String getVendorDirectory(PdpSrVendor vendor){
        return vendor.getVendor();
    }
}
