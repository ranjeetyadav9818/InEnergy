package com.inenergis.microbot.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class AwardADSRouteBuilder extends RouteBuilder {

    @Autowired
    @Qualifier("appProperties")
    private Properties properties;

    @Override
    public void configure() throws Exception {

        if (properties.getProperty("award.enabled").equals("false")) {
            return;
        }

        from("quartz2://awardADSRetriever?cron=0+*+*+*+*+?").id("awardADSRetriever")
                .onException(Throwable.class)
                    .log("ADS retrieving failed, exception: ${exception.stacktrace}")
                    .maximumRedeliveries(2)
                    .redeliveryDelay(15000)
                    .handled(true)
                .end()
                .log("retrievingADS")
                .to("bean:awardService?method=getLastUUID")
                .to("bean:awardService?method=getNewBatchesFromADS")
                .to("direct:batchDetailsSplitter")
                .to("bean:awardService?method=loadTrajectories")
                .choice()
                    .when(simple("${header.newBatchData}"))
                        .to("bean:awardService?method=saveTrajectories")
                        .to("direct:instructionSaver")
                .otherwise()
                        .log("no new awards in ADS")
                .end()
                .to("bean:awardService?method=saveLastUUID");

        from("direct:batchDetailsSplitter").id("batchDetailsSplitter")
                .split().body()
                .to("direct:batchDetailsRetriever");

        from("direct:batchDetailsRetriever").id("batchDetailsRetriever")
                .onException(Throwable.class)
                    .maximumRedeliveries(5)
                    .redeliveryDelay(100)
                    .handled(false)
                .end()
                .to("bean:awardService?method=getBatchDetails");

        from("direct:instructionSaver").id("instructionSaver")
                .split().body()
                .to("bean:awardService?method=saveInstructions");

    }
}
