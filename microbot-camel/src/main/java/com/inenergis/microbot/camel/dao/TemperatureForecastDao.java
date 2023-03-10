package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.TemperatureForecast;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface TemperatureForecastDao extends Repository<TemperatureForecast, Long> {

    TemperatureForecast getByDate(Date date);
}