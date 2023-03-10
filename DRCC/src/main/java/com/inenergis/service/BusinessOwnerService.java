package com.inenergis.service;

import com.inenergis.dao.BusinessOwnerDao;
import com.inenergis.dao.CriteriaCondition;
import com.inenergis.entity.workflow.BusinessOwner;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BusinessOwnerService {

    private static final Logger log = LoggerFactory.getLogger(BusinessOwnerService.class);

    @Inject
    BusinessOwnerDao ownerDao;

    public void save(BusinessOwner owner) {
        ownerDao.saveOrUpdate(owner);
    }

    public List<BusinessOwner> getAll() {
        return ownerDao.getAll();
    }

    public List<BusinessOwner> getOwners(String name, String recipient) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            conditions.add(CriteriaCondition.builder().key("name").value(name).matchMode(MatchMode.START).build());
        }
        if (StringUtils.isNotBlank(recipient)) {
            conditions.add(CriteriaCondition.builder().key("emailList").value(recipient).matchMode(MatchMode.ANYWHERE).build());
        }
        return ownerDao.getWithCriteria(conditions);
    }

    public BusinessOwner getById(Long id) {
        return ownerDao.getById(id);
    }
}