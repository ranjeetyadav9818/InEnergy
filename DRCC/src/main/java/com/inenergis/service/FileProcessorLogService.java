package com.inenergis.service;


import com.inenergis.dao.FileProcessorErrorDao;
import com.inenergis.dao.FileProcessorLogDao;
import com.inenergis.entity.log.FileProcessorError;
import com.inenergis.entity.log.FileProcessorLog;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class FileProcessorLogService {

    @Inject
    FileProcessorErrorDao fileProcessorErrorDao;

    @Inject
    FileProcessorLogDao fileProcessorLogDao;

    public void saveError(FileProcessorError fileProcessorError) {
        fileProcessorErrorDao.saveOrUpdate(fileProcessorError);
    }

    public FileProcessorError getErrorById(Long id) {
        return fileProcessorErrorDao.getById(id);
    }

    public List<FileProcessorError> getAllErrors() {
        return fileProcessorErrorDao.getAll();
    }

    public List<FileProcessorLog> getAll() {
        return fileProcessorLogDao.getAll();
    }

//    public List<FileProcessorLog> searchBy(SubLap subLap, Lse lse, List<String> programs) {
//        List<CriteriaCondition> conditions = new ArrayList<>();
//
//        if (subLap != null) {
//            conditions.add(CriteriaCondition.builder().key("isoSublap").value(subLap.getCode()).matchMode(MatchMode.EXACT).build());
//        }
//
//        if (lse != null) {
//            conditions.add(CriteriaCondition.builder().key("isoLse").value(lse.getCode()).matchMode(MatchMode.EXACT).build());
//        }
//
//        if (programs != null && !programs.isEmpty()) {
//            conditions.add(CriteriaCondition.builder()
//                    .key("programServiceAgreementEnrollment.program.name")
//                    .value(programs)
//                    .matchMode(MatchMode.EXACT)
//                    .build());
//        }
//
//        return fileProcessorLogDao.getWithCriteria(conditions);
//    }

}
