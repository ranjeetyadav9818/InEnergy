package com.inenergis.oneShot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inenergis.oneShot.dao.AssetDao;
import com.inenergis.entity.asset.Asset;
import com.inenergis.model.ElasticAssetConverter;
import com.inenergis.model.SearchMatch;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.net.UnknownHostException;

import static com.inenergis.model.ElasticAsset.ELASTIC_TYPE;

@Component
public class ElasticAssetService extends ElasticService<Asset> {

    @Autowired
    private AssetDao assetDao;

    @Transactional
    public boolean sendAssetToElastic(JestClient client, int page, int size) throws UnknownHostException, JsonProcessingException {
        return sendObjectToElastic(client, page, size, ELASTIC_TYPE);
    }

    @Override
    protected SearchMatch getElasticObject(Asset asset) {
        return ElasticAssetConverter.convert(asset);
    }

    @Override
    protected Page<Asset> findAll(int page, int size) {
        return assetDao.findAll(new PageRequest(page, size));
    }
}
