package com.inenergis.util.asset;

import com.inenergis.controller.general.AssetHelper;
import com.inenergis.entity.DownloadableData;
import com.inenergis.entity.assetTopology.DeviceAttribute;
import com.inenergis.entity.device.AssetDevice;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.StringJoiner;

/**
 * Created by egamas on 12/12/2017.
 */
@Getter
@Setter
public class DeviceSerializer extends DownloadableData implements Serializable {

    private String header = "Commodity,Network Type,External Id,Asset Id,Name,Description,Address 1,Address 2,Address 3,City,Postcode,Latitude,Longitude,Device Attributes";
    private Logger logger = LoggerFactory.getLogger(DeviceSerializer.class);
    final DecimalFormat f = new DecimalFormat("##.###");

    @Override
    protected String serialize(Serializable objectToSerialize) {
        AssetDevice device = (AssetDevice) objectToSerialize;
        StringJoiner result = new StringJoiner(AssetHelper.SEPARATOR);
        result.add(device.getAsset().getAssetProfile().getNetworkType().getCommodityType().name());
        result.add(device.getAsset().getAssetProfile().getNetworkType().getName());
        result.add(Long.toString(device.getExternalId() == null ? device.getId() : device.getExternalId()))
                .add(Long.toString(device.getAsset().getExternalId() == null ? device.getAsset().getId() : device.getAsset().getExternalId()))
                .add(AssetHelper.doubleQuotedString(device.getName()))
                .add(AssetHelper.doubleQuotedString(device.getDescription()))
                .add(StringUtils.defaultIfEmpty(AssetHelper.doubleQuotedString(device.getAddress1()), StringUtils.EMPTY))
                .add(StringUtils.defaultIfEmpty(AssetHelper.doubleQuotedString(device.getAddress2()), StringUtils.EMPTY))
                .add(StringUtils.defaultIfEmpty(AssetHelper.doubleQuotedString(device.getAddress3()), StringUtils.EMPTY))
                .add(StringUtils.defaultIfEmpty(AssetHelper.doubleQuotedString(device.getCity()), StringUtils.EMPTY))
                .add(StringUtils.defaultIfEmpty(AssetHelper.doubleQuotedString(device.getPostcode()), StringUtils.EMPTY))
                .add(device.getLatitude() == null ? StringUtils.EMPTY : f.format(device.getLatitude().doubleValue()))
                .add(device.getLongitude() == null ? StringUtils.EMPTY : f.format(device.getLongitude().doubleValue()));


        for (DeviceAttribute attribute : device.getDeviceAttributes()) {
            result.add(attribute.printValue());
        }

        return result.toString();
    }
}
