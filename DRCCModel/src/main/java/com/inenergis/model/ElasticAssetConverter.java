package com.inenergis.model;

import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.assetTopology.AssetAttribute;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public final class ElasticAssetConverter {

    private ElasticAssetConverter() {
    }

    public static ElasticAsset convert(Asset asset) {
        final ElasticAsset.ElasticAssetBuilder builder = ElasticAsset.builder()
                .id(asset.getId())
                .name(asset.getName())
                .description(asset.getDescription())
                .make(asset.getMake())
                .supplierPartNumber(asset.getSupplierPartNumber())
                .model(asset.getModel())
                .assetType(asset.getClass().getSimpleName());

        if (CollectionUtils.isNotEmpty(asset.getCatalogAttributes())) {
            builder.catalogAttributes(asset.getCatalogAttributes()
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