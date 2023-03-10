package com.inenergis.oneShot.dao;

import com.inenergis.entity.locationRegistration.IsoResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface ResourceDao extends Repository<IsoResource, Long> {
    Page<IsoResource> findAll(Pageable pageable);
}