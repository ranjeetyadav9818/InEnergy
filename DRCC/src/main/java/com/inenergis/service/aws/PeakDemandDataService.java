package com.inenergis.service.aws;

import com.inenergis.dao.aws.PeakDemandIntervalDataDao;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.meterData.PeakDemandMeterData;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Stateless
public class PeakDemandDataService {

    @Inject
    private PeakDemandIntervalDataDao peakDemandIntervalDataDao;

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public ByteArrayInputStream getByServicePointId(String servicePointId) throws IOException, SQLException {
        return peakDemandIntervalDataDao.getByServicePointId(servicePointId);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<PeakDemandMeterData> getIntervalDate(String servicePointId, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return peakDemandIntervalDataDao.getByServicePointAndDate(servicePointId, dateFrom, dateTo);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<DateGroupedIntervalData> getIntervalDataGroupByMonth(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return peakDemandIntervalDataDao.getIntervalDataGroupByMonth(agreementPointMapList, dateFrom, dateTo);
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<DateGroupedIntervalData> getIntervalDataGroupByDay(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, SQLException {
        return peakDemandIntervalDataDao.getIntervalDataGroupByDay(agreementPointMapList, dateFrom, dateTo);
    }
}
