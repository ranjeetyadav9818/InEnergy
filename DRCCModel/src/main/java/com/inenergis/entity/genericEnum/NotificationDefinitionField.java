package com.inenergis.entity.genericEnum;


public enum NotificationDefinitionField {

    NAME("name"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    NOTIFICATION("notification");

    NotificationDefinitionField(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public String getFieldName() {
        return fieldName;
    }
}
