package com.inenergis.controller.converter;

import com.inenergis.entity.program.CreditDiscountFee;
import com.inenergis.service.CreditDiscountFeeService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "creditDiscountFeeConverter")
public class CreditDiscountFeeConverter extends GenericConverter<CreditDiscountFee> {

    @Inject
    CreditDiscountFeeService creditDiscountFeeService;

    @Override
    public CreditDiscountFee getById(Long id) {
        return creditDiscountFeeService.getById(id);
    }

}