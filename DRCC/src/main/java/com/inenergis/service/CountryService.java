package com.inenergis.service;

import com.inenergis.dao.CountryDao;
import com.inenergis.entity.Country;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Getter
@Setter
public class CountryService {

    @Inject
    CountryDao countryDao;

    public List<Country> getAll() {
        return countryDao.getAll();
    }

    public Country getById(Long key) { return countryDao.getById(key); }
}
