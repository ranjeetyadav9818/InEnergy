package com.inenergis.dao;

import com.inenergis.entity.AssetGroup;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class AssetGroupDao extends GenericDao<AssetGroup> {
    public AssetGroupDao() {
        setClazz(AssetGroup.class);
    }

    public List<AssetGroup> getOrderedGroup(){
        Criteria criteria = ((Session) entityManager.getDelegate()).createCriteria(AssetGroup.class)
                .addOrder(Order.asc("level"));
        return criteria.list();
    }
}
