package com.inenergis.dao;

import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.device.AssetDevice;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;

@Stateless
@Transactional
public class DeviceDao extends GenericDao<AssetDevice> {
    public DeviceDao() {
        setClazz(AssetDevice.class);
    }

    @SuppressWarnings("unchecked")
    public List<AssetDevice> getByExternalIds(Collection<Long> ids) {
        return entityManager.createQuery("from AssetDevice a WHERE a.externalId IN :ids")
                .setParameter("ids", ids).getResultList();
    }

    @SuppressWarnings("unchecked")
    public AssetDevice getByExternalId(Long id) {
        final List<AssetDevice>  list = entityManager.createQuery("from AssetDevice a WHERE a.externalId = :id")
                .setParameter("id", id).getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public Long countByClass(Asset asset, Class aClass) {
        final Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(aClass);
        criteria.add(eq("asset.id", asset.getId()));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

}
