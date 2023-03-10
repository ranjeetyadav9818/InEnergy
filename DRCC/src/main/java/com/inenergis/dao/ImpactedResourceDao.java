package com.inenergis.dao;

import com.inenergis.entity.program.ImpactedResource;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class ImpactedResourceDao extends GenericDao<ImpactedResource> {
    public ImpactedResourceDao() {
        setClazz(ImpactedResource.class);
    }
}
