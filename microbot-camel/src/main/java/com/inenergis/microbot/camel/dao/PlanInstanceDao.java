package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.workflow.PlanInstance;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface PlanInstanceDao extends Repository<PlanInstance, Long> {

    PlanInstance save(PlanInstance planInstance);
}