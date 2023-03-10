package com.inenergis.service;

import com.inenergis.dao.CriteriaCondition;
import com.inenergis.dao.DeviceLinkDao;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.genericEnum.CommodityType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.criterion.MatchMode;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Getter
@Setter
public class DeviceLinkService {

    @Inject
    DeviceLinkDao deviceLinkDao;

    @Transactional
    public DeviceLink saveOrUpdate(DeviceLink link) {
        return deviceLinkDao.saveOrUpdate(link);
    }

    @Transactional
    public void deleteByExternalId(DeviceLink deviceLink) {
        deviceLinkDao.deleteByExternalId(deviceLink.getExternalId());
    }

    @Transactional
    public void saveOrReplace(List<DeviceLink> deviceLinks) {
        List<DeviceLink> existingDeviceLinks = getByExternalIds(deviceLinks.stream().map(DeviceLink::getExternalId).collect(Collectors.toList()));
        existingDeviceLinks.forEach(this::deleteByExternalId);
        deviceLinks.forEach(this::saveOrUpdate);
    }

    @Transactional
    public DeviceLink getById(Long id) {
        return deviceLinkDao.getById(id);
    }

    @Transactional
    public void remove(DeviceLink deviceLink) {
        deviceLinkDao.delete(deviceLink);
    }

    @Transactional
    public List<DeviceLink> getByExternalIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }
        return deviceLinkDao.getByExternalIds(ids);
    }

    public DeviceLink getByExternalId(long externalId) {
        return deviceLinkDao.getByExternalId(externalId);
    }

    public List<DeviceLink> getAllByCommodity(CommodityType commodityType) {
        return deviceLinkDao.getWithCriteria(Collections.singletonList(CriteriaCondition.builder().key("assetProfile.networkType.commodityType").value(commodityType).matchMode(MatchMode.EXACT).build()));
    }
}