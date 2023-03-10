package com.inenergis.service;

import com.inenergis.dao.SecondaryContactDao;
import com.inenergis.entity.SecondaryContact;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class SecondaryContactService {

    @Inject
    SecondaryContactDao secondaryContactDao;

    public void saveOrUpdate(SecondaryContact secondaryContact) {
        secondaryContactDao.saveOrUpdate(secondaryContact);
    }

    public void delete(SecondaryContact secondaryContact) {
        secondaryContactDao.delete(secondaryContact);
    }

    public List<SecondaryContact> getAll() {
        return secondaryContactDao.getAll();
    }

    public SecondaryContact getById(Long id) {
        return secondaryContactDao.getById(id);
    }
}