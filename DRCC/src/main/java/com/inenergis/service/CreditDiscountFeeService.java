package com.inenergis.service;

import com.inenergis.dao.CreditDiscountFeeDao;
import com.inenergis.entity.program.CreditDiscountFee;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CreditDiscountFeeService {

    @Inject
    CreditDiscountFeeDao creditDiscountFeeDao;

    public List<CreditDiscountFee> getAll() {
        return creditDiscountFeeDao.getAll();
    }

    public List<CreditDiscountFee> getAllComparisonEligible() {
        return creditDiscountFeeDao.getAllComparisonEligible();
    }

    public CreditDiscountFee getById(Long id) {
        return creditDiscountFeeDao.getById(id);
    }
}