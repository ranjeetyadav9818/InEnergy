package com.inenergis.entity;

import java.util.List;


public interface VModelEntity {
    String idFieldName();

    List<String> relationshipFieldNames();

    List<String> excludedFieldsToCompare();
}
