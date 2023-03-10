package com.inenergis.service;

import com.inenergis.dao.DeviceLineDao;
import com.inenergis.entity.device.DeviceLine;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class DeviceLineService {

    @Inject
    DeviceLineDao deviceLineDao;

    public void save(DeviceLine deviceLine) {
        deviceLineDao.saveOrUpdate(deviceLine);
    }

    public List<DeviceLine> getAll() {
        return deviceLineDao.getAll();
    }

    public DeviceLine getById(Long id) {
        return deviceLineDao.getById(id);
    }

    public void delete(DeviceLine line) {
        deviceLineDao.delete(line);
    }
}