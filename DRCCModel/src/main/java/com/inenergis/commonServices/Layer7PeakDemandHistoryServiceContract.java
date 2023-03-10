package com.inenergis.commonServices;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.ServiceAgreement;

import java.util.List;

/**
 * Created by egamas on 13/10/2017.
 */
public interface Layer7PeakDemandHistoryServiceContract {
    void save(Layer7PeakDemandHistory layer7PeakDemandHistory);

    List<Layer7PeakDemandHistory> getBy(BaseServiceAgreement sa, Integer numberOfMonths);

}
