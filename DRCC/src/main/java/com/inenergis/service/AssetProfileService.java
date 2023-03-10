package com.inenergis.service;

import com.inenergis.dao.AssetProfileDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.AssetProfileType;
import com.inenergis.entity.genericEnum.CommodityType;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Stateless
public class AssetProfileService {
    @Inject
    private AssetProfileDao assetProfileDao;

    public List<AssetProfile> getAll() {
        return assetProfileDao.getAll();
    }

    public void delete(AssetProfile profileToDelete) {
        assetProfileDao.delete(profileToDelete);
    }

    public AssetProfile save(AssetProfile newProfile) {
        return assetProfileDao.saveOrUpdate(newProfile);
    }

    public AssetProfile getById(Long profileId) {
        return assetProfileDao.getById(profileId);
    }

    public List<AssetProfile> getProfilesByGrid(NetworkType networkType) {
        List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("networkType.id").matchMode(MatchMode.EXACT).value(networkType.getId()).build());
        return assetProfileDao.getWithCriteria(criteriaConditions);
    }

    public List<AssetProfile> getProfilesByCommodityAndType(CommodityType commodityType, AssetProfileType assetProfileType) {
        List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("assetProfileType").matchMode(MatchMode.EXACT).value(assetProfileType).build(),
                CriteriaCondition.builder().key("networkType.commodityType").matchMode(MatchMode.EXACT).value(commodityType).build());
        return assetProfileDao.getWithCriteria(criteriaConditions);
    }


    public List<AssetProfile> getBYNetworkType(Long id, AssetProfileType assetProfileType) {
        List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("assetProfileType").matchMode(MatchMode.EXACT).value(assetProfileType).build(),
                CriteriaCondition.builder().key("networkType.id").matchMode(MatchMode.EXACT).value(id).build());
        return assetProfileDao.getWithCriteria(criteriaConditions);
    }
}
