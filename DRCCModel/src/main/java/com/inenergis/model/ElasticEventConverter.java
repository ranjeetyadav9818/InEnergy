package com.inenergis.model;

import com.inenergis.entity.Event;
import com.inenergis.entity.PdpSrEvent;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class ElasticEventConverter {

    private ElasticEventConverter(){}

    public static ElasticEvent convert(Event event){
        ElasticEvent elasticEvent = new ElasticEvent();
        elasticEvent.setId(event.getId());
        elasticEvent.setName(event.getName());
        elasticEvent.setDrmsId(event.getProgramId());
        elasticEvent.setDispatchLevel(event.getDispatchLevel()!=null?event.getDispatchLevel().getName():null);
        elasticEvent.setDispatchReason(event.getDispatchReason()!=null?event.getDispatchReason().getName():null);
        elasticEvent.setProgram(event.getProgram().getName());
        return elasticEvent;
    }

}
