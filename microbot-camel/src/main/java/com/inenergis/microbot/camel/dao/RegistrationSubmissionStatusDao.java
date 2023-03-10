package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface RegistrationSubmissionStatusDao extends Repository<RegistrationSubmissionStatus, Long> {

    List<RegistrationSubmissionStatus> findByRegistrationStatusIn(Collection<RegistrationSubmissionStatus.RegistrationStatus> statuses);
}