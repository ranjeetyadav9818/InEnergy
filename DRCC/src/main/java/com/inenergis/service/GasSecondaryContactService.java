package com.inenergis.service;

import com.inenergis.dao.GasSecondaryContactDao;
import com.inenergis.entity.GasSecondaryContact;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class GasSecondaryContactService {

    @Inject
    GasSecondaryContactDao secondaryContactDao;

    public void saveOrUpdate(GasSecondaryContact secondaryContact) {
        secondaryContactDao.saveOrUpdate(secondaryContact);
    }

    public void delete(GasSecondaryContact secondaryContact) {
        secondaryContactDao.delete(secondaryContact);
    }

    public List<GasSecondaryContact> getAll() {
        return secondaryContactDao.getAll();
    }

    public GasSecondaryContact getById(Long id) {
        return secondaryContactDao.getById(id);
    }
}