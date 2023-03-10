package com.inenergis.dao;

import com.inenergis.entity.program.SeasonCalendar;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class RateSeasonDao extends GenericDao<SeasonCalendar> {
    public RateSeasonDao() {
        setClazz(SeasonCalendar.class);
    }
}
