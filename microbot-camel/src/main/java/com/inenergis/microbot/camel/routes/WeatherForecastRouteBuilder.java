package com.inenergis.microbot.camel.routes;

import com.inenergis.microbot.camel.processors.ForecastProcessor;
import com.inenergis.microbot.camel.services.ConfigurationService;
import com.inenergis.microbot.camel.services.IniConfigurationService;
import com.inenergis.util.ConstantsProviderModel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherForecastRouteBuilder extends RouteBuilder {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    IniConfigurationService iniConfigurationService;

    @Autowired
    private ForecastProcessor forecastProcessor;

    @Override
    public void configure() throws Exception {

        if (!ConstantsProviderModel.TRUE.equals(configurationService.getRouteEnableWeatherForecastRoute())) {
            log.info(" WeatherForecastRouteBuilder disabled ");
            return;
        } else {
            log.info(" WeatherForecastRouteBuilder enabled ");
        }

        from("direct:forecastRouteTest").to("seda:weatherForecast").end();

        from("quartz2://WeatherForecastScheduler?cron=0+52+0/6+*+*+?").to("seda:weatherForecast").end();

        from("seda:weatherForecast").id("WeatherForecastScheduler")
                .onCompletion().log("temperature forecasts updated").end()
                .process(forecastProcessor)
                .split().body()
                .to("bean:weatherForecastService?method=findTemperatureForecast")
                .choice()
                .when(simple("${body.getId} == null"))
                .to("jpa:com.inenergis.entity.TemperatureForecast")
                .otherwise()
                .to("jpa:com.inenergis.entity.TemperatureForecast?usePersist=false&flushOnSend=true&joinTransaction=true")
                .end();

    }

}
