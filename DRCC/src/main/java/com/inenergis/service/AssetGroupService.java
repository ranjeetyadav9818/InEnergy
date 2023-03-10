package com.inenergis.service;

import com.inenergis.dao.AssetGroupDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.genericEnum.CommodityType;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Stateless
public class AssetGroupService {

    @Inject
    AssetGroupDao assetGroupDao;

    public AssetGroup saveOrUpdate(AssetGroup group) {
        return assetGroupDao.saveOrUpdate(group);
    }

    public List<AssetGroup> getAll() {
        return assetGroupDao.getOrderedGroup();
    }

    public void delete(AssetGroup group) {
        assetGroupDao.delete(group);
    }

    public AssetGroup getById(Long id) {
        return assetGroupDao.getById(id);
    }

    public List<AssetGroup> getAllBYComodityType(CommodityType commodityType) {
        List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("commodityType").matchMode(MatchMode.EXACT).value(commodityType).build());
        return assetGroupDao.getWithCriteria(criteriaConditions);
    }
}