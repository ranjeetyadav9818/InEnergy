package com.inenergis.oneShot.dao;

import com.inenergis.entity.program.ProgramAggregator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface AggregatorDao extends Repository<ProgramAggregator, String> {

    Page<ProgramAggregator> findAll(Pageable pageable);

}
