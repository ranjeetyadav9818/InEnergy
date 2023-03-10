package com.inenergis.dao;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import com.inenergis.entity.workflow.BusinessOwner;

@Stateless
@Transactional
public class BusinessOwnerDao extends GenericDao<BusinessOwner>  {

    public BusinessOwnerDao(){
        setClazz(BusinessOwner.class);
    }

}
