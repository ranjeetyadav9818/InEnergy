package com.inenergis.dao;

import com.inenergis.entity.assetTopology.AssetAttribute;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AttributeDao extends GenericDao<AssetAttribute> {
    public AttributeDao() {
        setClazz(AssetAttribute.class);
    }
}
