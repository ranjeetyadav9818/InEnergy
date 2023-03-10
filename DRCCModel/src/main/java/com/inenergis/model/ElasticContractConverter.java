package com.inenergis.model;

import com.inenergis.entity.marketIntegration.Commodity;
import com.inenergis.entity.marketIntegration.EnergyContract;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 04/09/2017.
 */
public class ElasticContractConverter {

    public static ElasticContract convert(EnergyContract contract) {

        final ElasticContract.ElasticContractBuilder builder = ElasticContract.builder();

        if (contract.getId() != null) {
            builder.id(contract.getId());
        }
        if (contract.getType() != null) {
            builder.typeName(contract.getType().getName());
        }
        if (StringUtils.isNotEmpty(contract.getName())) {builder.name(contract.getName());}

        convertCommodities(contract, builder);

        if (CollectionUtils.isNotEmpty(contract.getParties())) {
            builder.partyNames(contract.getParties().stream().filter(p -> p.getEntity() != null).map(p -> p.getEntity().getBusinessName()).collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(contract.getResources())) {
            builder.resourceNames(contract.getResources().stream().map(r -> r.getName()).collect(Collectors.toList()));
        }

        if (CollectionUtils.isNotEmpty(contract.getContractServiceAgreements())) {
            builder.serviceAgreements(contract.getContractServiceAgreements().stream().map(s -> s.getServiceAgreementId()).collect(Collectors.toList()));
        }

        return builder.build();
    }

    private static void convertCommodities(EnergyContract contract, ElasticContract.ElasticContractBuilder builder) {
        if (CollectionUtils.isNotEmpty(contract.getCommodities())) {
            List<String> programCommodityNames = new ArrayList();
            List<String> subProgramCommodityNames = new ArrayList();
            List<String> commodityTypes = new ArrayList();
            List<String> products = new ArrayList();
            List<String> powerSources = new ArrayList();
            for (Commodity commodity : contract.getCommodities()) {
                if (commodity.getCommodityProgram() != null) {
                    if (StringUtils.isNotEmpty(commodity.getCommodityProgram().getName())) {
                        programCommodityNames.add(commodity.getCommodityProgram().getName());
                    }
                }
                if (commodity.getCommoditySubProgram() != null) {
                    if (StringUtils.isNotEmpty(commodity.getCommoditySubProgram().getName())) {
                        subProgramCommodityNames.add(commodity.getCommoditySubProgram().getName());
                    }
                }
                if (commodity.getCommodityType() != null) {
                    commodityTypes.add(commodity.getCommodityType().getName());
                }
                if (commodity.getCommodityProductType() != null) {
                    if (StringUtils.isNotEmpty(commodity.getCommodityProductType().getName())) {
                        products.add(commodity.getCommodityProductType().getName());
                    }
                }
                if (commodity.getCommodityPowerSource() != null) {
                    if (StringUtils.isNotEmpty(commodity.getCommodityPowerSource().getName())) {
                        powerSources.add(commodity.getCommodityPowerSource().getName());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(programCommodityNames)) {
                builder.programCommodityNames(programCommodityNames.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(subProgramCommodityNames)) {
                builder.subProgramCommodityNames(subProgramCommodityNames.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(commodityTypes)) {
                builder.commodityTypes(commodityTypes.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(products)) {
                builder.productTypes(products.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(powerSources)) {
                builder.powerSources(powerSources.stream().distinct().collect(Collectors.toList()));
            }
        }
    }
}
