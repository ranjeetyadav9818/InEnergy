package com.inenergis.util.asset;

import com.inenergis.controller.general.AssetHelper;
import com.inenergis.entity.DownloadableData;
import com.inenergis.entity.assetTopology.ConnectionDeviceAttribute;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLink;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by egamas on 12/12/2017.
 */
@Getter
@Setter
public class LinkSerializer extends DownloadableData implements Serializable {

    private String header = "Commodity,Network type,External Id,Source Id,Target Id,Name,Direction,Operation,Asset Type Profile,Connection Attributes";
    private Logger logger = LoggerFactory.getLogger(LinkSerializer.class);
    final DecimalFormat f = new DecimalFormat("##.###");

    //todo test
    @Override
    protected String serialize(Serializable objectToSerialize) {
        DeviceLink deviceLink = (DeviceLink) objectToSerialize;
        List<String> lines = new ArrayList<>();
        Iterator<AssetDevice> targets = deviceLink.getTargets().iterator();
        for (AssetDevice device : deviceLink.getSources()) {
            AssetDevice target = null;
            if (!targets.hasNext()) {
                targets = deviceLink.getTargets().iterator();
            }
            if (targets.hasNext()) {
                target = targets.next();
            }
            StringJoiner result = new StringJoiner(AssetHelper.SEPARATOR);
            result.add(deviceLink.getAssetProfile().getNetworkType().getCommodityType().name())
                    .add(deviceLink.getAssetProfile().getNetworkType().getName())
                    .add(Long.toString(deviceLink.getExternalId() == null ? deviceLink.getId() : deviceLink.getExternalId()))
                    .add(Long.toString(device.getExternalId() == null ? device.getId() : device.getExternalId()))
                    .add(target == null ? StringUtils.EMPTY : Long.toString(target.getExternalId() == null ? target.getId() : target.getExternalId()))
                    .add(AssetHelper.doubleQuotedString(deviceLink.getName()))
                    .add(deviceLink.getType().name())
                    .add(lines.isEmpty() ? StringUtils.EMPTY : "ADD")
                    .add(Long.toString(deviceLink.getAssetProfile().getId()));


            for (ConnectionDeviceAttribute attribute : deviceLink.getLinkAttributes()) {
                result.add(attribute.printValue());
            }
            lines.add(result.toString());
        }
        return lines.stream().filter(StringUtils::isNotEmpty).filter(s -> !"\n".equals(s)).distinct().collect(Collectors.joining("\n"));
    }
}
