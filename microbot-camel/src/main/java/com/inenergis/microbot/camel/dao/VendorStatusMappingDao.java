package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.VendorStatusMapping;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VendorStatusMappingDao extends Repository<VendorStatusMapping, String> {
    List<VendorStatusMapping> findAll();
}