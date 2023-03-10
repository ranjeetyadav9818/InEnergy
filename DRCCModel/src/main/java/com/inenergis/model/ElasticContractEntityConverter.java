package com.inenergis.model;

import com.inenergis.entity.contract.ContractAddress;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.PointOfContact;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by egamas on 01/09/2017.
 */
public class ElasticContractEntityConverter {
    public static ElasticContractEntity convert(ContractEntity contractEntity) {
        final ElasticContractEntity.ElasticContractEntityBuilder builder = ElasticContractEntity.builder();

        if (contractEntity.getId() != null) {
            builder.id(contractEntity.getId());
        }

        builder.businessName(contractEntity.getBusinessName())
                .dba(contractEntity.getDba());
        if (contractEntity.getSector() != null) {
            builder.sector(contractEntity.getSector().getLabel());
        }
        if (StringUtils.isNotEmpty(contractEntity.getTaxId())) {
            builder.taxId(contractEntity.getTaxId());
        }

        convertAddresses(contractEntity, builder);

        convertPOCs(contractEntity, builder);

        return builder.build();
    }

    private static void convertPOCs(ContractEntity contractEntity, ElasticContractEntity.ElasticContractEntityBuilder builder) {
        if (CollectionUtils.isNotEmpty(contractEntity.getPointOfContacts())) {
            List<String> pocNames = new ArrayList();
            List<String> pocTitles = new ArrayList();
            List<String> pocPhones = new ArrayList();
            List<String> pocEmails = new ArrayList();
            for (PointOfContact poc : contractEntity.getPointOfContacts()) {
                if (StringUtils.isNotEmpty(poc.getName())) {pocNames.add(poc.getName());}
                if (StringUtils.isNotEmpty(poc.getTitle())) {pocTitles.add(poc.getTitle());}
                if (CollectionUtils.isNotEmpty(poc.getPocPhones())) {
                    pocPhones.addAll(poc.getPocPhones().stream().map(p->p.getNumber()).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(poc.getPocEmails())) {
                    pocEmails.addAll(poc.getPocEmails().stream().map(e -> e.getEmail()).collect(Collectors.toList()));
                }
            }
            if (CollectionUtils.isNotEmpty(pocNames)) {builder.pocNames(pocNames.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(pocTitles)) {builder.pocTitles(pocTitles.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(pocPhones)) {builder.pocPhones(pocPhones.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(pocEmails)) {builder.pocEmails(pocEmails.stream().distinct().collect(Collectors.toList()));}
        }
    }

    private static void convertAddresses(ContractEntity contractEntity, ElasticContractEntity.ElasticContractEntityBuilder builder) {
        if (CollectionUtils.isNotEmpty(contractEntity.getContractAddresses())) {
            List<String> addresses = new ArrayList();
            List<String> cities = new ArrayList();
            List<String> states = new ArrayList();
            List<String> postCodes = new ArrayList();
            for (ContractAddress address : contractEntity.getContractAddresses()) {
                if (StringUtils.isNotEmpty(address.getAddress1() )) {addresses.add(address.getAddress1());}
                if (StringUtils.isNotEmpty(address.getAddress2() )) {addresses.add(address.getAddress2());}
                if (StringUtils.isNotEmpty(address.getCity() )) {cities.add(address.getCity());}
                if (StringUtils.isNotEmpty(address.getPostCode() )) {postCodes.add(address.getPostCode());}
                if (address.getState() != null) {states.add(address.getState().getLabel());}
            }
            if (CollectionUtils.isNotEmpty(addresses)) {builder.addresses(addresses.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(cities)) {builder.cities(cities.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(states)) {builder.states(states.stream().distinct().collect(Collectors.toList()));}
            if (CollectionUtils.isNotEmpty(postCodes)) {builder.postCodes(postCodes.stream().distinct().collect(Collectors.toList()));}
        }
    }
}
