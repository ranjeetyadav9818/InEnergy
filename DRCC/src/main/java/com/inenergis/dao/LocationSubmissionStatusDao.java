package com.inenergis.dao;

import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.program.Program;
import org.hibernate.criterion.MatchMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationSubmissionStatusDao extends GenericDao<LocationSubmissionStatus> {
    public LocationSubmissionStatusDao() {
        setClazz(LocationSubmissionStatus.class);
    }

    public void find(String s, int limit) {
        getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("name").matchMode(MatchMode.START).value(s).build()), limit);
    }


    public List<LocationSubmissionStatus> getBySubLapsAndProgram(List<String> subLaps, Program program) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("isoSublap").value(subLaps).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("programServiceAgreementEnrollment.program").value(program).matchMode(MatchMode.EXACT).build());
        return getWithCriteria(conditions);
    }
}
