package com.inenergis.model;

import com.inenergis.entity.device.AssetDevice;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.stream.Collectors;

public final class ElasticDeviceConverter {

    private ElasticDeviceConverter() {
    }

    public static ElasticDevice convert(AssetDevice device) {
        final ElasticDevice.ElasticDeviceBuilder builder = ElasticDevice.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .externalId(device.getExternalId())
                .address1(device.getAddress1())
                .address2(device.getAddress2())
                .address3(device.getAddress3())
                .city(device.getCity())
                .postcode(device.getPostcode())
                .deviceType(device.getClass().getSimpleName());

        if (CollectionUtils.isNotEmpty(device.getDeviceAttributes())) {
            builder.deviceAttributes(device.getDeviceAttributes()
                    .stream()
                    .map(a->{
                        if (StringUtils.isNotEmpty(a.getStringValue())) {
                            return a.getStringValue();
                        } else if (a.getNumberValue() !=null){
                            return a.getNumberValue().toString();
                        }
                        return null;
                    })
                    .filter(StringUtils::isNotEmpty)
                    .distinct().collect(Collectors.toList()));
        }

        return builder.build();
    }
}