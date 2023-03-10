package com.inenergis.dao;

import com.inenergis.entity.device.DeviceLine;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class DeviceLineDao extends GenericDao<DeviceLine> {
    public DeviceLineDao() {
        setClazz(DeviceLine.class);
    }
}
