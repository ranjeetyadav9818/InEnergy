package com.inenergis.entity.genericEnum;


public enum NotificationDefinitionId {

    REVIEW_SPLIT_AND_MERGE_RECORDS("Review Split and Merge records"),
    CDW_ATTRIBUTE_CHANGE_NOTIFICATION("CDW attribute change notification notify customer (BIP only)"),
    SUBLAP_CHANGES_NOTIFICATION("SUBlap changes notification InterACT and APX update required"),
    SUBLAP_CHANGES_NOTIFICATION_ACT_APX("SUBlap changes notification"),
    UNABLE_TO_IDENTIFY_THE_SUBLAP("Unable to identify the SUBlap"),
    PROFILE_END_DATE_ALERT("Profile end date alert", NotificationTemplate.TEMPLATES_WORKFLOW_PROFILE_END_DATE_ALERT_HEADER, NotificationTemplate.TEMPLATES_WORKFLOW_PROFILE_END_DATE_ALERT_BODY),
    ELIGIBILITY_EXCEPTION_REVIEW("Eligibility Exception Review"),
    BAM_PAUSED_ALERT("BAM paused alert", NotificationTemplate.TEMPLATES_WORKFLOW_PAUSED_HEADER_VM, NotificationTemplate.TEMPLATES_WORKFLOW_PAUSED_BODY_VM),
    BAM_RESUME_ALERT("BAM resume alert", NotificationTemplate.TEMPLATES_WORKFLOW_PAUSED_HEADER_VM, NotificationTemplate.TEMPLATES_WORKFLOW_PAUSED_BODY_VM),
    REGISTRATION_EXCEPTION_REVIEW_REMINDER("Registration Exception Review Reminder"),
    MISSING_METER_DATA_EXCEPTION_REVIEW_REMINDER("Missing Meter Data Exception Review reminder"),
    RESOURCE_CAPACITY_REACHING_MAXIMUM_PMAX("Resource capacity reaching maximum (pmax) alert"),
    RESOURCE_CAPACITY_REACHING_MINIMUM_PMIN("Resource capacity reaching minimum (pmin) alert"),
    AUTO_RES_MNT_OFF_ALLOCATE_LOCATIONS_REMINDER("Auto Resource Maintenance off, reminder to allocate Locations reminder"),
    AUTO_BID_OFF_REMINDER_BID_SUBMISSION("Auto-bid off, reminder to complete Bid submission"),
    AUTO_BID_OFF_DEADLINE_APPROACHING("Auto-bid off, Bid deadline approaching reminder (configurable on frequency)"),
    REVIEW_REJECTED_ISO_BIDS_ALERT("Review rejected Market bids alert"),
    REVIEW_RECEIVED_AWARD_ALERT("Review received award (alert)"),
    MARKET_NOT_CLOSED_DISPATCH_WINDOW_APPROACHING("Market not closed but dispatch notification window approaching (alert)"),
    DISPATCH_FAILURE_MANUAL_INTERVENTION("Dispatch failure requiring manual intervention (alert)"),
    NOTIFY_NON_OPERATOR_OF_EVENT_SCHEDULED("Notify non-operator of event scheduled (i.e. CSR, etc.)"),
    NEW_PLAN_INSTANCE_GENERATED("New Plan Instance is created"),
    NEW_TASK_TRIGGERED("New Task Triggered");

    private NotificationTemplate mailHeaderTemplate;
    private NotificationTemplate mailBodyTemplate;
    private String description;

    public String getDescription() {
        return description;
    }

    NotificationDefinitionId(String description, NotificationTemplate mailHeaderTemplate, NotificationTemplate mailBodyTemplate) {
        this.mailHeaderTemplate = mailHeaderTemplate;
        this.mailBodyTemplate = mailBodyTemplate;
        this.description = description;
    }

    NotificationDefinitionId(String description) {
        this.description = description;
        this.mailHeaderTemplate = NotificationTemplate.TEMPLATES_WORKFLOW_ALERT_HEADER_VM;
        this.mailBodyTemplate = NotificationTemplate.TEMPLATES_WORKFLOW_ALERT_BODY_VM;
    }

    public NotificationTemplate getMailBodyTemplate() {

        return mailBodyTemplate;
    }

    public NotificationTemplate getMailHeaderTemplate() {

        return mailHeaderTemplate;
    }

    public String getId() {return getDeclaringClass().getCanonicalName() + '.' + name();}

}
