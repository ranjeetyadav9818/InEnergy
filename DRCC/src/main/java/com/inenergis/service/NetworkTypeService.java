package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.NetworkTypeDao;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by egamas on 10/11/2017.
 */
@Stateless
public class NetworkTypeService {
    @Inject
    NetworkTypeDao networkTypeDao;

    public List<NetworkType> getAll() {
        return networkTypeDao.getAll();
    }

    public NetworkType getById(Long id) {
        return networkTypeDao.getById(id);
    }

    public List<NetworkType> getAllBy(CommodityType commodityType) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("commodityType").value(commodityType).matchMode(MatchMode.EXACT).build());
        return networkTypeDao.getWithCriteria(conditions);
    }

    public List<NetworkType> getAllByCom(ComodityType commodityType) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("commodityType").value(commodityType).matchMode(MatchMode.EXACT).build());
        return networkTypeDao.getWithCriteria(conditions);
    }

    public NetworkType getBy(CommodityType commodityType, String name) {
        List<CriteriaCondition> conditions = new ArrayList<>();
        conditions.add(CriteriaCondition.builder().key("commodityType").value(commodityType).matchMode(MatchMode.EXACT).build());
        conditions.add(CriteriaCondition.builder().key("name").value(name).matchMode(MatchMode.EXACT).build());
        return networkTypeDao.getUniqueResultWithCriteria(conditions);
    }
}
