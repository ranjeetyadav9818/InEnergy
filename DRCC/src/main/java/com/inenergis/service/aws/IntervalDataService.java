package com.inenergis.service.aws;

import com.inenergis.dao.aws.IntervalDataDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.meterData.IntervalData;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class IntervalDataService {

    @Inject
    IntervalDataDao intervalDataDao;

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public ByteArrayInputStream getByServicePointId(String servicePointId) throws IOException, SQLException {
        return intervalDataDao.getByServicePointId(servicePointId);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<IntervalData> getIntervalDate(String servicePointId, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return intervalDataDao.getIntervalData(servicePointId, dateFrom, dateTo);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<DateGroupedIntervalData> getIntervalDataGroupByMonth(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return intervalDataDao.getIntervalDataGroupByMonth(agreementPointMapList, dateFrom, dateTo);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<DateGroupedIntervalData> getIntervalDataGroupByDay(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return intervalDataDao.getIntervalDataGroupByDay(agreementPointMapList, dateFrom, dateTo);
    }
}
