package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface RegistrationDao extends Repository<RegistrationSubmissionStatus, Long> {

    RegistrationSubmissionStatus getById(Long id);

    void save(RegistrationSubmissionStatus registrationSubmissionStatus);

    @Modifying
    @Transactional
    @Query("update RegistrationSubmissionStatus set isoBatchId = :isoBatchId where id = :id")
    void updateIsoBatchId(@Param("id") Long id, @Param("isoBatchId") String isoBatchId);

    List<RegistrationSubmissionStatus> findAllByIsoBatchIdIsNullAndInconsistencySolvedIsFalse();

    @Modifying
    @Transactional
    @Query("delete from RegistrationSubmissionStatus where id = :id")
    void deleteById(@Param("id") Long id);
}