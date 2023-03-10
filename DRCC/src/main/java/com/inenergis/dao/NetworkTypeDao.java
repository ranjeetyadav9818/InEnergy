package com.inenergis.dao;

import com.inenergis.entity.assetTopology.NetworkType;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

/**
 * Created by egamas on 10/11/2017.
 */
@Stateless
@Transactional
public class NetworkTypeDao extends GenericDao<NetworkType> {

    public NetworkTypeDao() {
        setClazz(NetworkType.class);
    }

}
