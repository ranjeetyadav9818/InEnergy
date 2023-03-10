package com.inenergis.oneShot.dao;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface LocationDao extends Repository<LocationSubmissionStatus, Long> {
    Page<LocationSubmissionStatus> findAll(Pageable pageable);
}