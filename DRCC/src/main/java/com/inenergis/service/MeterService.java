package com.inenergis.service;

import com.inenergis.dao.MeterDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class MeterService {

    @Inject
    MeterDao meterDao;

    public List<String> getDistinctMtrConfigTypeValues() {
        return meterDao.getDistinctMtrConfigTypeValues();
    }
}