package com.inenergis.service;

import com.inenergis.dao.CurrencyConfigDao;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.config.CurrencyConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Getter
@Setter
public class CurrencyConfigService {

    @Inject
    CurrencyConfigDao currencyConfigDao;

    public CurrencyConfig getSelected() {
        final List<CurrencyConfig> all = currencyConfigDao.getAll();
        if (CollectionUtils.isNotEmpty(all)) {
            return all.stream().filter(CurrencyConfig::isSelected).findFirst().orElse(null);
        }
        return null;
    }

    public IdentifiableEntity getById(Long id) {
        return currencyConfigDao.getById(id);
    }

    public List<CurrencyConfig> getAll() {
        return currencyConfigDao.getAll();
    }

    @Transactional
    public CurrencyConfig save(CurrencyConfig newValue) {
        return currencyConfigDao.save(newValue);
    }

    @Transactional
    public void saveAll(List<CurrencyConfig> list) {
        for (CurrencyConfig currencyConfig : list) {
            currencyConfigDao.save(currencyConfig);
        }
    }
}