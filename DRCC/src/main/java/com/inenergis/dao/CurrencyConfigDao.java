package com.inenergis.dao;

import com.inenergis.entity.config.CurrencyConfig;
import lombok.Getter;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
@Getter
public class CurrencyConfigDao extends GenericDao<CurrencyConfig> {

    public CurrencyConfigDao() {
        setClazz(CurrencyConfig.class);
    }
}