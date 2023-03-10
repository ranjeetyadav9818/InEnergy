package com.inenergis.entity.genericEnum;


public enum NotificationTemplate {

    TEMPLATES_WORKFLOW_PROFILE_END_DATE_ALERT_HEADER ("templates/workflow-profile-end-date-alert-h.vm"),
    TEMPLATES_WORKFLOW_PROFILE_END_DATE_ALERT_BODY ("templates/workflow-profile-end-date-alert-b.vm"),
    TEMPLATES_WORKFLOW_ALERT_BODY_VM ("templates/workflow-alert-body.vm"),
    TEMPLATES_WORKFLOW_ALERT_HEADER_VM ( "templates/workflow-alert-header.vm"),
    TEMPLATES_WORKFLOW_PAUSED_HEADER_VM ( "templates/workflow-paused-header.vm"),
    TEMPLATES_WORKFLOW_PAUSED_BODY_VM ("templates/workflow-paused-body.vm");

    private String text;

    NotificationTemplate(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
