package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.ServiceAgreement;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface ServiceAgreementDao extends Repository<ServiceAgreement, Long> {

    ServiceAgreement getByServiceAgreementId(String id);
}