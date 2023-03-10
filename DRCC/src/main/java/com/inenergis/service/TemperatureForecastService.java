package com.inenergis.service;

import com.inenergis.dao.TemperatureForecastDao;
import com.inenergis.entity.TemperatureForecast;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TemperatureForecastService implements Serializable {
    @Inject
    TemperatureForecastDao temperatureForecastDao;

    public TemperatureForecast getByDate(Date date) {
        return temperatureForecastDao.getByDate(date);
    }
}
