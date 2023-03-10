package com.inenergis.controller.converter;

import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.service.NetworkTypeService;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

/**
 * Created by egamas on 10/11/2017.
 */
@FacesConverter(value = "networkTypeConverter")
public class NetworkTypeConverter extends GenericConverter<NetworkType> {

    @Inject
    NetworkTypeService networkTypeService;

    @Override
    public NetworkType getById(Long id) {
        return networkTypeService.getById(id);
    }
}
