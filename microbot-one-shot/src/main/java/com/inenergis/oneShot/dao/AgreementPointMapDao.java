package com.inenergis.oneShot.dao;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.ServicePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AgreementPointMapDao extends Repository<AgreementPointMap, String> {

    Page<AgreementPointMap> findAll(Pageable pageable);

}
