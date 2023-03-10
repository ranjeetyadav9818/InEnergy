package com.inenergis.microbot.camel.processors;

import com.inenergis.entity.TemperatureForecast;
import com.inenergis.microbot.camel.services.ConfigurationService;
import com.inenergis.network.pgerestclient.PgeLayer7;
import com.inenergis.network.pgerestclient.PgeLayer7Emulator;
import com.inenergis.network.pgerestclient.model.weather.WeatherForecastResponse;
import com.inenergis.network.pgerestclient.model.weather.WeatherForecastUnit;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Properties;

/**
 * Created by egamas on 11/09/2017.
 */
@Component
public class ForecastProcessor implements Processor {

    @Value("${layer7.mocked}")
    private boolean layer7Mocked;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Override
    public void process(Exchange exchange) throws Exception {

        List<TemperatureForecast> result = new ArrayList<>();
        List<WeatherForecastUnit> forecasts = new ArrayList<>();
        Map<LocalDate, List<Integer>> map = new HashMap<>();
        try {
            PgeLayer7 pgeLayer7 = getPgeLayer7();
            WeatherForecastResponse weatherForecast = pgeLayer7.getWeatherForecast();
            forecasts = weatherForecast.getForecasts();
        } catch (Exception e) {
        }

        for (WeatherForecastUnit forecast : forecasts) {
            LocalDate localDate = forecast.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!map.containsKey(localDate)) {
                map.put(localDate, new ArrayList<>());
            }
            map.get(localDate).add(forecast.getTemperature());
        }
        for (Map.Entry<LocalDate, List<Integer>> entry : map.entrySet()) {
            Date date = Date.from(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant());
            OptionalDouble average = entry.getValue().stream()
                    .map(i -> i)
                    .mapToInt(Integer::new)
                    .average();
            result.add(TemperatureForecast.builder().date(date).degrees(BigDecimal.valueOf(average.getAsDouble())).build());
        }
        exchange.getIn().setBody(result);
    }

    public PgeLayer7 getPgeLayer7() throws IOException {
        if (layer7Mocked) {
            return new PgeLayer7Emulator(appProperties);
        } else {
            return new PgeLayer7(appProperties);
        }
    }

}
