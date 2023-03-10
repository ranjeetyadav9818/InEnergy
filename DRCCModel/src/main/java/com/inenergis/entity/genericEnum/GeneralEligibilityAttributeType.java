package com.inenergis.entity.genericEnum;

public enum GeneralEligibilityAttributeType {
    PREMISE_TYPE("Premise Type", "premiseType"),
    HAS_3RD_PARTY_DRP("Has 3rd Party DRP", "has3rdPartyDrp"),
    CUST_CLASS_CD("Customer Class CD", "custClassCd"),
    CUST_SIZE("Customer Size", "custSize"),
    MTR_CONFIG_TYPE("MTR Config Type", "mtrConfigType");

    private String name;
    private String modelVariable;

    GeneralEligibilityAttributeType(String name, String modelVariable) {
        this.name = name;
        this.modelVariable = modelVariable;
    }

    public String getName() {
        return name;
    }

    public String getModelVariable() {
        return modelVariable;
    }
}