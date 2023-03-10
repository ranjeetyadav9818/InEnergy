package com.inenergis.dao;

import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by egamas on 04/10/2017.
 */
public interface Layer7PeakDemandHistoryDao extends Repository<Layer7PeakDemandHistory,Long> {

    @Transactional("mysqlTransactionManager")
    @Modifying
    Layer7PeakDemandHistory save(Layer7PeakDemandHistory history);

    @Transactional("mysqlTransactionManager")
    List<Layer7PeakDemandHistory> getAllByServiceAgreement(ServiceAgreement serviceAgreement);
}
