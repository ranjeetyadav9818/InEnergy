package com.inenergis.microbot.camel.services;

import com.inenergis.entity.DataMappingType;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.microbot.camel.dao.DataMappingDao;
import com.inenergis.microbot.camel.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.microbot.camel.exception.LocationProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Getter
@Setter
@Service
public class DataMappingService {
    private static final Logger logger = LoggerFactory.getLogger(DataMappingService.class);

    @Autowired
    private DataMappingDao dataMappingDao;

    @Autowired
    private ProgramServiceAgreementEnrollmentDao serviceAgreementEnrollmentDao;


    @Transactional
    public void getLseData(Exchange exchange) {
        ProgramServiceAgreementEnrollment enrollment = serviceAgreementEnrollmentDao.getById(Long.valueOf((String) exchange.getIn().getBody()));
        String lseCode = enrollment.getServiceAgreement().getCustomerLseCode();
        exchange.setProperty("lseData", dataMappingDao.getFirstByTypeAndSource(DataMappingType.LSE, lseCode));
    }

    @Transactional
    public void getSubLapData(Exchange exchange) throws LocationProcessingException {
        ProgramServiceAgreementEnrollment enrollment = serviceAgreementEnrollmentDao.getById(Long.valueOf((String) exchange.getIn().getBody()));
        String feeder;
        try {
            feeder = enrollment.getServiceAgreement().getAgreementPointMaps().get(0).getServicePoint().getFeeder();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new LocationProcessingException("Can't get feeder");
        }
        exchange.setProperty("subLapData", dataMappingDao.getFirstByTypeAndSource(DataMappingType.SUBLAP,feeder));
    }
}
