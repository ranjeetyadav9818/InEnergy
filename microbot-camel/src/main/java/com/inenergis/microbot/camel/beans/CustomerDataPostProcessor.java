package com.inenergis.microbot.camel.beans;


import com.inenergis.entity.ServiceAgreement;
import com.inenergis.microbot.camel.csv.CustomerInterface;

public class CustomerDataPostProcessor {

    public void postProcess(CustomerInterface customer){
        customer.setPHONE(ServiceAgreement.stripOutNoDigit(customer.getPHONE()));
        customer.solveBooleanValues();
    }
}
