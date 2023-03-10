package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.LocationSubmissionExceptionDao;
import com.inenergis.dao.LocationSubmissionStatusDao;
import com.inenergis.entity.Lse;
import com.inenergis.entity.SubLap;
import com.inenergis.entity.locationRegistration.LocationSubmissionException;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class LocationSubmissionStatusService {

    @Inject
    LocationSubmissionStatusDao statusDao;

    @Inject
    LocationSubmissionExceptionDao exceptionDao;

    public List<LocationSubmissionStatus> getAll() {
        return statusDao.getAll();
    }

    public List<LocationSubmissionStatus> searchBy(SubLap subLap, Lse lse, List<String> programs, List<String> statuses, Iso iso) {
        List<CriteriaCondition> conditions = new ArrayList<>();

        if (subLap != null) {
            conditions.add(CriteriaCondition.builder().key("isoSublap").value(subLap.getCode()).matchMode(MatchMode.EXACT).build());
        }

        if (lse != null) {
            conditions.add(CriteriaCondition.builder().key("isoLse").value(lse.getCode()).matchMode(MatchMode.EXACT).build());
        }

        if (programs != null && !programs.isEmpty()) {
            conditions.add(CriteriaCondition.builder()
                    .key("programServiceAgreementEnrollment.program.name")
                    .value(programs)
                    .matchMode(MatchMode.EXACT)
                    .build());
        }

        if (statuses != null && !statuses.isEmpty()) {
            conditions.add(CriteriaCondition.builder().key("status").value(statuses).matchMode(MatchMode.EXACT).build());
        }
        if (iso != null) {
            conditions.add(CriteriaCondition.builder().key("iso.id").value(iso.getId()).matchMode(MatchMode.EXACT).build());
        }
        return statusDao.getWithCriteria(conditions);
    }

    public void saveStatus(LocationSubmissionStatus status) {
        statusDao.save(status);
    }

    public LocationSubmissionStatus getStatus(Long id) {
        LocationSubmissionStatus status = statusDao.getById(id);
        for (LocationSubmissionException locationRegistrationException : status.getExceptions()) {
            locationRegistrationException.onload();
        }
        return status;
    }

    public LocationSubmissionException getException(Long id) {
        LocationSubmissionException exception = exceptionDao.getById(id);
        if (exception != null) {
            exception.onload();
        }
        return exception;

    }

    public List<LocationSubmissionException> listAll() {
        return exceptionDao.getAll();
    }

    public void saveOrUpdateStatus(LocationSubmissionStatus status) {
        //This should be called automatically but it is not;
        if (status.getExceptions() != null) {
            for (LocationSubmissionException locationSubmissionException : status.getExceptions()) {
                callExceptionsLifecycle(locationSubmissionException);
            }
        }
        statusDao.saveOrUpdate(status);
    }

    public void saveOrUpdateException(LocationSubmissionException exception) {
        //This should be called automatically but it is not;
        callExceptionsLifecycle(exception);
        exceptionDao.saveOrUpdate(exception);
    }

    private void callExceptionsLifecycle(LocationSubmissionException locationSubmissionException) {
        if (locationSubmissionException.getId() == null) {
            locationSubmissionException.onCreate();
        } else {
            locationSubmissionException.onModifyflushAccumulatedExceptions();
        }
    }

    public List<LocationSubmissionStatus> getBySubLapsAndProgram(List<String> subLaps, Program program) {
        return statusDao.getBySubLapsAndProgram(subLaps, program);
    }
}
