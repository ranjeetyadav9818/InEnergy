package com.inenergis.microbot.camel.services;

import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.trove.MeterForecast;
import com.inenergis.microbot.camel.csv.MeterForecastCsv;
import com.inenergis.microbot.camel.dao.ServiceAgreementDao;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeterForecastService {

    private static final Logger logger = LoggerFactory.getLogger(MeterForecastService.class);

    @Autowired
    private ServiceAgreementDao serviceAgreementDao;

    @Transactional
    public void saveMeterForecast(Exchange exchange) throws Exception {
        try {
            MeterForecastCsv meterForecastCsv = (MeterForecastCsv) exchange.getIn().getBody();
            String serviceAgreementId = meterForecastCsv.getServiceAgreementId();
            ServiceAgreement serviceAgreement = serviceAgreementDao.getByServiceAgreementId(serviceAgreementId);
            if (serviceAgreement == null) {
                String error = "Error retrieving service agreement " + serviceAgreementId;
                logger.error(error);
                throw new Exception(error);
            } else {
                MeterForecast meterForecast = populateMeterForecast(meterForecastCsv);
                meterForecast.setServiceAgreement(serviceAgreement);
            }
        } catch (Exception e) {
            logger.error("Error saving forecast csv", e);
            throw e;
        }
    }

    private MeterForecast populateMeterForecast(MeterForecastCsv meterForecastCsv) {

        try {
            MeterForecast meterForecast = new MeterForecast();
            meterForecast.setMeasureDate(meterForecastCsv.getMeasureDate());
            meterForecast.setMeasureType(meterForecastCsv.getMeasureType());
            meterForecast.setHourEnd1(Integer.valueOf(meterForecastCsv.getHourEnd1()));
            meterForecast.setHourEnd2(Integer.valueOf(meterForecastCsv.getHourEnd2()));
            meterForecast.setHourEnd3(Integer.valueOf(meterForecastCsv.getHourEnd3()));
            meterForecast.setHourEnd4(Integer.valueOf(meterForecastCsv.getHourEnd4()));
            meterForecast.setHourEnd5(Integer.valueOf(meterForecastCsv.getHourEnd5()));
            meterForecast.setHourEnd6(Integer.valueOf(meterForecastCsv.getHourEnd6()));
            meterForecast.setHourEnd7(Integer.valueOf(meterForecastCsv.getHourEnd7()));
            meterForecast.setHourEnd8(Integer.valueOf(meterForecastCsv.getHourEnd8()));
            meterForecast.setHourEnd9(Integer.valueOf(meterForecastCsv.getHourEnd9()));
            meterForecast.setHourEnd10(Integer.valueOf(meterForecastCsv.getHourEnd10()));
            meterForecast.setHourEnd11(Integer.valueOf(meterForecastCsv.getHourEnd11()));
            meterForecast.setHourEnd12(Integer.valueOf(meterForecastCsv.getHourEnd12()));
            meterForecast.setHourEnd13(Integer.valueOf(meterForecastCsv.getHourEnd13()));
            meterForecast.setHourEnd14(Integer.valueOf(meterForecastCsv.getHourEnd14()));
            meterForecast.setHourEnd15(Integer.valueOf(meterForecastCsv.getHourEnd15()));
            meterForecast.setHourEnd16(Integer.valueOf(meterForecastCsv.getHourEnd16()));
            meterForecast.setHourEnd17(Integer.valueOf(meterForecastCsv.getHourEnd17()));
            meterForecast.setHourEnd18(Integer.valueOf(meterForecastCsv.getHourEnd18()));
            meterForecast.setHourEnd19(Integer.valueOf(meterForecastCsv.getHourEnd19()));
            meterForecast.setHourEnd20(Integer.valueOf(meterForecastCsv.getHourEnd20()));
            meterForecast.setHourEnd21(Integer.valueOf(meterForecastCsv.getHourEnd21()));
            meterForecast.setHourEnd22(Integer.valueOf(meterForecastCsv.getHourEnd22()));
            meterForecast.setHourEnd23(Integer.valueOf(meterForecastCsv.getHourEnd23()));
            meterForecast.setHourEnd24(Integer.valueOf(meterForecastCsv.getHourEnd24()));
            return meterForecast;
        } catch (NumberFormatException e) {
            logger.error("Error converting forecast csv to entity", e);
        }
        return null;
    }
}