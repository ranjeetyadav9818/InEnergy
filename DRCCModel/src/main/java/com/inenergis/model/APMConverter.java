package com.inenergis.model;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServicePoint;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class APMConverter {

    private APMConverter(){}

    public static ElasticAgreementPointMap convert(AgreementPointMap apm){
        ElasticAgreementPointMap elasticAgreementPointMap = new ElasticAgreementPointMap();
        elasticAgreementPointMap.setApmId(apm.getSaSpId());
        elasticAgreementPointMap.setServiceAgreementId(apm.getServiceAgreement().getServiceAgreementId());
        elasticAgreementPointMap.setServicePointId(apm.getServicePoint().getServicePointId());
        elasticAgreementPointMap.setStatus(apm.getServiceAgreement().translateSAStatusLabel());
        elasticAgreementPointMap.setAccountId(apm.getServiceAgreement().getAccount().getAccountId());
        elasticAgreementPointMap.setBusinessName(apm.getServiceAgreement().getAccount().getPerson().getBusinessName());
        elasticAgreementPointMap.setCustomerName(apm.getServiceAgreement().getAccount().getPerson().getCustomerName());
//        elasticAgreementPointMap.setEndDate(getLocalDate(apm.getServiceAgreement().getEndDate()));
//        elasticAgreementPointMap.setStartDate(getLocalDate(apm.getServiceAgreement().getStartDate()));
        elasticAgreementPointMap.setLatitude(getBigDecimal(apm.getServicePoint().getLatitude()));
        elasticAgreementPointMap.setLongitude(getBigDecimal(apm.getServicePoint().getLongitude()));
        elasticAgreementPointMap.setMeterId(((ServicePoint)apm.getServicePoint()).getMeter().getMeterId());
        elasticAgreementPointMap.setPersonId(apm.getServiceAgreement().getAccount().getPerson().getPersonId());
        elasticAgreementPointMap.setPhone(apm.getServiceAgreement().getPhone());
        elasticAgreementPointMap.setPremiseId(apm.getServicePoint().getPremise().getPremiseId());
        elasticAgreementPointMap.setCity(apm.getServicePoint().getPremise().getServiceCityUpr());
        elasticAgreementPointMap.setStreet(apm.getServicePoint().getPremise().getServiceAddress1());
        elasticAgreementPointMap.setPostalCode(apm.getServicePoint().getPremise().getServicePostal());
        elasticAgreementPointMap.setState(apm.getServicePoint().getPremise().getServiceState());
        elasticAgreementPointMap.setMeterBadgeNumber(((ServicePoint)apm.getServicePoint()).getMeter().getBadgeNumber());
        return elasticAgreementPointMap;
    }

    public static BigDecimal getBigDecimal(String value) {
        if(StringUtils.isNotEmpty(value)){
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e){
                System.out.println("number exception: " + value);
                return null;
            }
        } else {
            return null;
        }
    }

    public static LocalDate getLocalDate(Date date) {
        if(date == null){
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
