package com.inenergis.service;

import com.inenergis.dao.ManufacturerDao;
import com.inenergis.entity.Manufacturer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ManufacturerService {

    @Inject
    private ManufacturerDao manufacturerDao;

    public void save(Manufacturer manufacturer) {
        manufacturerDao.save(manufacturer);
    }

    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    public Manufacturer getById(Long id) {
        return manufacturerDao.getById(id);
    }
}