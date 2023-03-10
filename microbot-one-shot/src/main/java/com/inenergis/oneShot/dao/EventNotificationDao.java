package com.inenergis.oneShot.dao;

import com.inenergis.entity.PdpSrEvent;
import com.inenergis.entity.program.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface EventNotificationDao extends Repository<PdpSrEvent, Long> {

    Page<PdpSrEvent> findAll(Pageable pageable);

}
