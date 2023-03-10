package com.inenergis.util.asset;

import com.inenergis.controller.general.AssetHelper;
import com.inenergis.entity.DownloadableData;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.CatalogAttribute;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * Created by egamas on 12/12/2017.
 */
@Getter
@Setter
public class AssetSerializer extends DownloadableData implements Serializable {

    private String header = "Commodity,Network Type,External Id,Name,Description,Make,Active,Manufacturer Id,Contract Entity Id,Supplier Part Number,Model,Asset Profile Id,Asset Group Id,Ownership (BTM),Asset Usage (BTM),Asset attributes";
    private Logger logger = LoggerFactory.getLogger(AssetSerializer.class);

    @Override
    protected String serialize(Serializable objectToSerialize) {
        Asset asset = (Asset) objectToSerialize;
        StringJoiner result = new StringJoiner(AssetHelper.SEPARATOR);
        result.add(asset.getAssetProfile().getNetworkType().getCommodityType().name());
        result.add(asset.getAssetProfile().getNetworkType().getName());

        result.add(Long.toString(asset.getExternalId() == null ? asset.getId() : asset.getExternalId()))
                .add(AssetHelper.doubleQuotedString(asset.getName()))
                .add(AssetHelper.doubleQuotedString(asset.getDescription()))
                .add(AssetHelper.doubleQuotedString(asset.getMake()))
                .add((asset.isActive() ? "1" : "0"))
                .add((asset.getManufacturer() == null ? StringUtils.EMPTY : Long.toString(asset.getManufacturer().getId())))
                .add((asset.getSupplier() == null ? StringUtils.EMPTY : Long.toString(asset.getSupplier().getId())))
                .add(StringUtils.defaultIfEmpty(asset.getSupplierPartNumber(), StringUtils.EMPTY))
                .add(StringUtils.defaultIfEmpty(asset.getModel(), StringUtils.EMPTY))
                .add((asset.getAssetProfile() == null ? StringUtils.EMPTY : Long.toString(asset.getAssetProfile().getId())))
                .add((asset.getAssetGroup() == null ? StringUtils.EMPTY : Long.toString(asset.getAssetGroup().getId())))
                .add(asset.getOwnership() == null ? StringUtils.EMPTY : asset.getOwnership().name())
                .add(asset.getUsage() == null ? StringUtils.EMPTY : asset.getUsage().name());

        for (CatalogAttribute attribute : asset.getCatalogAttributes()) {
            result.add(attribute.printValue());
        }

        return result.toString();
    }
}
