package com.inenergis.service;

import com.inenergis.dao.MeterForecastDao;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.trove.MeterForecast;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MeterForecastService implements Serializable {
    @Inject
    MeterForecastDao meterForecastDao;

    public List<MeterForecast> getAll() {
        return meterForecastDao.getAll();
    }

    public List<MeterForecast> getBy(List<String> saIds, String measureType, Date tradeDate) {
        return meterForecastDao.getBy(saIds, measureType, tradeDate);
    }
    public List<MeterForecast> getBy(List<BaseServiceAgreement> serviceAgreements, String measureType, Date start, Date end) {
        return meterForecastDao.getBy(measureType,serviceAgreements,start,end);
    }
}
