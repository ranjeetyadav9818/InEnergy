package com.inenergis.model;

import lombok.Data;

import java.beans.Transient;
import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by Antonio on 18/08/2017.
 */
@Data
public class ElasticAgreementPointMap implements SearchMatch {

    private String apmId;
    private String serviceAgreementId;
    private String servicePointId;
    private String status;
    private String phone;
    private String accountId;
    private String personId;
    private String customerName;
    private String businessName;
    private String premiseId;
    private String meterId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String street;
    private String postalCode;
    private String state;
    private String meterBadgeNumber;

    @Transient
    public String getComputedName(){
        if(isEmpty(customerName)){
            return businessName;
        }
        if(isEmpty(businessName)){
            return customerName;
        }
        return customerName + " (" + businessName + ")";
    }

    @Override
    public String toString(){
        String computedName = getComputedName();
        if (isEmpty(computedName)) {
            return null;
        }
        return serviceAgreementId + ": "+ computedName;
    }

    @Override
    @Transient
    public String getId() {
        return apmId;
    }

    @Override
    public String getType() {
        return "APM";
    }

    @Override
    @Transient
    public String getIcon() {
        return "person_outline";
    }

    @Override
    public String getPermission() {
        return null;
    }

}
