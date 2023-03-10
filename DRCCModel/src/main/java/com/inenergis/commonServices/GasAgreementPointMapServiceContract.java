package com.inenergis.commonServices;

import com.inenergis.entity.AgreementPointMap;

import java.util.List;

public interface GasAgreementPointMapServiceContract {
    List<AgreementPointMap> getListByIds(List<String> ids);

}
