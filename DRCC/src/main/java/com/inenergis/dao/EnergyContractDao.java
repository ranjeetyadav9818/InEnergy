package com.inenergis.dao;

import com.inenergis.entity.marketIntegration.EnergyContract;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class EnergyContractDao extends GenericDao<EnergyContract> {
    public EnergyContractDao() {
        setClazz(EnergyContract.class);
    }
}
