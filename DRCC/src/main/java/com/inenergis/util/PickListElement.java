package com.inenergis.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PickListElement {
    private String name;
    private String code;

    public PickListElement(String name) {
        this(name, name);
    }

    public PickListElement(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
