package com.inenergis.oneShot.dao;

import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.program.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface ProgramDao extends Repository<Program, Long> {

    Page<Program> findAll(Pageable pageable);

}
