package com.inenergis.entity.genericEnum;

import java.util.Collections;
import java.util.List;

public enum EssentialCustomerType {
    Y("Yes, Non-Exempt (Y)", Collections.singletonList("YES")),
    E("Yes, Exempt (E)", Collections.singletonList("E")),
    N("No (N)", Collections.singletonList("NULL"));

    private String name;
    private List<String> thirdPartyValues;

    EssentialCustomerType(String name, List<String> thirdPartyValues) {
        this.name = name;
        this.thirdPartyValues = thirdPartyValues;
    }

    public String getName() {
        return name;
    }

    public List<String> getThirdPartyValues() {
        return thirdPartyValues;
    }
}