package com.inenergis.oneShot.dao;

import com.inenergis.entity.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface AssetDao extends Repository<Asset, Long> {
    Page<Asset> findAll(Pageable pageable);
}