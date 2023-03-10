package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.PdpSrParticipant;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface PdpSrParticipantDao extends Repository<PdpSrParticipant, String> {

    PdpSrParticipant findFirstByPdpSrEventEventIdAndDruid(Long id, String druid);
}