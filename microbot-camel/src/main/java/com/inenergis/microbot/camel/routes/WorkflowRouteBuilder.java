package com.inenergis.microbot.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class WorkflowRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz2://WorkflowCheckerScheduler?cron=0+*+*+*+*+?").id("WorkflowCheckerScheduler")
            .to("bean:workflowService?method=findAllErrorInstances")
            .split().body()
            .to("bean:workflowService?method=retryInstance")
            .to("jpa:com.inenergis.entity.workflow.PlanInstance");

    }
}
