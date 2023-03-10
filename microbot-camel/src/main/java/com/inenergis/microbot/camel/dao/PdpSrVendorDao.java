package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.PdpSrVendor;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PdpSrVendorDao extends Repository<PdpSrVendor, String> {
    List<PdpSrVendor> findAll();
}