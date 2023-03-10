package com.inenergis.service;

import com.inenergis.commonServices.Layer7PeakDemandHistoryServiceContract;
import com.inenergis.dao.Layer7PeakDemandHistoryDao;
import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;
import lombok.Getter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Getter
public class Layer7PeakDemandHistoryService implements Layer7PeakDemandHistoryServiceContract {

    @Inject
    Layer7PeakDemandHistoryDao layer7PeakDemandHistoryDao;

    @Override
    public void save(Layer7PeakDemandHistory layer7PeakDemandHistory) {
        layer7PeakDemandHistoryDao.saveOrUpdate(layer7PeakDemandHistory);
    }

    @Override
    public List<Layer7PeakDemandHistory> getBy(BaseServiceAgreement sa, Integer numberOfMonths) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/yyyy");
        return layer7PeakDemandHistoryDao.getBy(sa).stream()
                .peek(h -> h.setDate(YearMonth.parse(h.getMonthYear(), formatter).atDay(1)))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(numberOfMonths)
                .collect(Collectors.toList());
    }
}
