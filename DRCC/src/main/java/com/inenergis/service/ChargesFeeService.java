package com.inenergis.service;

import com.inenergis.dao.ChargesFeeDao;
import com.inenergis.entity.program.ChargesFee;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ChargesFeeService {

    @Inject
    ChargesFeeDao chargesFeeDao;

    public List<ChargesFee> getAll() {
        return chargesFeeDao.getAll();
    }

    public List<ChargesFee> getAllComparisonEligible() {
        return chargesFeeDao.getAllComparisonEligible();
    }

    public ChargesFee getById(Long id) {
        return chargesFeeDao.getById(id);
    }
}