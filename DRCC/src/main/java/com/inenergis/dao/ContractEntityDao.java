package com.inenergis.dao;

import com.inenergis.entity.contract.ContractEntity;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ContractEntityDao extends GenericDao<ContractEntity>{
    public ContractEntityDao() {
        setClazz(ContractEntity.class);
    }
}
