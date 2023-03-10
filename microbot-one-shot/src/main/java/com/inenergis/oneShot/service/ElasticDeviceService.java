package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.AssetDeviceDao;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.model.ElasticDeviceConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticDevice.ELASTIC_TYPE;

@Component
public class ElasticDeviceService extends ElasticService<AssetDevice> {

    @Autowired
    private AssetDeviceDao assetDeviceDao;

    @Transactional
    public boolean sendDevicesToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(AssetDevice assetDevice) {
        return ElasticDeviceConverter.convert(assetDevice);
    }

    @Override
    protected Page<AssetDevice> findAll(int page, int size) {
        return assetDeviceDao.findAll(new PageRequest(page, size));
    }
}