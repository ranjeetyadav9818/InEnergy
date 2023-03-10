package com.inenergis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inenergis.dao.bigdata.ConsumptionByMonth;
import com.inenergis.dao.bigdata.MeterDataDao;
import com.inenergis.entity.ServicePoint;
import com.inenergis.model.meter.MeterData;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 22/09/2017.
 */
@Getter
@Setter
@Component
public class ConsumptionService {

    private static final Logger log = LoggerFactory.getLogger(ConsumptionService.class);

    @Autowired
    private MeterDataDao meterDataDao;

    @Autowired
    ObjectMapper objectMapper;

    @Transactional("redshiftTransactionManager")
    public List<Object> getConsumptionByMonth(List<ServicePoint> servicePoints){
        return meterDataDao.getConsumptionsByMonth(servicePoints.stream().map(ServicePoint::getServicePointId).collect(Collectors.toList()));
    }
}
