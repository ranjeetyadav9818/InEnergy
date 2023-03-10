package com.inenergis.microbot.camel.services;

import com.inenergis.entity.TemperatureForecast;
import com.inenergis.microbot.camel.dao.TemperatureForecastDao;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Service
public class WeatherForecastService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherForecastService.class);

    @Autowired
    private TemperatureForecastDao temperatureForecastDao;

    @Transactional
    public void findTemperatureForecast(Exchange exchange) throws Exception {
        TemperatureForecast temperatureForecast = (TemperatureForecast) exchange.getIn().getBody();
        TemperatureForecast databaseForecast = temperatureForecastDao.getByDate(temperatureForecast.getDate());
        if (databaseForecast != null) {
            databaseForecast.setDegrees(temperatureForecast.getDegrees());
        } else {
            databaseForecast = temperatureForecast;
        }
        exchange.getIn().setBody(databaseForecast);
    }
}
