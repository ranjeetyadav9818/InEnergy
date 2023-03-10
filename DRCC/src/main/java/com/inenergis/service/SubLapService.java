package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.SubLapDao;
import com.inenergis.entity.SubLap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SubLapService {

    @Inject
    SubLapDao subLapDao;

    public void save(SubLap subLap) {
        subLapDao.save(subLap);
    }

    public List<SubLap> getAll() {
        return subLapDao.getAll();
    }

    public List<SubLap> get(int limit) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        return subLapDao.getWithCriteria(conditions, limit);
    }

    public SubLap getById(Long id) {
        return subLapDao.getById(id);
    }

    public SubLap getByCode(String s) {
        return subLapDao.getByCode(s);
    }

    public void saveOrUpdate(SubLap subLap) {
        subLapDao.saveOrUpdate(subLap);
    }
}