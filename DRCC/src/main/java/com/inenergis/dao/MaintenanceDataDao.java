package com.inenergis.dao;

import com.inenergis.entity.maintenanceData.MaintenanceData;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class MaintenanceDataDao extends GenericDao<MaintenanceData> {

    public MaintenanceDataDao() {
        setClazz(MaintenanceData.class);
    }
}
