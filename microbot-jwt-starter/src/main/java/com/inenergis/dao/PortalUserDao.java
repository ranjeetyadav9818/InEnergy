package com.inenergis.dao;

import com.inenergis.entity.PortalUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egamas on 22/09/2017.
 */
public interface PortalUserDao extends Repository<PortalUser,Long>{

    @Transactional("mysqlTransactionManager")
    PortalUser findByEmail(String email);

    @Transactional("mysqlTransactionManager")
    @Modifying
    void save(PortalUser user);

}
