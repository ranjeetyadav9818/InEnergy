package com.inenergis.dao;

import com.inenergis.entity.asset.Asset;
import lombok.Getter;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Stateless
@Transactional
@Getter
public class AssetDao extends GenericDao<Asset> {

    public AssetDao() {
        setClazz(Asset.class);
    }

    @SuppressWarnings("unchecked")
    public List<Asset> getByExternalIds(Collection<Long> ids) {
        return entityManager.createQuery("from Asset a WHERE a.externalId IN :ids")
                .setParameter("ids", ids).getResultList();
    }

    public Asset getByExternalId(Long id) {
        CriteriaCondition externalId = CriteriaCondition.builder().key("externalId").value(id).matchMode(MatchMode.EXACT).build();
        List<CriteriaCondition> criteria = Arrays.asList(externalId);
        return this.getUniqueResultWithCriteria(criteria);
    }
}