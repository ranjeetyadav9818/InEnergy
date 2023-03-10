package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.AgreementPointMapPK;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface AgreementPointMapDao extends Repository<AgreementPointMap, Long> {

    AgreementPointMap getById(AgreementPointMapPK agreementPointMapPK);
}