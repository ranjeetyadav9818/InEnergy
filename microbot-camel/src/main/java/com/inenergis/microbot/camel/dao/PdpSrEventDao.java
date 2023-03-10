package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.PdpSrEvent;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface PdpSrEventDao extends Repository<PdpSrEvent, String> {

    PdpSrEvent findFirstByEventUniqueIdAndEventProgramAndEventState(String currentUniqueEventId, String currentProgram, String currentEventState);

    PdpSrEvent findFirstByEventProgramAndEventStartAfterAndEventStartLessThanEqualAndEventEnd(String preferenceCategory, Date dateOne, Date dateTwo, Date dateThree);
}