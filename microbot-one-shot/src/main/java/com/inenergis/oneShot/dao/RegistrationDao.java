package com.inenergis.oneShot.dao;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface RegistrationDao extends Repository<RegistrationSubmissionStatus, Long> {
    Page<RegistrationSubmissionStatus> findAll(Pageable pageable);
}