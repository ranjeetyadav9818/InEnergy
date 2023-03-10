package com.inenergis.service;

import com.inenergis.commonServices.Layer7PeakDemandHistoryServiceContract;
import com.inenergis.dao.Layer7PeakDemandHistoryDao;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 12/10/2017.
 */
@Getter
@Setter
@Component
public class Layer7PeakDemandHistoryService implements Layer7PeakDemandHistoryServiceContract {

    private static final Logger log = LoggerFactory.getLogger(Layer7PeakDemandHistoryService.class);

    @Autowired
    Layer7PeakDemandHistoryDao layer7PeakDemandHistoryDao;

    @Transactional("mysqlTransactionManager")
    public void save(Layer7PeakDemandHistory layer7PeakDemandHistory) {
        layer7PeakDemandHistoryDao.save(layer7PeakDemandHistory);
    }

    @Transactional("mysqlTransactionManager")
    public List<Layer7PeakDemandHistory> getBy(ServiceAgreement sa, Integer numberOfMonths) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/yyyy");
        return layer7PeakDemandHistoryDao.getAllByServiceAgreement(sa).stream()
                .peek(h -> h.setDate(YearMonth.parse(h.getMonthYear(), formatter).atDay(1)))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .limit(numberOfMonths)
                .collect(Collectors.toList());
    }
}
