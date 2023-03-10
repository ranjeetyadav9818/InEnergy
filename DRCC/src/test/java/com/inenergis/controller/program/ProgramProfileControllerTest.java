package com.inenergis.controller.program;

import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.program.NotificationDuplicationSource;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramCustomerNotification;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramProfileAsset;
import com.inenergis.service.AssetService;
import com.inenergis.service.CreditDiscountFeeService;
import com.inenergis.service.HistoryService;
import com.inenergis.service.IsoProductService;
import com.inenergis.service.NetworkTypeService;
import com.inenergis.service.ProgramService;
import com.inenergis.service.SubLapService;
import com.inenergis.util.UIMessage;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.picketlink.Identity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProgramProfileControllerTest {

    @Mock
    ProgramService programService;
    @Mock
    CreditDiscountFeeService creditDiscountFeeService;
    @Mock
    HistoryService historyService;
    @Mock
    IsoProductService isoProductService;
    @Mock
    SubLapService subLapService;
    @Mock
    UIMessage uiMessage;
    @Mock
    Identity identity;
    @Mock
    AssetService assetService;

    @InjectMocks
    private ProgramProfileController programProfileController;

    @Mock
    private NetworkTypeService networkTypeService;

    private ProgramCustomerNotification programCustomerNotification;


    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        final Program renewableProgram = new Program();
        Mockito.when(programService.getProgram(Mockito.any())).thenReturn(renewableProgram);
        Mockito.when(networkTypeService.getAll()).thenReturn(Arrays.asList( NetworkType.builder().commodityType(CommodityType.ELECTRICITY).name("Behind The Meter").build()));
        programProfileController.doInit(0L);
        programCustomerNotification = new ProgramCustomerNotification();
    }

    @Test
    void addNotificationDuplicationSource_Success() {
        programProfileController.addNotificationDuplicationSource(programCustomerNotification);

        assertNotNull(programCustomerNotification.getNotificationDuplicationSources());
        assertEquals(1, programCustomerNotification.getNotificationDuplicationSources().size());
    }

    @Test
    void removeNotificationDuplicationSource_Success() {
        programCustomerNotification.setNotificationDuplicationSources(new ArrayList<>());
        programCustomerNotification.getNotificationDuplicationSources().add(new NotificationDuplicationSource());

        NotificationDuplicationSource source = programCustomerNotification.getNotificationDuplicationSources().get(0);
        programProfileController.removeNotificationDuplicationSource(programCustomerNotification, source);

        assertNotNull(programCustomerNotification.getNotificationDuplicationSources());
        assertEquals(0, programCustomerNotification.getNotificationDuplicationSources().size());
    }

    @Test
    void saveProfileWithAsset() {
        final Asset asset = new Asset();
        asset.setName("Asset1");
        final ProgramProfile profile1 = buildProgramProfile();
        programProfileController.setProfile(profile1);
        programProfileController.syncPickLists();
        programProfileController.addEquipment();
        final ProgramProfile profile = programProfileController.getProfile();
        Assertions.assertNotNull(profile);
        final List<ProgramProfileAsset> programProfileAssets = profile.getProgramProfileAssets();
        Assertions.assertNotNull(programProfileAssets);
    }

    @Test
    void checkRepeatedValuesTest() {
        AssetProfile assetProfile = new AssetProfile();
        assetProfile.setNetworkType(NetworkType.builder().commodityType(CommodityType.ELECTRICITY).name("Behind The Meter").build());
        assetProfile.getNetworkType().setId(1L);
        assetProfile.getNetworkType().setName("Distribution");
        Asset asset1 = new Asset();
        asset1.setName("asset1");
        asset1.setAssetProfile(assetProfile);
        Asset asset2 = new Asset();
        asset2.setName("asset2");
        asset2.setAssetProfile(assetProfile);
        final ProgramProfile profile = buildProgramProfile();
        final ProgramProfileAsset profile1 = new ProgramProfileAsset();
        profile1.setProfile(profile);
        profile1.setAsset(asset1);
        final ProgramProfileAsset profile2 = new ProgramProfileAsset();
        profile2.setProfile(profile);
        profile2.setAsset(asset2);
        final ProgramProfileAsset profile3 = new ProgramProfileAsset();
        profile3.setProfile(profile);
        profile3.setAsset(asset1);
        List<ProgramProfileAsset> programProfileAssets = Arrays.asList(
                profile1,
                profile2,
                profile3);
        String repeated = programProfileController.checkRepeatedValues(programProfileAssets);
        Assertions.assertTrue(StringUtils.isNotEmpty(repeated));

        programProfileAssets = Arrays.asList(
                profile1, profile2);

        repeated = programProfileController.checkRepeatedValues(programProfileAssets);
        Assertions.assertTrue(StringUtils.isEmpty(repeated));

    }

    private ProgramProfile buildProgramProfile() {
        final ProgramProfile profile1 = new ProgramProfile();
        return profile1;
    }

}