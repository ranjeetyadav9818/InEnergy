package com.inenergis.commonServices;

import com.inenergis.entity.AgreementPointMap;

import java.util.List;

/**
 * Created by egamas on 13/10/2017.
 */
public interface AgreementPointMapServiceContract {
    List<AgreementPointMap> getListByIds(List<String> ids);


}
