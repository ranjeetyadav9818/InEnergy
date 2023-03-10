package com.inenergis.dao;

import com.inenergis.entity.MeterDataUsage;
import com.inenergis.entity.ServiceAgreement;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Stateless
@Transactional
public class MeterDataUsageDao extends GenericDao<MeterDataUsage>  {

    public MeterDataUsageDao(){
        setClazz(MeterDataUsage.class);
    }

    public List<MeterDataUsage> getAllByServicePoint(String servicePointId) {
        return entityManager.createQuery("from MeterDataUsage s WHERE s.servicePointId = :id").setParameter("id", servicePointId).getResultList();
    }
}
