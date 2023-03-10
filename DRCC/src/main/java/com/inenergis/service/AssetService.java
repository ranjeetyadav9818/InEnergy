package com.inenergis.service;

import com.inenergis.dao.AssetDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.model.ElasticAsset;
import com.inenergis.model.ElasticAssetConverter;
import com.inenergis.util.ElasticActionsUtil;
import com.inenergis.util.PropertyAccessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
@Getter
@Setter
public class AssetService {

    @Inject
    AssetDao assetDao;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Inject
    PropertyAccessor propertyAccessor;

    @Transactional
    public Asset saveOrUpdate(Asset asset) throws IOException {
        final Asset assetSaved = assetDao.saveOrUpdate(asset);
        elasticActionsUtil.indexDocument(assetSaved.getId().toString(), ElasticAssetConverter.convert(assetSaved), ENERGY_ARRAY_INDEX, ElasticAsset.ELASTIC_TYPE);

        return assetSaved;
    }

    @Transactional
    public void delete(Asset asset) throws IOException {
        assetDao.delete(asset);
        elasticActionsUtil.deleteDocument(asset.getId().toString(), ENERGY_ARRAY_INDEX, ElasticAsset.ELASTIC_TYPE);
    }

    @Transactional
    public void saveOrReplace(List<Asset> assets)  throws IOException {
        final List<Long> externalIds = assets.stream().map(Asset::getExternalId).filter(Objects::nonNull).collect(Collectors.toList());
        final List<Long> ids = assets.stream().map(Asset::getId).collect(Collectors.toList());
        List<Asset> existingAssets = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(externalIds)) {
            existingAssets.addAll(getByExternalIds(externalIds));
    }
        if (CollectionUtils.isNotEmpty(ids)) {
            existingAssets.addAll(getByIds(ids));
        }
        for (Asset existingAsset : existingAssets.stream().distinct().collect(Collectors.toList())) {
            delete(existingAsset);
        }
        for (Asset asset : assets) {
            saveOrUpdate(asset);
        }
    }

    public List<Asset> getAll() {
        return assetDao.getAll();
    }

    public Asset getById(Long id) {
        return assetDao.getById(id);
    }

    public Asset getByExternalId(Long id) {
        Asset asset = assetDao.getByExternalId(id);
        if (asset == null) {
            return assetDao.getById(id);
        }
        return asset;
    }
    public List<Asset> getByExternalIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return assetDao.getByExternalIds(ids);
    }

    public List<Asset> getByIds(List<Long> ids) {
        return assetDao.getByIds(ids);
    }

    public List<Asset> getByProfile(AssetProfile assetProfile) {
        List<CriteriaCondition> criteriaConditions = Arrays.asList(
                CriteriaCondition.builder().key("assetProfile.id").matchMode(MatchMode.EXACT).value(assetProfile.getId()).build());
        return assetDao.getWithCriteria(criteriaConditions);
    }
    public List<Asset> getAllByNetworkType(Long networkTypeId) {
        return assetDao.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("assetProfile.networkType.id").value(networkTypeId).matchMode(MatchMode.EXACT).build()));
    }

    public List<Asset> getAllByCommodity(CommodityType commodityType) {
        return assetDao.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("assetProfile.networkType.commodityType").value(commodityType).matchMode(MatchMode.EXACT).build()));
    }
}