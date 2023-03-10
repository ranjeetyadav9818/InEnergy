package com.inenergis.rest.services;

import com.inenergis.entity.ServicePoint;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorInput;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorOutput;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorRequest;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorResponse;
import com.inenergis.rest.model.voltageIndicator.VoltageIndicatorSPListOutput;
import com.inenergis.service.ServicePointService;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
public class VoltageIndicatorRESTService {

    @Inject
    ServicePointService servicePointService;

    public VoltageIndicatorResponse getVoltageIndicator(VoltageIndicatorRequest voltageIndicatorList) {

        VoltageIndicatorResponse result = new VoltageIndicatorResponse();
        Collection<String> params = new ArrayList<>(voltageIndicatorList.getVoltageIndicatorRequest().getSpIds().size());

        for (VoltageIndicatorInput voltageIndicatorInput : voltageIndicatorList.getVoltageIndicatorRequest().getSpIds()) {
            params.add(voltageIndicatorInput.getSpId());
        }
        List<ServicePoint> listById = servicePointService.getByIds(params);
        if (CollectionUtils.isEmpty(listById)) {
            return result;
        } else {
            VoltageIndicatorSPListOutput voltageIndicatorSPListOutput = new VoltageIndicatorSPListOutput();
            final ArrayList<VoltageIndicatorOutput> spIds = new ArrayList<>();
            for (ServicePoint servicePoint : listById) {
                VoltageIndicatorOutput voltageIndicatorOutput = new VoltageIndicatorOutput();
                voltageIndicatorOutput.setSpId(servicePoint.getServicePointId());
                voltageIndicatorOutput.setVoltageIndicator(servicePoint.getServiceVoltageClass());
                spIds.add(voltageIndicatorOutput);
            }
            voltageIndicatorSPListOutput.setSpIds(spIds);
            result.setVoltageIndicatorResponse(voltageIndicatorSPListOutput);
        }
        return result;
    }

}
