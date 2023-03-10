package com.inenergis.controller.converter;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.util.ConstantsProviderModel;

import javax.faces.convert.FacesConverter;


@FacesConverter(value = "bidCapacityInMWConverter")

public class BidCapacityInMWConverter extends LongBigDecimalConverter {
public static final String NAME = "bidCapacityInMWConverter";

    public BidCapacityInMWConverter(){
        super(ConstantsProvider.THREE_DECIMAL_POSITION_PATTERN, ConstantsProviderModel.ONE_MILLION_BIG_DECIMAL);
    }
}
