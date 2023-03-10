package com.inenergis.dao;

import com.inenergis.entity.assetTopology.AssetProfile;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AssetProfileDao extends GenericDao<AssetProfile>{
    public AssetProfileDao() {
        setClazz(AssetProfile.class);
    }
}
