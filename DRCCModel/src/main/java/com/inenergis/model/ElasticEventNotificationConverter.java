package com.inenergis.model;

import com.inenergis.entity.PdpSrEvent;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class ElasticEventNotificationConverter {

    private ElasticEventNotificationConverter(){}

    public static ElasticEventNotification convert(PdpSrEvent event){
        ElasticEventNotification elasticEvent = new ElasticEventNotification();
        elasticEvent.setId(event.getEventId());
        elasticEvent.setName(event.getEventName() + " " + event.getEventProgram());
        elasticEvent.setOptions(event.getEventOptions());
        elasticEvent.setEventType(event.getEventType());
        return elasticEvent;
    }

}
