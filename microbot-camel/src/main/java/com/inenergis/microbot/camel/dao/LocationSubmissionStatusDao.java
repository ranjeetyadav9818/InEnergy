package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface LocationSubmissionStatusDao extends Repository<LocationSubmissionStatus, Long> {

    List<LocationSubmissionStatus> findByStatusAndMeterDataRecheck(String status, Boolean b);

    List<LocationSubmissionStatus> findByMeterDataRecheck(Boolean b);

    List<LocationSubmissionStatus> findByStatusIn(Collection<String> statuses);

    LocationSubmissionStatus getById(Long id);

    LocationSubmissionStatus save(LocationSubmissionStatus locationSubmissionStatus);
}