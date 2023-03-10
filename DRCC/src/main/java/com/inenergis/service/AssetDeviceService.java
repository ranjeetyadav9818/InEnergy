package com.inenergis.service;

import com.inenergis.dao.AssetDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.DeviceDao;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.model.ElasticDevice;
import com.inenergis.model.ElasticDeviceConverter;
import com.inenergis.util.ElasticActionsUtil;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class AssetDeviceService {

    @Inject
    private DeviceDao deviceDao;

    @Inject
    private AssetDao assetDao;

    @Inject
    private ElasticActionsUtil elasticActionsUtil;

    @Transactional
    public AssetDevice saveOrUpdate(AssetDevice asset) throws IOException {
        final AssetDevice assetDeviceSaved = deviceDao.saveOrUpdate(asset);
        elasticActionsUtil.indexDocument(assetDeviceSaved.getId().toString(), ElasticDeviceConverter.convert(assetDeviceSaved), ENERGY_ARRAY_INDEX, ElasticDevice.ELASTIC_TYPE);
        return assetDeviceSaved;
    }

    public List<AssetDevice> getAll() {
        return deviceDao.getAll();
    }

    public List<AssetDevice> getAllByCommodity(CommodityType commodity) {
        return deviceDao.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("asset.assetProfile.networkType.commodityType").value(commodity).matchMode(MatchMode.EXACT).build()));
    }

    @Transactional
    public AssetDevice getById(Long id) {
        return deviceDao.getById(id);
    }

    public Long countByAsset(Asset asset) {
        return deviceDao.countByClass(asset, AssetDevice.class);
    }

    public AssetDevice getByExternalId(Long externalId) {
        return deviceDao.getByExternalId(externalId);
    }

    public List<AssetDevice> getByName(String query) {
        return deviceDao.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("name").value(query).matchMode(MatchMode.ANYWHERE).build()));
    }
}