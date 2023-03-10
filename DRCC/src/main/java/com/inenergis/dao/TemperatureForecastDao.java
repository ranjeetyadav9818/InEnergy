package com.inenergis.dao;

import com.inenergis.entity.TemperatureForecast;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Stateless
@Transactional
public class TemperatureForecastDao extends GenericDao<TemperatureForecast> {

    public TemperatureForecastDao() {
        setClazz(TemperatureForecast.class);
    }

    public TemperatureForecast getByDate(Date date) {
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(TemperatureForecast.class);

        criteria.add(Restrictions.eq("date", date));

        return ((TemperatureForecast) criteria.uniqueResult());
    }
}
