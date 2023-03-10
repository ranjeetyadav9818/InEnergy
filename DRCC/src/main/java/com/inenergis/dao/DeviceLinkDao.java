package com.inenergis.dao;

import com.inenergis.entity.device.DeviceLink;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
@Transactional
public class DeviceLinkDao extends GenericDao<DeviceLink> {
    public DeviceLinkDao() {
        setClazz(DeviceLink.class);
    }

    @SuppressWarnings("unchecked")
    public List<DeviceLink> getByExternalIds(Collection<Long> ids) {
        return entityManager.createQuery("from DeviceLink a WHERE a.externalId IN :ids")
                .setParameter("ids", ids).getResultList();
    }

    public void deleteByExternalId(Long externalId) {
        final DeviceLink deviceToDelete = getByExternalId(externalId);
        if (deviceToDelete != null) {
            delete(deviceToDelete);
        } else {
            throw new IllegalArgumentException("deviceLink with externalId : " + externalId + " does not exist.");
        }
    }

    public DeviceLink getByExternalId(Long externalId) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("externalId").value(externalId).matchMode(MatchMode.EXACT).build());
        return getUniqueResultWithCriteria(conditions);
    }
}
