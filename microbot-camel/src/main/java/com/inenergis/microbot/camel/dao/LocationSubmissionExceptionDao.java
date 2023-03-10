package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LocationSubmissionExceptionDao extends Repository<LocationSubmissionException, Long> {

    List<LocationSubmissionException> getAllByMarkedRetryAndResolved(boolean markedRetry, boolean resolved);

    void save(LocationSubmissionException locationSubmissionException);
}