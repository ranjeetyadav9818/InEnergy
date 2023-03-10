package com.inenergis.controller.carousel;

import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.AssetGroup;
import com.inenergis.entity.Manufacturer;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetProfile;
import com.inenergis.entity.genericEnum.AssetOwnership;
import com.inenergis.entity.genericEnum.AssetUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetCarouselTest {

    private AssetCarousel assetCarousel;

    private List<EnergyArrayDataBeanList> entityDetails;
    private Asset asset;

    @BeforeEach
    void setUp() {
        assetCarousel = new AssetCarousel();

        entityDetails = new ArrayList<>();
        asset = new Asset();
        asset.setAssetProfile(new AssetProfile());
        asset.setOwnership(AssetOwnership.CUSTOMER);
        asset.setUsage(AssetUsage.BOTH);
        asset.setManufacturer(new Manufacturer());
        asset.setAssetGroup(new AssetGroup());
    }

    @Test
    void generate() {
        assetCarousel.generate(entityDetails, asset);
        assertEquals(3, entityDetails.size());
        assertEquals(asset, entityDetails.get(0).getObject());
    }
}