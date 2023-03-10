package com.inenergis.microbot.camel.routes;


import com.inenergis.microbot.camel.csv.MeterForecastCsv;
import com.inenergis.microbot.camel.processors.MeterForecastPostProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TroveRouteBuilder extends RouteBuilder {

    @Autowired
    @Qualifier("appProperties")
    private Properties p;

    BindyCsvDataFormat bindyForecast = new BindyCsvDataFormat(MeterForecastCsv.class);
    MeterForecastPostProcessor meterForecastPostProcessor = new MeterForecastPostProcessor();

    @Value("${trove.route.enabled}")
    boolean troveRoutEnabled = true;

    @Override
    public void configure() throws Exception {

        if (troveRoutEnabled) {
            log.info("Starting trove route");

            String ftpIn = p.getProperty("ftp.incoming.trove.url");
            String ftpInUser = p.getProperty("ftp.incoming.trove.user");
            String ftpInPassword = p.getProperty("ftp.incoming.trove.password");
            String ftpInUrl = String.format("%s?username=%s&password=%s&download=true&delete=false&recursive=false&scheduler=quartz2&scheduler.cron=0+0+*+*+*+?&" +
                    "idempotent=true&idempotentRepository=#fileStoreForecast",ftpIn,ftpInUser,ftpInPassword);

            if(StringUtils.isNotEmpty(ftpIn)) {
                from(ftpInUrl).id("troveFTPDownload")
                        .log("downloading ${file:name}")
                        .to("file:work/drcc/trove/?fileName=${file:onlyname}");
            } else {
                log.error("Ftp config not found");
            }
            from("file:work/drcc/trove/?charset=cp1252&delete=true")
                    .id("meterForecastFile")
                    .onCompletion().log("Forecast meter data processing finished").end()
                    .onException(Exception.class)
                        .log("Exception on processing csv file")
                        .end()
                    .split(body().tokenize("\n")).streaming()
                    .choice()
                        .when(simple("${body} contains 'sa_id'"))
                        .log("Skipping first line")
                        .endChoice()
                    .otherwise()
                        .to("seda:ProcessMeterForecastLine?size=40&concurrentConsumers=20&blockWhenFull=true")
                    .endChoice()
            .end();

            from("seda:ProcessMeterForecastLine?size=40&concurrentConsumers=1&blockWhenFull=true")
                    .id("meterForecastRow")
                    .onException(Exception.class)
                        .to("seda:failedMeterForecastRow")
                        .log("Exception on processing csv row")
                        .end()
                    .setHeader("originalMessage")
                    .simple("${body}")
                        .unmarshal(bindyForecast)
                        .bean(meterForecastPostProcessor, "postProcess")
                        .to("bean:meterForecastService?method=saveMeterForecast");


            from("seda:failedMeterForecastRow").id("FailedForecastDeadletter")
                    .setBody(simple("${header.originalMessage}\\n"))
                    .log("error with ${header.originalMessage}")
                    .to("file:work/drcc/trove/deadletter/?charset=cp1252&fileName=failedRows&fileExist=append");
        }

    }
}
