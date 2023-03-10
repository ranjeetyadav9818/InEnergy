package com.inenergis.microbot.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class EventRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz2://EventCheckerScheduler?cron=0+*+*+*+*+?").id("eventCheckScheduler")
            .to("bean:eventNotificationService?method=findAllPendingNotificationEvents")
            .split().body()
            .to("bean:eventNotificationService?method=assignVendorToNotifications")
            .to("bean:eventNotificationService?method=produceFilesToVendors")
            .to("jpa:com.inenergis.entity.Event")
            .setBody(header("VENDOR_FILES"))
            .split().body()
            .process(vendorFileProcessor)
            .to("file:work/drcc/vendors/");
    }

    Processor vendorFileProcessor = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            Map.Entry<String, String> body = (Map.Entry<String, String>) exchange.getIn().getBody();
            exchange.getIn().setBody(body.getValue());
            exchange.getIn().setHeader(Exchange.FILE_NAME,body.getKey()+"/"+(new Date()).getTime()+".txt");
        }
    };
}
