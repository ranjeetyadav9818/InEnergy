package com.inenergis.service;

import com.inenergis.dao.RateSeasonDao;
import com.inenergis.entity.program.SeasonCalendar;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class RateSeasonService {

    @Inject
    private RateSeasonDao rateSeasonDao;

    public SeasonCalendar getById(Long id) {
        return rateSeasonDao.getById(id);
    }


    public SeasonCalendar saveOrUpdate(SeasonCalendar ratePlan) {
        return rateSeasonDao.saveOrUpdate(ratePlan);
    }
}