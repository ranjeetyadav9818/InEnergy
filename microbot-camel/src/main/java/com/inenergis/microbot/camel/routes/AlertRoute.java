package com.inenergis.microbot.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AlertRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        //Profile about to end route
        from("quartz2://retrieveProgramProfilesToEnd?cron=10+0/2+*+*+*+?").id("retrieveProgramProfilesToEnd")
                .to("bean:programProfileToEndService?method=createNewAlerts")
                .transacted()
                .choice()
                    .when(simple("${body} == null"))
                        .log("No alerts generated")
                    .otherwise()
                        .to("jpa:com.inenergis.entity.workflow.NotificationInstance?usePersist=false&flushOnSend=true&joinTransaction=true&entityType=java.util.ArrayList")
                        .log("Notifications created: ${body.size}")
                .end();


        //Send pending messages
        from("quartz2://retrievePendingNotifications?cron=30+0/5+*+*+*+?").id("sendPendingNotifications")
                .to("bean:mailNotificationService?method=retrievePendingNotifications")
                .transacted()
                .split().body()
                .to("bean:mailNotificationService?method=sendNotification")
                .log("Notification ${body.id} sending attempt. Result ${body.sent}")
                .to("jpa:com.inenergis.entity.workflow.NotificationInstance?usePersist=false&flushOnSend=true&joinTransaction=true")
                .log("Notification ${body.id} updated");
    }
}
