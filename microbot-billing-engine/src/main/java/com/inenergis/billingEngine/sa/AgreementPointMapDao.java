package com.inenergis.billingEngine.sa;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.BaseServicePoint;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AgreementPointMapDao extends Repository<AgreementPointMap, BaseServicePoint> {


    AgreementPointMap getFirstByServicePointServicePointId(String id);

    AgreementPointMap getFirstByServiceAgreementServiceAgreementId(String saId);

}
