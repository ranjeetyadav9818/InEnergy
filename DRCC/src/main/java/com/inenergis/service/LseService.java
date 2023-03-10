package com.inenergis.service;

import com.inenergis.dao.LseDao;
import com.inenergis.entity.Lse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class LseService {

    @Inject
    LseDao lseDao;

    public void save(Lse lse) {
        lseDao.save(lse);
    }

    public List<Lse> getAll() {
        return lseDao.getAll();
    }

    public Lse getById(Long id) {
        return lseDao.getById(id);
    }

    public Lse getByCode(String s) {
        return lseDao.getByCode(s);
    }

    public void saveOrUpdate(Lse lse) {
        lseDao.saveOrUpdate(lse);
    }
}