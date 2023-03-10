package com.inenergis.service;

import com.inenergis.dao.DeviceLinkDao;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.entity.genericEnum.DeviceLinkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * Created by egamas on 27/11/2017.
 */
public class DeviceLinkServiceTest {


    @Mock
    private DeviceLinkDao deviceLinkDao;

    @InjectMocks
    private DeviceLinkService deviceLinkService = new DeviceLinkService();

    List<DeviceLink> deviceLinks;
    List<DeviceLink> existingDeviceLinks;

    private DeviceLink deviceLink1;
    private DeviceLink equalDeviceLink1;
    private DeviceLink otherDeviceLink1;
    private DeviceLink otherDeviceLink2;

    @BeforeEach
    void init() {

        MockitoAnnotations.initMocks(this);

        deviceLink1 = new DeviceLink();
        deviceLink1.setName("name");
        deviceLink1.setType(DeviceLinkType.BIDIRECTIONAL);
        deviceLink1.setAssetProfile(new AssetProfile());
        deviceLink1.setExternalId(10L);

        equalDeviceLink1 = new DeviceLink();
        equalDeviceLink1.setName("name");
        equalDeviceLink1.setType(DeviceLinkType.BIDIRECTIONAL);
        equalDeviceLink1.setAssetProfile(new AssetProfile());
        equalDeviceLink1.setExternalId(10L);

        otherDeviceLink1 = new DeviceLink();
        otherDeviceLink1.setName("name1");
        otherDeviceLink1.setType(DeviceLinkType.BIDIRECTIONAL);
        otherDeviceLink1.setAssetProfile(new AssetProfile());
        otherDeviceLink1.setExternalId(1L);

        otherDeviceLink2 = new DeviceLink();
        otherDeviceLink2.setName("name2");
        otherDeviceLink2.setType(DeviceLinkType.BIDIRECTIONAL);
        otherDeviceLink2.setAssetProfile(new AssetProfile());
        otherDeviceLink2.setExternalId(2L);

        deviceLinks = Arrays.asList(deviceLink1, equalDeviceLink1);
        existingDeviceLinks = Arrays.asList(deviceLink1, otherDeviceLink1, otherDeviceLink2);
        Mockito.when(deviceLinkDao.getByExternalIds(Mockito.anyList())).thenReturn(existingDeviceLinks);

    }

    @Test
    void saveOrReplaceEdgeCase() {
        deviceLinkService.saveOrReplace(deviceLinks);
        Mockito.verify(deviceLinkDao,Mockito.times(3)).deleteByExternalId(Mockito.any());
        Mockito.verify(deviceLinkDao,Mockito.times(2)).saveOrUpdate(Mockito.any());
    }
}
