package com.inenergis.entity.genericEnum;

import java.util.HashMap;
import java.util.Map;

public enum BidStatus {
    SUBMITTED("Submitted to FBS"),
    ACCEPTED("Submitted to Market"),
    AUTO_BID("Auto Bid"),
    OUTAGE("Outage"),
    NO_BID("No Bid"),
    ACTION_REQUIRED("Action Required"),
    READY_TO_SUBMIT("Ready to be submitted to FBS"),
    EXCEPTIONS("Submitted to Market with exceptions"),
    DELIVERY_ERROR("Submission not accepted due to errors");

    private String name;

    private static Map<BidStatus, String> icons = new HashMap<>();

    static {
        icons.put(BidStatus.SUBMITTED, "icon-ok");
        icons.put(BidStatus.ACCEPTED, "icon-ok");
        icons.put(BidStatus.AUTO_BID, "icon-arrows-cw");
        icons.put(BidStatus.OUTAGE, "icon-bat1");
        icons.put(BidStatus.NO_BID, "icon-block");
        icons.put(BidStatus.ACTION_REQUIRED, "icon-warning-empty");
        icons.put(BidStatus.READY_TO_SUBMIT, "icon-arrows-cw");
        icons.put(BidStatus.EXCEPTIONS, "icon-cancel");
        icons.put(BidStatus.DELIVERY_ERROR, "icon-cancel");
    }

    BidStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icons.get(this);
    }
}