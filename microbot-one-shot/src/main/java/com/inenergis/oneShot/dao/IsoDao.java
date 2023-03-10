package com.inenergis.oneShot.dao;

import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.ProgramAggregator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface IsoDao extends Repository<Iso, Long> {

    Page<Iso> findAll(Pageable pageable);

}
