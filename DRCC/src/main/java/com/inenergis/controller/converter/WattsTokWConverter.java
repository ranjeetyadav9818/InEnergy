package com.inenergis.controller.converter;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.util.ConstantsProviderModel;

import javax.faces.convert.FacesConverter;


@FacesConverter(value = "wattsTokWConverter")

public class WattsTokWConverter extends LongBigDecimalConverter {
public static final String NAME = "wattsTokWConverter";

    public WattsTokWConverter(){
        super(ConstantsProvider.THREE_DECIMAL_POSITION_PATTERN, ConstantsProviderModel.ONE_THOUSAND_BIG_DECIMAL);
    }
}
