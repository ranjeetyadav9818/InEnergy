package com.inenergis.oneShot.dao;

import com.inenergis.entity.contract.ContractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by egamas on 01/09/2017.
 */
public interface EntityDao  extends Repository<ContractEntity, Long> {

    Page<ContractEntity> findAll(Pageable pageable);
}
