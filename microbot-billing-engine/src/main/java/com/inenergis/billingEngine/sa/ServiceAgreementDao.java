package com.inenergis.billingEngine.sa;

import com.inenergis.entity.BaseServiceAgreement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceAgreementDao extends Repository<BaseServiceAgreement, String> {
    @Transactional("mysqlTransactionManager")
    List<BaseServiceAgreement> findByBillCycleCd(String serial, Pageable pageable);
}
