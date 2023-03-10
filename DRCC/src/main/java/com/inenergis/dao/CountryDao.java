package com.inenergis.dao;


import com.inenergis.entity.Country;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
@Getter
@Setter
public class CountryDao extends GenericDao<Country>{
    public CountryDao() {
        setClazz(Country.class);
    }
}
