package com.inenergis.network.pgerestclient.model.weather;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "weather_forecast")
public class WeatherForecastResponse {

    @XmlElement(name = "station_id")
    private String station;
    @XmlElement(name = "forecast")
    private List<WeatherForecastUnit> forecasts;
}