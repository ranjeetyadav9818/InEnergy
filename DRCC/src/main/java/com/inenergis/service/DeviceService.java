package com.inenergis.service;

import com.inenergis.dao.DeviceDao;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.model.ElasticDevice;
import com.inenergis.model.ElasticDeviceConverter;
import com.inenergis.util.ElasticActionsUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.inenergis.util.ElasticActionsUtil.ENERGY_ARRAY_INDEX;

@Stateless
public class DeviceService {

    @Inject
    DeviceDao deviceDao;

    @Inject
    ElasticActionsUtil elasticActionsUtil;

    @Transactional
    public AssetDevice saveOrUpdate(AssetDevice device) throws IOException {
        final AssetDevice savedDevice = deviceDao.saveOrUpdate(device);
        elasticActionsUtil.indexDocument(savedDevice.getId().toString(), ElasticDeviceConverter.convert(savedDevice), ENERGY_ARRAY_INDEX, ElasticDevice.ELASTIC_TYPE);
        return savedDevice;
    }

    @Transactional
    public void delete(AssetDevice assetDevice) throws IOException {
        elasticActionsUtil.deleteDocument(assetDevice.getId().toString(), ENERGY_ARRAY_INDEX, ElasticDevice.ELASTIC_TYPE);
        deviceDao.delete(assetDevice);
    }

    @Transactional
    public void saveOrReplace(List<AssetDevice> devices) throws IOException {
        final List<Long> externalIds = devices.stream().map(AssetDevice::getExternalId).filter(Objects::nonNull).collect(Collectors.toList());
        final List<Long> ids = devices.stream().map(AssetDevice::getId).filter(Objects::nonNull).collect(Collectors.toList());
        List<AssetDevice> existingDevices = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(externalIds)) {
            existingDevices.addAll(getByExternalIds(externalIds));
        }
        if (CollectionUtils.isNotEmpty(ids)) {
            existingDevices.addAll(getByIds(ids));
        }
        for (AssetDevice existingDevice : existingDevices.stream().distinct().collect(Collectors.toList())) {
            delete(existingDevice);
        }
        for (AssetDevice device : devices) {
            saveOrUpdate(device);
        }
    }

    private Collection<AssetDevice> getByIds(List<Long> ids) {
        return deviceDao.getByIds(ids);
    }

    public List<AssetDevice> getAll() {
        return deviceDao.getAll();
    }

    public AssetDevice getById(long id) {
        return deviceDao.getById(id);
    }

    public List<AssetDevice> getByExternalIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return deviceDao.getByExternalIds(ids);
    }
}