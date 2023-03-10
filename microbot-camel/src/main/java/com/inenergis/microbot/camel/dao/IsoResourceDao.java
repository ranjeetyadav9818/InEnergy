package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.ProductType;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.marketIntegration.IsoProduct;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IsoResourceDao extends Repository<IsoResource, Long> {

    List<IsoResource> findByIsoLseAndIsoSublapAndType(String lse, String sublap, ProductType type);

    List<IsoResource> findByIsoSublapAndIsoProduct(String sublap, IsoProduct isoProduct);

    IsoResource findByName(String name);
}