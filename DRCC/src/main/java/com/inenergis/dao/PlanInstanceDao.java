package com.inenergis.dao;


import com.inenergis.entity.workflow.PlanInstance;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class PlanInstanceDao extends GenericDao<PlanInstance> {
    public PlanInstanceDao(){
        setClazz(PlanInstance.class);
    }
}
