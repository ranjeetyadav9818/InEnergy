package com.inenergis.model;

import com.inenergis.entity.marketIntegration.Iso;
import com.inenergis.entity.marketIntegration.IsoProduct;
import com.inenergis.entity.marketIntegration.IsoProfile;
import com.inenergis.entity.program.ProgramAggregator;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class ElasticIsoConverter {

    private ElasticIsoConverter() {
    }

    public static ElasticISO convert(Iso iso) {

        final IsoProfile profile = iso.getActiveProfile();
        final List<String> productNames = new ArrayList();
        final List<String> productTypes = new ArrayList();
        final List<String> marketTypes = new ArrayList();
        final List<String> profileNames = new ArrayList();
        if (CollectionUtils.isNotEmpty(iso.getProfiles())) {
            profileNames .addAll(iso.getProfiles().stream().map(p -> p.getName()).collect(Collectors.toList()));
        }

        ElasticISO.ElasticISOBuilder builder = ElasticISO.builder();
        builder.id(iso.getId());
        if (profile != null) {
            final List<IsoProduct> products = profile.getProducts();
            if (CollectionUtils.isNotEmpty(products)) {
                for (IsoProduct product : products) {
                    productNames.add(product.getName());
                    productTypes.add(product.getType().getName());
                    marketTypes.add(product.getMarketType().getName());
                }
            }
            builder.udcId(profile.getUdcId())
                    .scId(profile.getScId())
                    .drpId(profile.getDrpId());
        }
        return builder
                .id(iso.getId())
                .name(iso.getName())
                .profileNames(profileNames)
                .productNames(productNames)
                .productTypes(productTypes)
                .marketTypes(marketTypes)
                .build();
    }

}
