package com.inenergis.service;

import com.inenergis.dao.MaintenanceDataDao;
import com.inenergis.entity.maintenanceData.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Stateless
public class MaintenanceDataService {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceDataService.class);

    @Inject
    private MaintenanceDataDao maintenanceDataDao;

    public PowerSource getPowerSourceById(Long id) {
        return (PowerSource) maintenanceDataDao.getById(id);
    }

    public List<PowerSource> getPowerSources() {
        return maintenanceDataDao.getAllByType(PowerSource.class);
    }

    public List<PartyType> getPartyTypes() {
        return maintenanceDataDao.getAllByType(PartyType.class);
    }

    public List<FeeType> getFeeTypes() {
        return maintenanceDataDao.getAllByType(FeeType.class);
    }


    public List<GasFeeType> getGasFeeTypes() {
        return maintenanceDataDao.getAllByType(GasFeeType.class);
    }

    public List<AncillaryFee> getAncillaryFees() {
        return maintenanceDataDao.getAllByType(AncillaryFee.class);
    }

    public List<? extends MaintenanceData> getMaintenanceDataListByClass(Class clazz) {
        if (clazz.equals(PowerSource.class)) {
            return getPowerSources();
        }
        if (clazz.equals(PartyType.class)) {
            return getPartyTypes();
        }
        if (clazz.equals(FeeType.class)){
            return getFeeTypes();
        }

        return null;
    }

    public void save(Class<? extends MaintenanceData> clazz, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MaintenanceData o = clazz.getConstructor().newInstance();
        o.setValue(value);
        maintenanceDataDao.save(o);
    }
}