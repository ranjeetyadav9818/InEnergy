package com.inenergis.controller.carousel;

import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.genericEnum.AssetOwnership;
import com.inenergis.entity.genericEnum.AssetUsage;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AssetCarousel {

    public void generate(List<EnergyArrayDataBeanList> entityDetails, Asset asset) {

        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Name", asset.getName()));
        cBeans.add(new DataBean("Description", asset.getDescription()));
        if (asset.getAssetProfile() != null) {
            cBeans.add(new DataBean("Asset Profile", asset.getAssetProfile().getName()));
        }
        cBeans.add(new DataBean("Asset Group", asset.getAssetGroup().getName()));
        cBeans.add(new DataBean("Status", asset.isActive() ? "Active" : "Inactive"));


        EnergyArrayDataBeanList customerData = new EnergyArrayDataBeanList("Asset", "icon-account_box2", cBeans);
        customerData.setObject(asset);
        entityDetails.add(customerData);

        //More asset
        List<DataBean> assetBeans2 = new ArrayList<>();
        assetBeans2.add(new DataBean("Supplier Part No.", asset.getSupplierPartNumber()));
        assetBeans2.add(new DataBean("Make", asset.getMake()));
        assetBeans2.add(new DataBean("Model", asset.getModel()));
        final AssetOwnership ownership = asset.getOwnership();
        assetBeans2.add(new DataBean("Ownership", ownership == null ? StringUtils.EMPTY : ownership.getName()));
        final AssetUsage usage = asset.getUsage();
        assetBeans2.add(new DataBean("Usage", usage == null ? StringUtils.EMPTY : usage.getName()));

        EnergyArrayDataBeanList asset2Data = new EnergyArrayDataBeanList("Asset", "icon-account_box2", assetBeans2);
        asset2Data.setObject(asset);
        entityDetails.add(asset2Data);


        // Manufacturer
        List<DataBean> manufacturerBeans = new ArrayList<>();
        manufacturerBeans.add(new DataBean("Name", asset.getManufacturer().getName()));
        manufacturerBeans.add(new DataBean("Point of Contact", asset.getManufacturer().getPointOfContact()));
        manufacturerBeans.add(new DataBean("Phone", asset.getManufacturer().getPhone()));

        EnergyArrayDataBeanList manufacturerData = new EnergyArrayDataBeanList("Manufacturer", "icon-business", manufacturerBeans);
        manufacturerData.setObject(asset);
        entityDetails.add(manufacturerData);
    }
}