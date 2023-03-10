package com.inenergis.controller.converter;

import com.inenergis.controller.general.ConstantsProvider;
import com.inenergis.util.ConstantsProviderModel;

import javax.faces.convert.FacesConverter;


@FacesConverter(value = "bidCapacityInMwTwoPrecissionConverter")

public class BidCapacityInMwTwoPrecissionConverter extends LongBigDecimalConverter {
    public BidCapacityInMwTwoPrecissionConverter(){
        super(ConstantsProvider.TWO_DECIMAL_POSITION_PATTERN, ConstantsProviderModel.ONE_MILLION_BIG_DECIMAL);
    }
}
