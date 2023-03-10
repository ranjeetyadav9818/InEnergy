package com.inenergis.dao;

import com.inenergis.entity.ContractType;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ContractTypeDao extends GenericDao<ContractType>{
    public ContractTypeDao() {
        setClazz(ContractType.class);
    }
}