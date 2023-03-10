package com.inenergis.oneShot.dao;

import com.inenergis.entity.marketIntegration.EnergyContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by egamas on 04/09/2017.
 */
public interface ContractDao extends Repository<EnergyContract, Long> {

    Page<EnergyContract> findAll(Pageable pageable);
}
