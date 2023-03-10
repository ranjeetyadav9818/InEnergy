package com.inenergis.oneShot.dao;

import com.inenergis.entity.device.AssetDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface AssetDeviceDao extends Repository<AssetDevice, Long> {
    Page<AssetDevice> findAll(Pageable pageable);
}