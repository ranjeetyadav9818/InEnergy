package com.inenergis.service;

import com.inenergis.dao.HistoryDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.History;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.RatePlanProfile;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class HistoryService {

    @Inject
    HistoryDao historyDao;

    @Inject
    EntityManager entityManager;

    public List<History> getHistory(AgreementPointMap agreementPointMap) {
        return historyDao.getHistory(agreementPointMap);
    }

    public List<History> getHistory(ProgramProfile profile) {
        return historyDao.getHistory(profile);
    }

    public List<History> getHistory(IsoProfile profile) {
        return historyDao.getHistory(profile);
    }

    @Transactional
    public void saveHistory(History history){
        historyDao.save(history);
    }

    @Transactional
    public void saveHistory(List<History> histories) {
        if(histories!=null){
            for (History history : histories) {
                saveHistory(history);
            }
        }
    }

    public List<History> getHistory(LocationSubmissionStatus locationSubmissionStatus) {
        return historyDao.getHistory(locationSubmissionStatus);
    }

    public List<History> getHistory(RatePlanProfile profile) {
        return historyDao.getHistory(profile);
    }
}